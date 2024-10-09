package com.kroune.nineMensMorrisApp.data.remote.game

import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import com.kroune.nineMensMorrisApp.data.remote.Common.networkScope
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Repository for interacting with server games
 */
class GameRepositoryImpl @Inject constructor() : GameRepositoryI {
    override suspend fun startSearchingGame(jwtToken: String): Channel<Pair<Boolean, Long>> {
        val channel = Channel<Pair<Boolean, Long>>()
        // we use this to make sure this function isn't executed in parallel
        CoroutineScope(networkScope).async {
            runCatching {
                network.webSocket("ws$SERVER_ADDRESS$USER_API/search-for-game", request = {
                    url {
                        parameters["jwtToken"] = jwtToken
                    }
                }) {
                    while (true) {
                        incoming.consumeEach { frame ->
                            if (frame !is Frame.Text) {
                                return@consumeEach
                            }
                            val serverMessage = frame.readText()
                            val serverData =
                                Json.decodeFromString<Pair<Boolean, Long>>(serverMessage)
                            channel.send(serverData)
                            // this indicates that we got game id, so channel should be closed
                            if (!serverData.first) {
                                channel.close()
                                close()
                                return@webSocket
                            }
                        }
                    }
                }
            }.onFailure {
                println("error accessing ${"ws$SERVER_ADDRESS$USER_API/search-for-game"}")
                it.printStackTrace()
            }
        }
        return channel
    }

    override suspend fun isPlaying(jwtToken: String): Result<Long?> {
        return runCatching {
            val result = network.get("http$SERVER_ADDRESS$USER_API/is-playing") {
                method = HttpMethod.Get
                url {
                    parameters["jwtToken"] = jwtToken
                }
            }
            Json.decodeFromString<Long?>(result.bodyAsText())
        }.onFailure {
            println("error accessing ${"http$SERVER_ADDRESS$USER_API/is-playing"}")
            it.printStackTrace()
        }
    }
}
