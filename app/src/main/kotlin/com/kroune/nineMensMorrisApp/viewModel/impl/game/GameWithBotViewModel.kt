package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.lifecycle.viewModelScope
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GameBoardUseCase
import com.kroune.nineMensMorrisLib.GameState
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.gameStartPosition
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * game with bot model
 */
class GameWithBotViewModel(
    onGameEnd: GameWithBotViewModel.(Position) -> Unit
) : ViewModelI() {

    /**
     * our game board
     */
    val gameBoard = GameBoardViewModel(
        pos = gameStartPosition,
        onClick = { index ->
            this.response(index)
        },
        onUndo = {
            this@GameWithBotViewModel.onUndo()
        },
        onGameEnd = {
            onGameEnd(it)
        }
    )

    val movesHistory = gameBoard.movesHistory

    /**
     * performs needed actions after click
     * @param index index of the clicked element
     */
    private fun GameBoardUseCase.response(index: Int) {
        if (gameBoard.pos.value.pieceToMove) {
            handleClick(index)
            handleHighLighting()
            botJob = viewModelScope.launch {
                while (!gameBoard.pos.value.pieceToMove && gameBoard.pos.value.gameState() != GameState.End) {
                    // TODO: FIX THIS
                    // this line isn't needed, but for some reason
                    // without it, this code executes on the main thread
                    delay(10)
                    launchBot()
                }
            }
        }
    }

    private fun onUndo() {
        botJob?.cancel()
        botJob = viewModelScope.launch {
            delay(800)
            while (!gameBoard.pos.value.pieceToMove && gameBoard.pos.value.gameState() != GameState.End) {
                launchBot()
            }
        }
    }

    private var botJob: Job? = null

    /**
     * launches bot actions against player
     */
    private fun launchBot() {
        val bestMove = gameBoard.pos.value.findBestMove(4u)
        gameBoard.processMove(bestMove!!)
    }
}
