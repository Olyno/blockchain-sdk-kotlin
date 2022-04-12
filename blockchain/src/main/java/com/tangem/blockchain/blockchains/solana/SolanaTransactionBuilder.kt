package com.tangem.blockchain.blockchains.solana

import com.tangem.blockchain.blockchains.solana.solanaj.core.Transaction
import com.tangem.blockchain.common.TransactionData
import org.p2p.solanaj.core.PublicKey
import org.p2p.solanaj.programs.SystemProgram
import java.math.BigDecimal

/**
 * Created by Anton Zhilenkov on 26/01/2022.
 */
class SolanaTransactionBuilder(
    private val valueConverter: SolanaValueConverter
) {

    fun buildToSign(transactionData: TransactionData, recentBlockhash: String): Transaction {
        val from = PublicKey(transactionData.sourceAddress)
        val to = PublicKey(transactionData.destinationAddress)
        val lamports = valueConverter.toLamports(transactionData.amount.value ?: BigDecimal.ZERO)

        val solanaTx = Transaction(from)
        solanaTx.addInstruction(SystemProgram.transfer(from, to, lamports))
        solanaTx.setRecentBlockHash(recentBlockhash)
        return solanaTx
    }
}