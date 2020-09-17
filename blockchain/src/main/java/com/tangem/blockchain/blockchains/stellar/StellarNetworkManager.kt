package com.tangem.blockchain.blockchains.stellar

import com.tangem.blockchain.blockchains.stellar.StellarWalletManager.Companion.STROOPS_IN_XLM
import com.tangem.blockchain.common.*
import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.SimpleResult
import com.tangem.blockchain.network.API_STELLAR
import com.tangem.blockchain.network.API_STELLAR_TESTNET
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.stellar.sdk.*
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.sdk.requests.RequestBuilder
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import java.io.IOException
import java.math.BigDecimal
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class StellarNetworkManager(isTestNet: Boolean = false) {
    private val blockchain = Blockchain.Stellar
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val recordsLimitCap = 200

    val network: Network = if (isTestNet) Network.TESTNET else Network.PUBLIC
    private val stellarServer by lazy {
        Server(if (isTestNet) API_STELLAR_TESTNET else API_STELLAR)
    }

    suspend fun sendTransaction(transaction: String): SimpleResult {
        return try {
            val response = stellarServer.submitTransaction(Transaction.fromEnvelopeXdr(transaction, network))
            if (response.isSuccess) {
                SimpleResult.Success
            } else {
                val trResult: String? = response.extras?.resultCodes?.transactionResultCode +
                        (response.extras?.resultCodes?.operationsResultCodes?.getOrNull(0) ?: "")
                SimpleResult.Failure(Exception(trResult ?: "transaction failed"))
            }
        } catch (error: Exception) {
            SimpleResult.Failure(error)
        }
    }

    suspend fun checkIsAccountCreated(address: String): Boolean { // TODO: return result?
        try {
            stellarServer.accounts().account(address)
            return true
        } catch (errorResponse: ErrorResponse) {
            if (errorResponse.code == 404) return false
            return false
        } catch (exception: IOException) {
            return false
        }
    }

    suspend fun getInfo(accountId: String, assetCode: String? = null): Result<StellarResponse> {
        return try {
            coroutineScope {
                val accountResponseDeferred = async(Dispatchers.IO) { stellarServer.accounts().account(accountId) }
                val ledgerResponseDeferred = async(Dispatchers.IO) {
                    val latestLedger: Int = stellarServer.root().historyLatestLedger
                    stellarServer.ledgers().ledger(latestLedger.toLong())
                }
                val paymentsResponseDeferred = async(Dispatchers.IO) {
                    stellarServer.payments().forAccount(accountId)
                            .order(RequestBuilder.Order.DESC)
                            .execute()
                }

                val accountResponse = accountResponseDeferred.await()
                val balance = accountResponse.balances
                        .find { it.assetType == "native" }
                        ?.balance?.toBigDecimal()
                        ?: return@coroutineScope Result.Failure(Exception("Stellar Balance not found"))
                val assetBalance = if (assetCode == null) {
                    null
                } else {
                    accountResponse.balances
                            .find { it.assetType != "native" && it.assetIssuer == assetCode }
                            ?.balance?.toBigDecimal()
                            ?: return@coroutineScope Result.Failure(Exception("Stellar Balance not found"))
                }
                val sequence = accountResponse.sequenceNumber

                val ledgerResponse = ledgerResponseDeferred.await()
                val baseFee = ledgerResponse.baseFeeInStroops.toBigDecimal().divide(STROOPS_IN_XLM)
                val baseReserve = ledgerResponse.baseReserveInStroops.toBigDecimal().divide(STROOPS_IN_XLM)

                val recentTransactions =
                        paymentsResponseDeferred.await().records.mapNotNull { it.toTransactionData() }

                Result.Success(StellarResponse(
                        baseFee,
                        baseReserve,
                        assetBalance,
                        balance,
                        sequence,
                        recentTransactions
                ))
            }
        } catch (error: Exception) {
            Result.Failure(error)
        }
    }

    suspend fun getSignatureCount(accountId: String): Result<Int> {
        return try {
            var operationsPage = stellarServer.operations().forAccount(accountId)
                    .limit(recordsLimitCap)
                    .includeFailed(true)
                    .execute()
            val operations = operationsPage.records

            while (operationsPage.records.size == recordsLimitCap) {
                try {
                    operationsPage = operationsPage.getNextPage(stellarServer.httpClient)
                    operations.addAll(operationsPage.records)
                } catch (e: URISyntaxException) {
                    break
                }
            }
            Result.Success(operations.filter { it.sourceAccount == accountId }.size)

        } catch (error: Exception) {
            Result.Failure(error)
        }
    }

    private fun OperationResponse.toTransactionData(): TransactionData? {
        return when (this) {
            is PaymentOperationResponse -> this.toTransactionData()
            is CreateAccountOperationResponse -> this.toTransactionData()
            else -> null
        }
    }

    private fun PaymentOperationResponse.toTransactionData(): TransactionData {
        val asset = asset
        val amount = when (asset) {
            is AssetTypeNative -> Amount(amount.toBigDecimal(), blockchain)
            is AssetTypeCreditAlphaNum -> Amount(
                    currencySymbol = asset.code,
                    value = amount.toBigDecimal(),
                    address = asset.issuer,
                    decimals = blockchain.decimals(),
                    type = AmountType.Token
            )
            else -> throw Exception("Unknown asset type")
        }
        return TransactionData(
                amount = amount,
                fee = null,
                sourceAddress = from,
                destinationAddress = to,
                contractAddress = amount.address,
                status = TransactionStatus.Confirmed,
                date = Calendar.getInstance().apply { time = dateFormat.parse(createdAt)!! },
                hash = transactionHash
        )
    }

    private fun CreateAccountOperationResponse.toTransactionData(): TransactionData {
        return TransactionData(
                amount = Amount(startingBalance.toBigDecimal(), blockchain),
                fee = null,
                sourceAddress = funder,
                destinationAddress = account,
                status = TransactionStatus.Confirmed,
                date = Calendar.getInstance().apply { time = dateFormat.parse(createdAt)!! },
                hash = transactionHash
        )
    }
}

data class StellarResponse(
        val baseFee: BigDecimal,
        val baseReserve: BigDecimal,
        val assetBalance: BigDecimal?,
        val balance: BigDecimal,
        val sequence: Long,
        val recentTransactions: List<TransactionData>
)