package com.kroune.nineMensMorrisApp.data.remote.game

import com.kr8ne.mensMorris.move.Movement
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import com.kroune.nineMensMorrisApp.data.remote.Common.networkScope
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.utils.io.printStack
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

/**
 * Repository for interacting with server games
 */
class GameRepository @Inject constructor() : GameRepositoryI {
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
                        val serverMessage = (incoming.receive() as? Frame.Text)?.readText()
                        if (serverMessage != null) {
                            gameId = serverMessage
                            close(CloseReason(CloseReason.Codes.NORMAL, "ok"))
                            break
                        }
                    }
                }
                gameId!!.toLong()
            }.onFailure {
                println("error accessing ${"ws$SERVER_ADDRESS$USER_API/search-for-game"}")
                it.printStack()
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
            it.printStack()
        }
    }
}
