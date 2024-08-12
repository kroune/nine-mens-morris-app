package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.runtime.Composable
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisLib.Position

/**
 * renders game with bot screen
 */
@Composable
fun RenderGameWithBotScreen(
    pos: Position,
    selectedButton: Int?,
    moveHints: List<Int>,
    onClick: (Int) -> Unit,
    handleUndo: () -> Unit,
    handleRedo: () -> Unit
) {
    AppTheme {
        RenderGameBoard(
            pos = pos,
            selectedButton = selectedButton,
            moveHints = moveHints,
            onClick = onClick
        )
        RenderPieceCount(
            pos = pos
        )
        RenderUndoRedo(
            handleUndo = handleUndo,
            handleRedo = handleRedo
        )
    }
}
