package com.kroune.nineMensMorrisApp.ui.impl.game

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisLib.Position

/**
 * Renders game analysis
 */
@Composable
fun RenderGameAnalyzeScreen(
    positions: List<Position>,
    depth: Int,
    startAnalyze: () -> Unit,
    increaseDepth: () -> Unit,
    decreaseDepth: () -> Unit
) {
    // I have tried to find a good placement of analyze screen, but it just doesn't suit
    if (LocalConfiguration.current.orientation != ORIENTATION_PORTRAIT)
        return
    if (positions.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(0.dp, BUTTON_WIDTH * 3f, 0.dp, 0.dp)
                .background(Color.DarkGray, RoundedCornerShape(5))
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false)
                ) {
                    positions.forEach {
                        RenderGameBoard(
                            it,
                            null,
                            mutableListOf(),
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(), Alignment.TopCenter
    ) {
        Button(onClick = {
            startAnalyze()
        }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Analyze")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            decreaseDepth()
                        },
                        colors = ButtonColors(
                            Color(177, 134, 255, 50),
                            Color.White,
                            Color.Unspecified,
                            Color.Unspecified
                        )
                    ) {
                        // may be it is a bit better to use some icons
                        // but I will leave it like this for now
                        Text("-", fontSize = 30.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("depth - $depth", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            increaseDepth()
                        },
                        colors = ButtonColors(
                            Color(177, 134, 255, 50),
                            Color.White,
                            Color.Unspecified,
                            Color.Unspecified
                        )
                    ) {
                        // may be it is a bit better to use some icons
                        // but I will leave it like this for now
                        Text("+", fontSize = 22.sp)
                    }
                }
            }
        }
    }
}
