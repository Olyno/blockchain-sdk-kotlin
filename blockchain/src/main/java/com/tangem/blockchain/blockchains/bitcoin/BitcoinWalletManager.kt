package com.tangem.blockchain.blockchains.bitcoin

import android.util.Log
import com.tangem.blockchain.blockchains.bitcoin.network.BitcoinAddressInfo
import com.tangem.blockchain.blockchains.bitcoin.network.BitcoinNetworkProvider
import com.tangem.blockchain.common.*
import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.SimpleResult
import com.tangem.commands.SignResponse
import com.tangem.common.CompletionResult
import com.tangem.common.extensions.toHexString
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal

open class BitcoinWalletManager(
        wallet: Wallet,
        protected val transactionBuilder: BitcoinTransactionBuilder,
        private val networkProvider: BitcoinNetworkProvider
) : WalletManager(wallet), TransactionSender, SignatureCountValidator {

    protected val blockchain = wallet.blockchain
    open val minimalFeePerKb = 0.0001.toBigDecimal()
    open val minimalFee = 0.00001.toBigDecimal()

    override suspend fun update() {
        coroutineScope {
            val addressInfos = mutableListOf<BitcoinAddressInfo>()
            val responsesDeferred =
                    wallet.addresses.map { async { networkProvider.getInfo(it.value) } }

            responsesDeferred.forEach {
                when (val response = it.await()) {
                    is Result.Success -> addressInfos.add(response.data)
                    is Result.Failure -> {
                        updateError(response.error)
                        return@coroutineScope
                    }
                }
            }
            updateWallet(addressInfos.merge())
        }
    }

    private fun List<BitcoinAddressInfo>.merge(): BitcoinAddressInfo {
        var balance = BigDecimal.ZERO
        val unspentOutputs = mutableListOf<BitcoinUnspentOutput>()
        val recentTransactions = mutableListOf<BasicTransactionData>()
        var hasUnconfirmed: Boolean? = false

        this.forEach {
            balance += it.balance
            unspentOutputs.addAll(it.unspentOutputs)
            recentTransactions.addAll(it.recentTransactions)
            hasUnconfirmed = if (hasUnconfirmed == null || it.hasUnconfirmed == null) {
                null
            } else {
                hasUnconfirmed!! || it.hasUnconfirmed
            }
        }

        // merge same transaction on different addresses
        val transactionsByHash = mutableMapOf<String, List<BasicTransactionData>>()
        recentTransactions.forEach { transaction ->
            val sameHashTransactions = recentTransactions.filter { it.hash == transaction.hash }
            transactionsByHash[transaction.hash] = sameHashTransactions
        }
        val finalTransactions = transactionsByHash.map {
            BasicTransactionData(
                    balanceDif = it.value.sumOf { transaction -> transaction.balanceDif },
                    hash = it.value[0].hash,
                    date = it.value[0].date,
                    isConfirmed = it.value[0].isConfirmed
            )
        }
        return BitcoinAddressInfo(balance, unspentOutputs, finalTransactions, hasUnconfirmed)
    }

    private fun updateWallet(response: BitcoinAddressInfo) {
        Log.d(this::class.java.simpleName, "Balance is ${response.balance}")
        wallet.amounts[AmountType.Coin]?.value = response.balance
        transactionBuilder.unspentOutputs = response.unspentOutputs
        if (response.recentTransactions.isNotEmpty()) {
            updateRecentTransactionsBasic(response.recentTransactions)
        } else {
            when (response.hasUnconfirmed) {
                true -> wallet.addTransactionDummy()
                false -> wallet.recentTransactions.clear()
            }
        }
    }

    private fun updateError(error: Throwable?) {
        Log.e(this::class.java.simpleName, error?.message ?: "")
        if (error != null) throw error
    }

    override suspend fun send(
            transactionData: TransactionData, signer: TransactionSigner
    ): SimpleResult {
        when (val buildTransactionResult = transactionBuilder.buildToSign(transactionData)) {
            is Result.Failure -> return SimpleResult.Failure(buildTransactionResult.error)
            is Result.Success -> {
                val signerResult = signer.sign(
                        buildTransactionResult.data,
                        wallet.cardId, walletPublicKey = wallet.publicKey
                )
                return when (signerResult) {
                    is CompletionResult.Success -> {
                        val transactionToSend = transactionBuilder.buildToSend(
                                signerResult.data.reduce { acc, bytes -> acc + bytes }
                        )
                        val sendResult = networkProvider.sendTransaction(transactionToSend.toHexString())

                        if (sendResult is SimpleResult.Success) {
                            transactionData.hash = transactionBuilder.getTransactionHash().toHexString()
                            wallet.addOutgoingTransaction(transactionData)
                        }
                        sendResult
                    }
                    is CompletionResult.Failure -> SimpleResult.failure(signerResult.error)
                }
            }
        }
    }

    override suspend fun getFee(amount: Amount, destination: String): Result<List<Amount>> {
        try {
            when (val feeResult = networkProvider.getFee()) {
                is Result.Failure -> return feeResult
                is Result.Success -> {
                    val feeValue = BigDecimal.ONE.movePointLeft(blockchain.decimals())
                    amount.value = amount.value!! - feeValue
                    val sizeResult = transactionBuilder.getEstimateSize(
                            TransactionData(amount, Amount(amount, feeValue), wallet.address, destination)
                    )
                    return when (sizeResult) {
                        is Result.Failure -> sizeResult
                        is Result.Success -> {
                            val transactionSize = sizeResult.data.toBigDecimal()
                            val minFee = feeResult.data.minimalPerKb.calculateFee(transactionSize)
                            val normalFee = feeResult.data.normalPerKb.calculateFee(transactionSize)
                            val priorityFee = feeResult.data.priorityPerKb.calculateFee(transactionSize)
                            val fees = listOf(Amount(minFee, blockchain),
                                    Amount(normalFee, blockchain),
                                    Amount(priorityFee, blockchain)
                            )
                            Result.Success(fees)
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            return Result.Failure(exception)
        }
    }

    override suspend fun validateSignatureCount(signedHashes: Int): SimpleResult {
        return when (val result = networkProvider.getSignatureCount(wallet.address)) {
            is Result.Success -> if (result.data == signedHashes) {
                SimpleResult.Success
            } else {
                SimpleResult.Failure(BlockchainSdkError.SignatureCountNotMatched)
            }
            is Result.Failure -> SimpleResult.Failure(result.error)
        }
    }

    private fun BigDecimal.calculateFee(transactionSize: BigDecimal): BigDecimal {
        val feePerKb = maxOf(this, minimalFeePerKb)
        val bytesInKb = BigDecimal(1024)
        val calculatedFee = feePerKb.divide(bytesInKb).multiply(transactionSize)
                .setScale(8, BigDecimal.ROUND_DOWN)
        return maxOf(calculatedFee, minimalFee)
    }
}