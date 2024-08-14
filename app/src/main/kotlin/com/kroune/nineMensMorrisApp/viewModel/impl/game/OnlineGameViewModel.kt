package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GameBoardUseCase
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.gameStartPosition
import com.kroune.nineMensMorrisLib.move.Movement
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * welcome model
 * called when app is launched
 */
class OnlineGameViewModel @AssistedInject constructor(
    private val accountInfoRepositoryI: AccountInfoRepositoryI,
    @Assisted
    private val gameId: Long
) : ViewModelI() {
    /**
     * assisted factory for [OnlineGameViewModel]
     */
    @AssistedFactory
    interface AssistedVMFactory {
        /**
         * creates [OnlineGameViewModel] using id
         */
        fun create(gameId: Long): OnlineGameViewModel
    }

    companion object {
        /**
         * provides factory
         */
        fun provideFactory(
            assistedVMFactory: AssistedVMFactory,
            id: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedVMFactory.create(id) as T
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.get(): String {
        val text = (incoming.receive() as Frame.Text).readText()
        return text
    }

    private var gameJob: Job? = null

    /**
     * our web socket session
     */
    private var session: DefaultClientWebSocketSession? = null

    /**
     * id of the enemy
     */
    var enemyId: MutableState<Long?> = mutableStateOf(null)

    /**
     * tells if the game has ended
     */
    val gameEnded = mutableStateOf(false)

    private var gameReconnectCoroutine: Job? = null

    private fun connectToTheGameAndPlay() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            try {
                val jwtTokenState =
                    accountInfoRepositoryI.jwtTokenState.value ?: error("jwt token cannot be null")
                network.webSocket("ws$SERVER_ADDRESS$USER_API/game",
                    request = {
                        url {
                            parameters["jwtToken"] = jwtTokenState
                            parameters["gameId"] = gameId.toString()
                        }
                    }) {
                    session = this
                    isGreen.value = get().toBooleanStrict()
                    gameBoard.pos.value = Json.decodeFromString<Position>(get())
                    enemyId.value = get().toLong()
                    while (true) {
                        // receive the server's data
                        val serverMessage = Json.decodeFromString<Movement>(get())
                        // this is a special way to encode game end
                        if (serverMessage.startIndex == null && serverMessage.endIndex == null) {
                            gameEnded.value = true
                            close(CloseReason(200, "game ended"))
                            break
                        }
                        gameBoard.pos.value = serverMessage.producePosition(gameBoard.pos.value)
                        if (gameBoard.pos.value.pieceToMove == isGreen.value) {
                            gameBoard.handleHighLighting()
                        } else {
                            gameBoard.moveHints.clear()
                        }
                    }
                }
            } catch (e: Exception) {
                println("error accessing playing game")
                e.printStackTrace()
                gameReconnectCoroutine?.cancel()
                gameReconnectCoroutine = viewModelScope.launch {
                    delay(2000)
                    // we try to relaunch this shit
                    connectToTheGameAndPlay()
                }
            }
        }
    }


    /**
     * tells if user is green or blue
     */
    var isGreen: MutableState<Boolean?> = mutableStateOf(null)

    /**
     * our game board
     */
    val gameBoard = GameBoardViewModel(
        pos = gameStartPosition,
        onClick = { index -> this.response(index) },
        onGameEnd = {
            println("Game ended")
        }
    )

    /**
     * sends information that we gave up
     */
    fun giveUp() {
        CoroutineScope(Dispatchers.IO).launch {
            val string = Json.encodeToString<Movement>(Movement(null, null))
            println("user gave up")
            session!!.send(string)
        }
    }

    private fun GameBoardUseCase.response(index: Int) {
        // check if we can make this move
        if (isGreen.value == gameBoard.pos.value.pieceToMove) {
            val move = gameBoard.getMovement(index)
            handleClick(index)
            if (gameBoard.pos.value.pieceToMove == isGreen.value) {
                handleHighLighting()
            } else {
                // we can't make any move if it isn't our move
                gameBoard.moveHints.clear()
            }
            move?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    val string = Json.encodeToString<Movement>(it)
                    // post our move
                    session!!.send(string)
                }
            }
        }
    }

    init {
        connectToTheGameAndPlay()
    }
}
