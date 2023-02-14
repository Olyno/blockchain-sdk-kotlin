package com.tangem.blockchain.blockchains.solana

import com.tangem.blockchain.blockchains.solana.solanaj.rpc.RpcClient
import com.tangem.blockchain.common.BlockchainSdkConfig
import com.tangem.blockchain.common.GetBlockCredentials
import com.tangem.blockchain.common.NowNodeCredentials
import com.tangem.blockchain.common.QuickNodeCredentials
import okhttp3.Interceptor
import okhttp3.Response
import org.p2p.solanaj.rpc.Cluster

/**
 * Created by Anton Zhilenkov on 06.02.2023.
 */
class SolanaRpcClientBuilder {

    fun build(isTestnet: Boolean, config: BlockchainSdkConfig): List<RpcClient> {
        return if (isTestnet) {
            listOf(devNet())
        } else {
            listOfNotNull(
                config.nowNodeCredentials?.let { nowNode(it) },
                config.quickNodeSolanaCredentials?.let { quickNode(it) },
                mainNet(),
            )
        }
    }

    private fun mainNet(): RpcClient = RpcClient(Cluster.MAINNET.endpoint)

    private fun devNet(): RpcClient = RpcClient(Cluster.DEVNET.endpoint)

    private fun testNet(): RpcClient = RpcClient(Cluster.TESTNET.endpoint)

    private fun quickNode(cred: QuickNodeCredentials): RpcClient {
        val host = "https://${cred.subdomain}.solana-mainnet.discover.quiknode.pro/${cred.apiKey}"
        return RpcClient(host)
    }

    private fun nowNode(cred: NowNodeCredentials): RpcClient {
        return RpcClient(
            host = "https://sol.nownodes.io",
            httpInterceptors = createInterceptor(NowNodeCredentials.headerApiKey, cred.apiKey),
        )
    }

    // contains old data about 7 hours
    private fun ankr(): RpcClient {
        return RpcClient("https://rpc.ankr.com/solana")
    }

    // unstable
    private fun getBlock(cred: GetBlockCredentials): RpcClient {
        return RpcClient(
            host = "https://sol.getblock.io/mainnet",
            httpInterceptors = createInterceptor(GetBlockCredentials.headerApiKey, cred.apiKey),
        )
    }

    // zero uptime
    private fun projectserum(): RpcClient {
        return RpcClient("https://solana-api.projectserum.com")
    }

    private fun createInterceptor(key: String, value: String): List<Interceptor> {
        return listOf(AddHeaderInterceptor(mapOf(key to value)))
    }
}

private class AddHeaderInterceptor(
    private val headers: Map<String, String>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            headers.forEach {
                addHeader(it.key, it.value)
            }
        }.build()

        return chain.proceed(request)
    }
}