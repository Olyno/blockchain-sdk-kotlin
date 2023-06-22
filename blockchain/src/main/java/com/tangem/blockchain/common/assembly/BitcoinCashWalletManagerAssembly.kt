package com.tangem.blockchain.common.assembly

import com.tangem.blockchain.blockchains.bitcoin.getBitcoinNetworkProviders
import com.tangem.blockchain.blockchains.bitcoin.network.BitcoinNetworkService
import com.tangem.blockchain.blockchains.bitcoincash.BitcoinCashTransactionBuilder
import com.tangem.blockchain.blockchains.bitcoincash.BitcoinCashWalletManager

object BitcoinCashWalletManagerAssembly : WalletManagerAssembly<BitcoinCashWalletManager>() {

    override fun make(input: WalletManagerAssemblyInput): BitcoinCashWalletManager  {
        with(input.wallet) {
            return BitcoinCashWalletManager(
                wallet = this,
                transactionBuilder = BitcoinCashTransactionBuilder(
                    walletPublicKey = publicKey.blockchainKey,
                    blockchain = blockchain
                ),
                networkProvider = BitcoinNetworkService(
                    providers = blockchain.getBitcoinNetworkProviders(blockchain, input.config)
                )
            )
        }
    }


}