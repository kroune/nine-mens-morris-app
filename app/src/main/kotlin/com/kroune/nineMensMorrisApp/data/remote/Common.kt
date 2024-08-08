package com.kroune.nineMensMorrisApp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.isSuccess
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
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, response -> !response.status.isSuccess() }
            retryOnServerErrors(maxRetries = 5)
            retryOnException(maxRetries = 5, retryOnTimeout = true)
            exponentialDelay()
        }
        install(HttpTimeout) {
            this.requestTimeoutMillis = 5 * 1000
            this.socketTimeoutMillis = 60 * 60 * 1000
            this.connectTimeoutMillis = 5 * 1000
        }
        install(WebSockets) {
            pingInterval = 3000L
        }
    }
}
