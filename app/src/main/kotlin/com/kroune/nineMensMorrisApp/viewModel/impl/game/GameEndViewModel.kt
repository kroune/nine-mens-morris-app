package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.common.toStack
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisLib.Position

/**
 * Represents the view model for the game end screen.
 * It holds the game position and provides methods to handle undo and redo actions.
 *
 * @param pos The current game position.
 * @param movesHistory A list of previous positions in the game.
 */
class GameEndViewModel(
    pos: Position,
    movesHistory: List<Position>
) : ViewModelI() {
    private val gameBoard = GameBoardViewModel(
        pos = pos,
        movesHistory = movesHistory.toStack()
    )

    /**
     * game position
     */
    val pos = gameBoard.pos

    /**
     * handles undo clicks (goes to previous moves)
     */
    fun handleUndo() {
        gameBoard.handleUndo()
    }

    /**
     * handles redo clicks (goes back to current moves)
     */
    fun handleRedo() {
        gameBoard.handleRedo()
    }
}
