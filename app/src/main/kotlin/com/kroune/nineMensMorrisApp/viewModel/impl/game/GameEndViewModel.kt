package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.common.toStack
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisLib.Position

class GameEndViewModel(
    pos: Position,
    val movesHistory: List<Position>
) : ViewModelI() {
    private val gameBoard = GameBoardViewModel(
        pos = pos,
        movesHistory = movesHistory.toStack()
    )

    val pos = gameBoard.pos

    fun handleUndo() {
        gameBoard.handleUndo()
    }

    fun handleRedo() {
        gameBoard.handleRedo()
    }
}