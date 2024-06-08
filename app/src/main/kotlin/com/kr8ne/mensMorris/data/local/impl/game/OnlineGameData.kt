package com.kr8ne.mensMorris.data.local.impl.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.kr8ne.mensMorris.Position
import com.kr8ne.mensMorris.WELCOME_SCREEN
import com.kr8ne.mensMorris.common.SERVER_ADDRESS
import com.kr8ne.mensMorris.common.USER_API
import com.kr8ne.mensMorris.data.local.interfaces.DataI
import com.kr8ne.mensMorris.data.local.interfaces.GameBoardInterface
import com.kr8ne.mensMorris.data.remote.Auth.jwtToken
import com.kr8ne.mensMorris.data.remote.Game
import com.kr8ne.mensMorris.data.remote.network
import com.kr8ne.mensMorris.data.remote.networkScope
import com.kr8ne.mensMorris.gameStartPosition
import com.kr8ne.mensMorris.move.Movement
import com.kr8ne.mensMorris.ui.impl.game.GameBoardScreen
import com.kroune.NetworkResponse
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.utils.io.printStack
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

/**
 * data for online game screen
 */
class OnlineGameData(
    private val gameId: Long,
    val navController: NavHostController?
) : GameBoardInterface, DataI() {

    var isGreen: MutableState<Boolean?> = mutableStateOf(null)
    override val gameBoard = GameBoardScreen(
        pos = gameStartPosition,
        onClick = { index -> this.response(index) },
        navController = null
    )

    private fun GameBoardData.response(index: Int) {
        // check if we can make this move
        println("isGreen - $isGreen; pieceToMove - ${gameBoard.pos.pieceToMove}")
        if (isGreen.value == gameBoard.pos.pieceToMove) {
            gameBoard.viewModel.data.getMovement(index)?.let {
                println("added move")
                Game.movesQueue.add(it)
            }
            handleClick(index)
            handleHighLighting()
        }
    }

    private val someInt = Random.nextInt()
    override fun invokeBackend() {
        /**
         * if you even see this debug message in logs more than once you should know
         * that you are FUCKED
         * I have tried fixing this complete piece of shit for MANY hours
         * I have made a "fix"
         * but it is the shittiest piece of code I have written in more than 30 days
         * I don't know what causes this issue
         * I don't want to debug it
         * I don't want to touch it
         * YOU don't want to touch it, leave it as it is, don't waste your life
         * I hope it will never break or at least I won't be the one responsible for it
         * FUCK THIS SHIT
         */
        println("invoke as $someInt")
        println(gameId.toString())
        CoroutineScope(networkScope).launch {
            runCatching {
                val jwtTokenState = jwtToken
                require(jwtTokenState != null)
                network.webSocket(
                    "ws${SERVER_ADDRESS}${USER_API}/game",
                    request = {
                        url {
                            parameters["jwtToken"] = jwtTokenState
                            parameters["gameId"] = gameId.toString()
                        }
                    }) {
                    try {
                        val isGreenData = (incoming.receive() as Frame.Text).readText()
                        println("isGreen new value - $isGreenData")
                        val isGreenText = Json.decodeFromString<NetworkResponse>(isGreenData)
                        isGreen.value = isGreenText.message.toBoolean()
                        println("check - ${isGreen}")

                        val positionServerData = (incoming.receive() as Frame.Text).readText()
                        println("position new value - $positionServerData")
                        val positionString =
                            Json.decodeFromString<NetworkResponse>(positionServerData).message!!
                        val newPosition = Json.decodeFromString<Position>(positionString)
                        gameBoard.pos = newPosition
                        while (true) {
                            // send all our moves one by one
                            val moveToSend = Game.movesQueue.poll()
                            if (moveToSend != null) {
                                val string = Json.encodeToString<Movement>(moveToSend)
                                // post our move
                                println(string)
                                send(string)
                            }
                            try {
                                // receive the server's data
                                val serverMessage =
                                    incoming.tryReceive().getOrNull() as? Frame.Text ?: continue
                                println(serverMessage)
                                val serverResponse =
                                    Json.decodeFromString<NetworkResponse>(serverMessage.readText())
                                when (serverResponse.code) {
                                    410 -> {
                                        // game ended
                                        println("game ended")
                                        navController?.navigate(WELCOME_SCREEN)
                                    }

                                    200 -> {
                                        val movement =
                                            Json.decodeFromString<Movement>(serverResponse.message!!)
                                        // apply move
                                        println("new move")
                                        gameBoard.viewModel.data.processMove(movement)
                                    }

                                    else -> {
                                        // we reload our game
                                        navController?.navigate("ONLINE_GAME_SCREEN/$gameId")
                                        println("smth went wrong, reloading")
                                    }
                                }
                            } catch (e: Exception) {
                                println("error when receiving move $someInt")
                                e.printStackTrace()
                            }
                        }
                    } catch (e: Exception) {
                        println("crash")
                        e.printStackTrace()
                    }
                }
            }.onFailure {
                println("error accessing ${"${SERVER_ADDRESS}${USER_API}/game; gameId = $gameId"} $someInt")
                it.printStack()
            }
        }
    }
}
