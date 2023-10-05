package com.tangem.blockchain.blockchains.near.network.api

import com.tangem.blockchain.common.JsonRPCRequest
import com.tangem.crypto.encodeToBase58String
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * NearApi provides access to posting requests to jsonRPC endpoints.
 * @see <a href="https://docs.near.org/api/rpc/introduction">NEAR RPC API</a>
 *
 * @author Anton Zhilenkov on 31.07.2023.
 */
interface NearApi {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/")
    suspend fun sendJsonRpc(@Body body: JsonRPCRequest): ResponseBody
}

internal sealed interface NearMethod {

    fun asRequestBody(): JsonRPCRequest

    object ProtocolConfig : NearMethod {
        override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
            method = "EXPERIMENTAL_protocol_config",
            params = mapOf(
                "finality" to "final",
            ),
        )
    }

    object NetworkStatus : NearMethod {
        override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
            method = "status",
            params = arrayOf<Any>(),
        )
    }

    sealed class AccessKey : NearMethod {

        class View(private val accountId: String, private val publicKeyEncodedToBase58: String) : AccessKey() {
            override fun asRequestBody(): JsonRPCRequest{
                return JsonRPCRequest(
                    method = "query",
                    params = mapOf(
                        "request_type" to "view_access_key",
                        "finality" to "final",
                        "account_id" to accountId,
                        "public_key" to "ed25519:${publicKeyEncodedToBase58}"
                    ),
                )
            }
        }
    }

    sealed class Account : NearMethod {

        class View(private val accountId: String) : Account() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "query",
                params = mapOf(
                    "request_type" to "view_account",
                    "finality" to "final",
                    "account_id" to accountId,
                ),
            )
        }
    }

    sealed class GasPrice : NearMethod {

        class BlockHeight(val blockHeight: Long) : GasPrice() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "gas_price",
                params = arrayOf(blockHeight),
            )
        }

        class BlockHash(val blockHash: String) : GasPrice() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "gas_price",
                params = arrayOf(blockHash),
            )
        }

        object Null : GasPrice() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "gas_price",
                params = arrayOfNulls<Any>(1),
            )
        }
    }

    sealed class Transaction : NearMethod {

        class SendTxAsync(val signedTxBase64: String) : Transaction() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "broadcast_tx_commit",// поменять на commit
                params = arrayOf(signedTxBase64),
            )
        }

        class Status(val txHash: String, val senderAccountId: String) : Transaction() {
            override fun asRequestBody(): JsonRPCRequest = JsonRPCRequest(
                method = "tx",
                params = arrayOf(txHash, senderAccountId),
            )
        }
    }
}