package com.kroune.nineMensMorrisApp.ui.impl.tutorial.domain

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderGameBoard
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderPieceCount
import com.kroune.nineMensMorrisLib.BLUE_
import com.kroune.nineMensMorrisLib.EMPTY
import com.kroune.nineMensMorrisLib.GREEN
import com.kroune.nineMensMorrisLib.Position

/**
 * this screen tells how to get removal moves
 */
@Composable
fun RenderTriplesTutorialScreen(
    resources: Resources
) {
    val position = Position(
        // @formatter:off
        arrayOf(
            BLUE_,                  BLUE_,                  BLUE_,
                    GREEN,          EMPTY,          EMPTY,
                            EMPTY,  EMPTY,  EMPTY,
            EMPTY,  GREEN,  EMPTY,          EMPTY,  EMPTY,  EMPTY,
                            EMPTY,  GREEN,  EMPTY,
                    EMPTY,          EMPTY,          EMPTY,
            EMPTY,                  BLUE_,                  GREEN
        ),
        // @formatter:on
        0u, 0u, pieceToMove = false, removalCount = 1u
    )
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Box {
            RenderGameBoard(
                pos = position,
                selectedButton = 3,
                moveHints = listOf(),
                onClick = {}
            )
            RenderPieceCount(
                pos = position
            )
        }
        Column(
            modifier = Modifier.padding(start = BUTTON_WIDTH, end = BUTTON_WIDTH),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = resources.getString(R.string.tutorial_triples_condition),
                textAlign = TextAlign.Center
            )
            Text(
                text = resources.getString(R.string.tutorial_triples_highlighting),
                textAlign = TextAlign.Center
            )
        }
    }
}
