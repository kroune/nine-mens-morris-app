package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kr8ne.mensMorris.Position
import com.kr8ne.mensMorris.gameStartPosition
import com.kr8ne.mensMorris.move.Movement
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.local.impl.game.GameBoardData
import com.kroune.nineMensMorrisApp.data.local.impl.game.OnlineGameData
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.ui.impl.game.GameBoardScreen
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.utils.io.printStack
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    override val data = OnlineGameData()

    private suspend fun DefaultClientWebSocketSession.get(): String {
        return (incoming.receive() as Frame.Text).readText()
    }

    private var gameJob: Job? = null

    private var session: DefaultClientWebSocketSession? = null

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
                    isGreen = get().toBooleanStrict()
                    gameBoard.pos.value = Json.decodeFromString<Position>(get())
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
                        if (gameBoard.pos.value.pieceToMove == isGreen) {
                            gameBoard.viewModel.handleHighLighting()
                        } else {
                            gameBoard.viewModel.data.moveHints.value = listOf()
                        }
                    }
                }
            } catch (e: Exception) {
                println("error accessing playing game")
                e.printStack()
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
    private var isGreen: Boolean? = null
        set(value) {
            _uiState.value = _uiState.value.copy(isGreen = value)
            field = value
        }

    /**
     * our game board
     */
    private val gameBoard = GameBoardScreen(
        pos = gameStartPosition,
        onClick = { index -> this.response(index) },
        navController = null
    )

    private fun GameBoardData.response(index: Int) {
        // check if we can make this move
        if (isGreen == gameBoard.pos.value.pieceToMove) {
            gameBoard.viewModel.data.getMovement(index)?.let {
                viewModelScope.launch {
                    val string = Json.encodeToString<Movement>(it)
                    // post our move
                    session!!.send(string)
                }
            }
            handleClick(index)
            if (gameBoard.viewModel.data.pos.value.pieceToMove == isGreen) {
                handleHighLighting()
            } else {
                // we can't make any move if it isn't our move
                gameBoard.viewModel.data.moveHints.value = listOf()
            }
        }
    }

    private val _uiState =
        MutableStateFlow(OnlineGameScreenUiState(gameBoard, isGreen))

    /**
     * exposed ui state
     */
    val uiState: StateFlow<OnlineGameScreenUiState>
        get() = _uiState

    init {
        connectToTheGameAndPlay()
    }
}

/**
 * ui state
 */
data class OnlineGameScreenUiState(
    /**
     * game board
     */
    val gameBoard: GameBoardScreen,
    /**
     * is green status
     */
    val isGreen: Boolean?
)
