package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisLib.Position

/**
 * Renders game end
 */
@Composable
fun RenderGameEnd(pos: Position, handleReset: () -> Unit) {
    AppTheme {
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
            DrawButtons(handleReset)
        }
    }
}

@Composable
private fun DrawButtons(handleReset: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(0.dp, BUTTON_WIDTH * 0.5f, 0.dp, 0.dp)
    ) {
        Text(fontSize = 30.sp, text = "Game has ended")
    }
    Box(
        modifier = Modifier
            .padding(0.dp, BUTTON_WIDTH * 6, 0.dp, 0.dp)
    ) {
        Button(onClick = {
            handleReset()
        }) {
            Text("Reset")
        }
    }
}
