package com.tangem.blockchain.network.electrum

import com.tangem.blockchain.extensions.Result
import com.tangem.blockchain.extensions.map
import org.joda.time.DateTime

internal suspend fun ElectrumApiService.getLatency(): Result<Long> {
    val now = DateTime.now().millis
    val res = getBlockTip()

    return res.map {
        DateTime.now().millis - now
    }
}
