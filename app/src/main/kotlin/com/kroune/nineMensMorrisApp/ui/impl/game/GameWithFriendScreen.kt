package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisLib.Position

/**
 * Renders game with friend screen
 */
@Composable
fun RenderGameWithFriendScreen(
    pos: Position,
    selectedButton: Int?,
    moveHints: List<Int>,
    onClick: (Int) -> Unit,
    handleUndo: () -> Unit,
    handleRedo: () -> Unit,

    positions: List<Position>,
    depth: Int,
    increaseDepth: () -> Unit,
    decreaseDepth: () -> Unit,
    startAnalyze: () -> Unit,
) {
    AppTheme {
        RenderGameBoard(
            pos = pos,
            selectedButton = selectedButton,
            moveHints = moveHints,
            onClick = {
                onClick(it)
            }
        )
        RenderPieceCount(pos = pos)
        RenderUndoRedo(handleUndo = handleUndo, handleRedo = handleRedo)
        Box(
            modifier = Modifier
                .padding(0.dp, BUTTON_WIDTH * 9.5f, 0.dp, 0.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
        ) {
            RenderGameAnalyzeScreen(
                positions = positions,
                depth = depth,
                startAnalyze = { startAnalyze() },
                increaseDepth = { increaseDepth() },
                decreaseDepth = { decreaseDepth() }
            )
        }
    }
}
