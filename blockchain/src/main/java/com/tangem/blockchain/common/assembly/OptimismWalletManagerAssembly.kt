package com.tangem.blockchain.common.assembly

import com.tangem.blockchain.blockchains.ethereum.EthereumTransactionBuilder
import com.tangem.blockchain.blockchains.ethereum.getEthereumJsonRpcProviders
import com.tangem.blockchain.blockchains.ethereum.network.EthereumNetworkService
import com.tangem.blockchain.blockchains.optimism.OptimismWalletManager

object OptimismWalletManagerAssembly : WalletManagerAssembly<OptimismWalletManager>() {

    override fun make(input: WalletManagerAssemblyInput): OptimismWalletManager {
        with(input.wallet) {
            return OptimismWalletManager(
                wallet = this,
                transactionBuilder = EthereumTransactionBuilder(
                    walletPublicKey = publicKey.blockchainKey,
                    blockchain = blockchain
                ),
                networkProvider = EthereumNetworkService(
                    jsonRpcProviders = blockchain.getEthereumJsonRpcProviders(input.config),
                ),
                presetTokens = input.presetTokens
            )
        }
    }

}