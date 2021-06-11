package com.tangem.blockchain.blockchains.cardano.network

import com.tangem.blockchain.blockchains.cardano.CardanoUnspentOutput
import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.SimpleResult

interface CardanoNetworkProvider {
    val host: String
    suspend fun getInfo(addresses: Set<String>): Result<CardanoAddressResponse>
    suspend fun sendTransaction(transaction: ByteArray): SimpleResult
}

data class CardanoAddressResponse(
        val balance: Long,
        val unspentOutputs: List<CardanoUnspentOutput>,
        val recentTransactionsHashes: List<String>
)