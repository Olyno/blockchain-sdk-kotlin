package com.tangem.blockchain.common.assembly

import com.tangem.blockchain.blockchains.cosmos.CosmosWalletManager
import com.tangem.blockchain.blockchains.cosmos.network.CosmosChain
import com.tangem.blockchain.blockchains.cosmos.network.CosmosRestProvider
import com.tangem.blockchain.extensions.letNotBlank

object TerraV2WalletManagerAssembly : WalletManagerAssembly<CosmosWalletManager>() {

    override fun make(input: WalletManagerAssemblyInput): CosmosWalletManager {
        val providers = buildList {
            input.config.getBlockCredentials?.apiKey.letNotBlank { add("https://luna.getblock.io/$it/mainnet/") }
            add("https://phoenix-lcd.terra.dev/")
        }.map(::CosmosRestProvider)

        return CosmosWalletManager(
            wallet = input.wallet,
            networkProviders = providers,
            cosmosChain = CosmosChain.TerraV2
        )
    }

}