package com.tangem.blockchain.blockchains.ethereum.network

import com.tangem.blockchain.common.Blockchain
import com.tangem.blockchain.common.Token
import com.tangem.blockchain.common.TransactionData
import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.SimpleResult
import com.tangem.blockchain.extensions.retryIO
import com.tangem.blockchain.network.API_INFURA
import com.tangem.blockchain.network.API_INFURA_TESTNET
import com.tangem.blockchain.network.API_RSK
import com.tangem.blockchain.network.blockchair.BlockchairEthProvider
import com.tangem.blockchain.network.blockcypher.BlockcypherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.math.RoundingMode


class EthereumNetworkManager(
        blockchain: Blockchain,
        infuraProjectId: String?,
        private val blockcypherProvider: BlockcypherProvider? = null,
        private val blockchairEthProvider: BlockchairEthProvider? = null
) : EthereumNetworkService {
    init {
        if (infuraProjectId == null &&
                (blockchain == Blockchain.Ethereum || blockchain == Blockchain.EthereumTestnet)
        ) {
            throw Exception("Infura project Id is required for Ethereum network manager")
        }
    }

    private val provider: EthereumProvider by lazy {
        val infuraApiVersionPath = "v3/"
        val ethereumApiUrl = when (blockchain) {
            Blockchain.Ethereum -> API_INFURA + infuraApiVersionPath + infuraProjectId
            Blockchain.EthereumTestnet -> API_INFURA_TESTNET + infuraApiVersionPath + infuraProjectId
            Blockchain.RSK -> API_RSK
            else -> throw Exception("${blockchain.fullName} blockchain is not supported by ${this::class.simpleName}")
        }
        EthereumProvider(ethereumApiUrl)
    }
    private val decimals = Blockchain.Ethereum.decimals()

    override suspend fun getInfo(address: String, tokens: Set<Token>): Result<EthereumInfoResponse> {
        return try {
            coroutineScope {
                val balanceResponseDeferred = retryIO { async { provider.getBalance(address) } }
                val txCountResponseDeferred = retryIO { async { provider.getTxCount(address) } }
                val pendingTxCountResponseDeferred =
                        retryIO { async { provider.getPendingTxCount(address) } }
                val transactionsResponseDeferred =
                        retryIO { async { blockchairEthProvider?.getTransactions(address, tokens) } }
                val tokenBalancesDeferred = tokens.map {
                    it to retryIO { async { provider.getTokenBalance(address, it.contractAddress) } }
                }.toMap()

                val balanceResponse = balanceResponseDeferred.await()
                val balance = balanceResponse.result?.parseAmount(decimals)
                        ?: throw balanceResponse.error?.toException()
                                ?: Exception("Unknown balance response format")

                val txCount = txCountResponseDeferred.await()
                        .result?.responseToBigInteger()?.toLong() ?: 0
                val pendingTxCount = pendingTxCountResponseDeferred.await()
                        .result?.responseToBigInteger()?.toLong() ?: 0

                val tokenBalanceResponses = tokenBalancesDeferred.mapValues { it.value.await() }
                val tokenBalances = tokenBalanceResponses.mapValues {
                    it.value.result?.parseAmount(it.key.decimals)
                            ?: throw it.value.error?.toException()
                                    ?: Exception("Unknown token balance response format")
                }

                val recentTransactions = when (val result = transactionsResponseDeferred.await()) {
                    is Result.Success -> result.data
                    else -> emptyList()
                }

                Result.Success(
                        EthereumInfoResponse(
                                coinBalance = balance,
                                tokenBalances = tokenBalances,
                                txCount = txCount,
                                pendingTxCount = pendingTxCount,
                                recentTransactions = recentTransactions
                        )
                )
            }
        } catch (error: Exception) {
            Result.Failure(error)
        }
    }

    override suspend fun sendTransaction(transaction: String): SimpleResult {
        return try {
            val response = retryIO { provider.sendTransaction(transaction) }
            if (response.error == null) {
                SimpleResult.Success
            } else {
                SimpleResult.Failure(response.error.toException())
            }
        } catch (error: Exception) {
            SimpleResult.Failure(error)
        }
    }

    override suspend fun getFee(to: String, from: String, data: String?, fallbackGasLimit: Long?): Result<EthereumFeeResponse> {
        return try {
            coroutineScope {
                val gasLimitDeferred = retryIO { async { provider.getGasLimit(to, from, data) } }
                val feeDeffered = retryIO { async { provider.getGasPrice() } }

                val gasLimitResponse = gasLimitDeferred.await()
                val gasLimit = gasLimitResponse.result?.responseToBigInteger()?.toLong()
                        ?: fallbackGasLimit // TODO: remove?
                        ?: throw gasLimitResponse.error?.toException()
                                ?: Exception("Unknown estimate gas response format")

                val feeResponse = feeDeffered.await()
                val fees = feeResponse.result?.parseFee(gasLimit)
                        ?: throw feeResponse.error?.toException()
                                ?: Exception("Unknown gas price response format")

                Result.Success(EthereumFeeResponse(fees, gasLimit))
            }
        } catch (error: Exception) {
            Result.Failure(error)
        }
    }

    override suspend fun getSignatureCount(address: String): Result<Int> {
        return blockcypherProvider?.getSignatureCount(address)
                ?: Result.Failure(Exception("No signature count provider found"))
    }

    private fun String.parseFee(gasLimit: Long): List<BigDecimal> {
        val gasPrice = this.responseToBigInteger().toBigDecimal()
        val minFee = gasPrice.multiply(gasLimit.toBigDecimal())
        val normalFee = minFee.multiply(BigDecimal(1.2)).setScale(0, RoundingMode.HALF_UP)
        val priorityFee = minFee.multiply(BigDecimal(1.5)).setScale(0, RoundingMode.HALF_UP)
        return listOf(
                minFee.movePointLeft(decimals),
                normalFee.movePointLeft(decimals),
                priorityFee.movePointLeft(decimals)
        )
    }

    private fun String.responseToBigInteger() =
            this.substring(2).toBigInteger(16)

    private fun String.parseAmount(decimals: Int) =
            this.responseToBigInteger().toBigDecimal().movePointLeft(decimals)

    private fun EthereumError.toException() =
            Exception("Code: ${this.code}, ${this.message}")

}

data class EthereumInfoResponse(
        val coinBalance: BigDecimal,
        val tokenBalances: Map<Token, BigDecimal>,
        val txCount: Long,
        val pendingTxCount: Long,
        val recentTransactions: List<TransactionData>?
)

data class EthereumFeeResponse(
        val fees: List<BigDecimal>,
        val gasLimit: Long
)