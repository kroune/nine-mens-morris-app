package com.kroune.nineMensMorrisApp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.Dispatchers

/**
 * Common remote data
 */
object Common {
    /**
     * The network scope for asynchronous operations.
     */
    val networkScope = Dispatchers.IO

    /**
     * The network client for making HTTP requests.
     */
    val network = HttpClient(OkHttp) {
        install(HttpRequestRetry) {
            // retry on timeout
            retryIf(maxRetries = 5) { request, response ->
                response.status.value == 408
            }
            retryOnExceptionIf(maxRetries = 5) { request, exception ->
                exception is HttpRequestTimeoutException
            }
            exponentialDelay()
        }
        install(HttpTimeout) {
            this.requestTimeoutMillis = 10 * 1000
            this.socketTimeoutMillis = 30 * 60 * 1000
            this.connectTimeoutMillis = 10 * 1000
        }
        install(WebSockets) {
            pingInterval = 3000L
        }
    }
}

/**
 * exception when receiving info from the server
 * @param status status of the call
 * @param reason body of the response server returned
 */
class ClientNetworkException(
    val status: Int,
    val reason: String
) : Exception()

/**
 * result of the auth
 */
sealed class AuthResults {
    /**
     * Everything worked
     */
    class Success : AuthResults()

    /**
     * Indicates failure
     * @param stringId can be used to get a matching exception description from resources
     */
    class Failure(val stringId: Int) : AuthResults()
}
