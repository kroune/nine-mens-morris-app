package com.kroune.nineMensMorrisApp.viewModel.useCases

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.data.local.interfaces.DataI
import com.kroune.nineMensMorrisLib.GameState
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.gameStartPosition
import com.kroune.nineMensMorrisLib.move.Movement
import com.kroune.nineMensMorrisLib.move.moveProvider
import java.util.Stack

/**
 * Game board use case
 */
class GameBoardUseCase(
    /**
     * stores current position
     */
    val pos: MutableState<Position> = mutableStateOf(gameStartPosition),
    /**
     * stores all pieces which can be moved (used for highlighting)
     */
    val moveHints: MutableState<List<Int>> = mutableStateOf(listOf()),
    /**
     * what we should execute on undo
     */
    val onUndo: GameBoardUseCase.() -> Unit,
    /**
     * what we should execute on redo
     */
    val onRedo: GameBoardUseCase.() -> Unit,
    /**
     * what will happen if we click some circle
     */
    var onClick: GameBoardUseCase.(index: Int) -> Unit,
    /**
     * used for storing info of the previous (valid one) clicked button
     */
    val selectedButton: MutableState<Int?> = mutableStateOf(null),
    /**
     * what should happen on game end
     */
    val onGameEnd: (pos: Position) -> Unit
) : DataI() {

    /**
     * stores all movements (positions) history
     */
    val movesHistory: Stack<Position> = Stack()

    /**
     * stores a moves we have undone
     * resets them if we do any other move
     */
    val undoneMoveHistory: Stack<Position> = Stack()

    /**
     * processes selected movement
     */
    fun processMove(move: Movement) {
        pos.value = move.producePosition(pos.value).copy()
        selectedButton.value = null
        saveMove(pos.value)
        if (pos.value.gameState() == GameState.End) {
            onGameEnd(pos.value)
        }
    }

    /**
     * saves a move we have made
     */
    private fun saveMove(pos: Position) {
        if (undoneMoveHistory.isNotEmpty()) {
            undoneMoveHistory.clear()
        }
        movesHistory.push(pos)
    }

    /**
     * gets movement produced by user click
     */
    @Suppress("ReturnCount")
    fun getMovement(elementIndex: Int): Movement? {
        when (pos.value.gameState()) {
            GameState.Placement -> {
                if (pos.value.positions[elementIndex] == null) {
                    return Movement(null, elementIndex)
                }
            }

            GameState.Normal -> {
                if (selectedButton.value != null) {
                    if (moveProvider[selectedButton.value!!].filter { endIndex ->
                            pos.value.positions[endIndex] == null
                        }.contains(elementIndex)) {
                        return Movement(selectedButton.value, elementIndex)
                    }
                }
            }

            GameState.Flying -> {
                if (selectedButton.value != null) {
                    if (pos.value.positions[elementIndex] == null) {
                        return Movement(selectedButton.value, elementIndex)
                    }
                }
            }

            GameState.Removing -> {
                if (pos.value.positions[elementIndex] == !pos.value.pieceToMove) {
                    return Movement(elementIndex, null)
                }
            }

            GameState.End -> {
            }
        }
        return null
    }

    /**
     * handles click on the pieces
     * @param elementIndex element that got clicked
     */
    fun handleClick(elementIndex: Int) {
        when (pos.value.gameState()) {
            GameState.Placement -> {
                if (pos.value.positions[elementIndex] == null) {
                    processMove(Movement(null, elementIndex))
                }
            }

            GameState.Normal -> {
                if (selectedButton.value == null) {
                    if (pos.value.positions[elementIndex] == pos.value.pieceToMove) {
                        selectedButton.value = elementIndex
                    }
                } else {
                    if (moveProvider[selectedButton.value!!].filter { endIndex ->
                            pos.value.positions[endIndex] == null
                        }.contains(elementIndex)) {
                        processMove(Movement(selectedButton.value, elementIndex))
                    } else {
                        selectedButton.value = elementIndex
                    }
                }
            }

            GameState.Flying -> {
                if (selectedButton.value == null) {
                    if (pos.value.positions[elementIndex] == pos.value.pieceToMove)
                        selectedButton.value = elementIndex
                } else {
                    if (pos.value.positions[elementIndex] == null) {
                        processMove(Movement(selectedButton.value, elementIndex))
                    } else {
                        selectedButton.value = elementIndex
                    }
                }
            }

            GameState.Removing -> {
                if (pos.value.positions[elementIndex] == !pos.value.pieceToMove) {
                    processMove(Movement(elementIndex, null))
                }
            }

            GameState.End -> {
                Log.e("screen switching error", "tried to handle move with END game state")
            }
        }
    }

    /**
     * finds pieces we should highlight
     */
    fun handleHighLighting() {
        pos.value.generateMoves().let { moves ->
            when (pos.value.gameState()) {
                GameState.Placement -> {
                    moveHints.value = moves.map { it.endIndex!! }.toMutableList()
                }

                GameState.Normal -> {
                    if (selectedButton.value == null) {
                        moveHints.value = moves.map { it.startIndex!! }.toMutableList()
                    } else {
                        moveHints.value = moves.filter { it.startIndex == selectedButton.value }
                            .map { it.endIndex!! }.toMutableList()
                    }
                }

                GameState.Flying -> {
                    if (selectedButton.value == null) {
                        moveHints.value = moves.map { it.startIndex!! }.toMutableList()
                    } else {
                        moveHints.value = moves.filter { it.startIndex == selectedButton.value }
                            .map { it.endIndex!! }.toMutableList()
                    }
                }

                GameState.Removing -> {
                    moveHints.value = moves.map { it.startIndex!! }.toMutableList()
                }

                GameState.End -> {
                }
            }
        }
    }
}
