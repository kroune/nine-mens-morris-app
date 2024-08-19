package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.MutableState
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisLib.Position

/**
 * game with friend view model
 */
class GameWithFriendViewModel(
    onGameEnd: GameWithFriendViewModel.() -> Unit
) : ViewModelI() {
    /**
     * game board vm
     */
    private val gameBoardViewModel = GameBoardViewModel(
        onGameEnd = {
            onGameEnd()
        }
    )

    /**
     * game analyze vm
     */
    private val gameAnalyzeViewModel = GameAnalyzeViewModel(gameBoardViewModel.pos)

    /**
     * current position
     */
    val pos: MutableState<Position> = gameBoardViewModel.pos

    /**
     * currently selected button
     */
    val selectedButton: MutableState<Int?> = gameBoardViewModel.selectedButton

    val movesHistory = gameBoardViewModel.movesHistory

    /**
     * hints of possible moves
     */
    val moveHints: MutableState<List<Int>> = gameBoardViewModel.moveHints

    /**
     * responds to the click
     */
    fun onClick(button: Int) {
        gameBoardViewModel.onClick(button)
    }

    /**
     * handles undoes
     */
    fun handleUndo() {
        gameBoardViewModel.handleUndo()
    }

    /**
     * handles redoes
     */
    fun handleRedo() {
        gameBoardViewModel.handleRedo()
    }

    /**
     * depth of the analyse search
     */
    val depth = gameAnalyzeViewModel.depth

    /**
     * positions produced by game analyses
     */
    val positions = gameAnalyzeViewModel.positions

    /**
     * increases depth
     */
    fun increaseDepth() {
        gameAnalyzeViewModel.increaseDepth()
    }

    /**
     * decreases the depth
     */
    fun decreaseDepth() {
        gameAnalyzeViewModel.decreaseDepth()
    }

    /**
     * starts analysis of the game
     */
    fun startAnalyze() {
        gameAnalyzeViewModel.startAnalyze()
    }
}
