package com.tangem.blockchain.blockchains.tezos.network

import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.SimpleResult
import com.tangem.blockchain.network.MultiProvider

class TezosNetworkManager(providers: List<TezosNetworkService>) :
        MultiProvider<TezosNetworkService>(providers),
        TezosNetworkService {

    override suspend fun getInfo(address: String): Result<TezosInfoResponse> {
        val result = provider.getInfo(address)
        return if (result.needsRetry()) getInfo(address) else result
    }

    override suspend fun isPublicKeyRevealed(address: String): Result<Boolean> {
        val result = provider.isPublicKeyRevealed(address)
        return if (result.needsRetry()) isPublicKeyRevealed(address) else result
    }

    override suspend fun getHeader(): Result<TezosHeader> {
        val result = provider.getHeader()
        return if (result.needsRetry()) getHeader() else result
    }

    override suspend fun forgeContents(
            headerHash: String,
            contents: List<TezosOperationContent>
    ): Result<String> {
        val result = provider.forgeContents(headerHash, contents)
        return if (result.needsRetry()) forgeContents(headerHash, contents) else result
    }

    override suspend fun checkTransaction(
            header: TezosHeader,
            contents: List<TezosOperationContent>,
            signature: ByteArray
    ): SimpleResult {
        val result = provider.checkTransaction(header, contents, signature)
        return if (result.needsRetry()) checkTransaction(header, contents, signature) else result
    }

    override suspend fun sendTransaction(transaction: String): SimpleResult {
        val result = provider.sendTransaction(transaction)
        return if (result.needsRetry()) sendTransaction(transaction) else result
    }
}