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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Repository for interacting with server games
 */
class GameRepositoryImpl @Inject constructor() : GameRepositoryI {
    override suspend fun startSearchingGame(jwtToken: String): Result<Long> {
        // we use this to make sure this function isn't executed in parallel
        if (searchingForGameJob?.isCompleted == false) {
            return searchingForGameJob!!.await()
        }
        searchingForGameJob = CoroutineScope(networkScope).async {
            runCatching {
                var gameId: String? = null
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
                            println(serverMessage)
                            val serverData =
                                Json.decodeFromString<Pair<Boolean, Long>>(serverMessage)
                            if (!serverData.first) {
                                gameId = serverData.second.toString()
                                println("new game id - $gameId")
                                close()
                                return@webSocket
                            }
                            println("expectedWaitingTime - ${serverData.second}")
                        }
                    }
                }
                gameId!!.toLong()
            }.onFailure {
                println("error accessing ${"ws$SERVER_ADDRESS$USER_API/search-for-game"}")
                it.printStackTrace()
            }
        }
        return searchingForGameJob!!.await()
    }

    /**
     * job created when searching for a game
     */
    private var searchingForGameJob: Deferred<Result<Long>>? = null

    override suspend fun isPlaying(jwtToken: String): Result<Long?> {
        return runCatching {
            val result = network.get("http$SERVER_ADDRESS$USER_API/is-playing") {
                method = HttpMethod.Get
                url {
                    parameters["jwtToken"] = jwtToken
                }
            }
            result.bodyAsText().toLongOrNull()
        }.onFailure {
            println("error accessing ${"http$SERVER_ADDRESS$USER_API/is-playing"}")
            it.printStackTrace()
        }
    }
}
