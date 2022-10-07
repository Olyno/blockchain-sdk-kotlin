package com.tangem.blockchain.blockchains.solana.solanaj.rpc

import org.p2p.solanaj.rpc.Cluster
import org.p2p.solanaj.rpc.RpcClient

/**
 * Created by Anton Zhilenkov on 26/01/2022.
 */
class RpcClient(val host: String) : RpcClient(host, null) {

    override fun createRpcApi(): RpcApi = RpcApi(this)

    override fun getApi(): RpcApi = super.getApi() as RpcApi

    companion object {
        fun devNet(): com.tangem.blockchain.blockchains.solana.solanaj.rpc.RpcClient =
            com.tangem.blockchain.blockchains.solana.solanaj.rpc.RpcClient(Cluster.DEVNET.endpoint)
        fun testNet(): com.tangem.blockchain.blockchains.solana.solanaj.rpc.RpcClient =
            com.tangem.blockchain.blockchains.solana.solanaj.rpc.RpcClient(Cluster.TESTNET.endpoint)
    }
}