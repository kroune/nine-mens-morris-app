package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GameBoardUseCase
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.gameStartPosition
import com.kroune.nineMensMorrisLib.move.Movement
import java.util.Stack

/**
 * view model for game board
 */
@Suppress("LongParameterList")
class GameBoardViewModel(
    /**
     * stores current position
     */
    pos: Position = gameStartPosition,
    /**
     * what will happen if we click some circle
     */
    onClick: GameBoardUseCase.(index: Int) -> Unit = { index ->
        handleClick(index)
        handleHighLighting()
    },
    /**
     * what we should additionally do on undo
     */
    onUndo: GameBoardUseCase.() -> Unit = {
        if (!movesHistory.empty()) {
            undoneMoveHistory.push(movesHistory.peek())
            movesHistory.pop()
            this.pos.value = movesHistory.lastOrNull() ?: gameStartPosition
            this.moveHints.value = arrayListOf()
            this.selectedButton.value = null
        }
    },
    /**
     * what we should execute on redo
     */
    onRedo: GameBoardUseCase.() -> Unit = {
        if (!undoneMoveHistory.empty()) {
            movesHistory.push(undoneMoveHistory.peek())
            undoneMoveHistory.pop()
            this.pos.value = movesHistory.lastOrNull() ?: gameStartPosition
            this.selectedButton.value = null
            this.moveHints.value = arrayListOf()
        }
    },
    /**
     * stores all pieces which can be moved (used for highlighting)
     */
    moveHints: MutableList<Int> = mutableListOf(),
    /**
     * used for storing info of the previous (valid one) clicked button
     */
    val selectedButton: MutableState<Int?> = mutableStateOf(null),
    onGameEnd: (Position) -> Unit
) : ViewModelI() {
    constructor(pos: Position, movesHistory: Stack<Position>) : this(
        pos = pos,
        onGameEnd = {}
    ) {
        useCase.movesHistory.clear()
        movesHistory.forEach {
            useCase.movesHistory.push(it)
        }
    }

    private val useCase = GameBoardUseCase(
        mutableStateOf(pos),
        mutableStateOf(moveHints),
        onUndo,
        onRedo,
        onClick,
        selectedButton,
        onGameEnd
    )

    val moveHints = useCase.moveHints

    /**
     * quick access
     */
    fun handleClick(index: Int) {
        return useCase.handleClick(index)
    }

    val movesHistory = useCase.movesHistory

    /**
     * handles clicking on the button with provided index
     */
    fun onClick(index: Int) {
        return useCase.run {
            onClick(index)
        }
    }

    /**
     * current position
     */
    val pos: MutableState<Position> = useCase.pos

    /**
     * quick access
     */
    fun handleUndo() {
        useCase.run {
            this.onUndo()
        }
    }

    /**
     * quick access
     */
    fun handleRedo() {
        useCase.run {
            this.onRedo()
        }
    }

    /**
     * quick access
     */
    fun handleHighLighting() {
        useCase.handleHighLighting()
    }

    /**
     * applies move to the position
     */
    fun processMove(move: Movement) {
        useCase.processMove(move)
    }

    /**
     * gets move produced after clicking button with provided index
     */
    fun getMovement(elementIndex: Int): Movement? {
        return useCase.getMovement(elementIndex)
    }
}

