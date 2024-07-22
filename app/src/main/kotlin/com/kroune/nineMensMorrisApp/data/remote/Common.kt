package com.kroune.nineMensMorrisApp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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
        install(HttpTimeout) {
            this.requestTimeoutMillis = 5 * 1000
            this.socketTimeoutMillis = 60 * 60 * 1000
            this.connectTimeoutMillis = 5 * 1000
        }
        install(WebSockets)
    }
}
