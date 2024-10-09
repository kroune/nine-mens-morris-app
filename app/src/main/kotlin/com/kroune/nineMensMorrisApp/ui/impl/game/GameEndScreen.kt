package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.kroune.nineMensMorrisLib.Position

/**
 * Renders game end
 */
@Composable
fun RenderGameEnd(
    pos: Position,
    handleReset: () -> Unit,
    handleUndo: () -> Unit,
    handleRedo: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            RenderPieceCount(
                pos = pos
            )
            RenderGameBoard(
                pos = pos,
                selectedButton = null,
                moveHints = listOf(),
                onClick = {}
            )
        }
        Text(fontSize = 30.sp, text = "Game has ended")
        Button(onClick = {
            handleReset()
        }) {
            Text("Reset")
        }
        RenderUndoRedo(
            handleUndo = { handleUndo() },
            handleRedo = { handleRedo() }
        )
    }
}
