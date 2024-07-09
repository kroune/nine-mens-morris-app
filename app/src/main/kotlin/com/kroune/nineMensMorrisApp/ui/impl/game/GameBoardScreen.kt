package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kr8ne.mensMorris.Position
import com.kr8ne.mensMorris.gameStartPosition
import com.kroune.nineMensMorrisApp.BUTTON_WIDTH
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.data.local.impl.game.GameBoardData
import com.kroune.nineMensMorrisApp.ui.interfaces.ScreenModelI
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameBoardViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * screen that is shown at the end
 */
class GameBoardScreen(
    pos: Position = gameStartPosition,
    onClick: GameBoardData.(index: Int) -> Unit = { index ->
        handleClick(index)
        handleHighLighting()
    },
    handleUndo: () -> Unit = {},
    handleRedo: () -> Unit = {},
    navController: NavHostController?
) : ScreenModelI {

    override val viewModel = GameBoardViewModel(
        pos = pos,
        onClick = onClick,
        onUndo = handleUndo,
        onRedo = handleRedo,
        navController = navController
    )

    /**
     * used for quick access
     * direct link to the data is kinda wrong, but it makes this code much cleaner
     */
    var pos: MutableStateFlow<Position>
        get() {
            return viewModel.data.pos
        }
        set(value) {
            viewModel.data.pos = value
        }

    @Composable
    override fun InvokeRender() {
        Box(
            modifier = Modifier
                .fillMaxWidth(), Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(BUTTON_WIDTH)
                    .clip(RoundedCornerShape(15))
                    .size(BUTTON_WIDTH * 8.25f)
                    .background(Color(0xFF8F8F8F)),
                Alignment.Center,
            ) {
                DrawCircles()
                DrawHorizontalShadows()
                DrawVerticalShadows()
            }
        }
    }

    /**
     * renders piece counters
     */
    @Composable
    fun RenderPieceCount() {
        val uiState = viewModel.uiState.collectAsState().value
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(BUTTON_WIDTH * if (uiState.pos.pieceToMove) 1.5f else 1f)
                        .background(Color.Green, CircleShape)
                        .alpha(if (uiState.pos.freeGreenPieces == 0.toUByte()) 0f else 1f),
                    Alignment.Center
                ) {
                    Text(
                        color = Color.Blue,
                        text = uiState.pos.freeGreenPieces.toString()
                    )
                }
            }
            Box(
                modifier = Modifier, contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(BUTTON_WIDTH * if (!uiState.pos.pieceToMove) 1.5f else 1f)
                        .background(Color.Blue, CircleShape)
                        .alpha(if (uiState.pos.freeBluePieces == 0.toUByte()) 0f else 1f),
                    Alignment.Center
                ) {
                    Text(
                        color = Color.Green,
                        text = uiState.pos.freeBluePieces.toString()
                    )
                }
            }
        }
    }

    /**
     * there are ways not to hard code this, but it looses a lot of readability
     */
    @Composable
    private fun DrawCircles() {
        Column(
            modifier = Modifier.size(BUTTON_WIDTH * 7, BUTTON_WIDTH * 7)
        ) {
            RowOfCircles(0, 2, 0..2)
            RowOfCircles(1, 1, 3..5)
            RowOfCircles(2, 0, 6..8)
            Box {
                RowOfCircles(0, 0, 9..11)
                RowOfCircles(4, 0, 12..14)
            }
            RowOfCircles(2, 0, 15..17)
            RowOfCircles(1, 1, 18..20)
            RowOfCircles(0, 2, 21..23)
        }
    }

    @Composable
    private fun DrawVerticalShadows() {
        Row(
            modifier = Modifier.size(BUTTON_WIDTH * 7, BUTTON_WIDTH * 7)
        ) {
            VerticalShadow(0.25f, 0.25f)
            VerticalShadow(1.25f, 1.25f)
            VerticalShadow(2.25f, 2.25f)
            Column(
                modifier = Modifier
                    .padding(
                        top = BUTTON_WIDTH * 0.25f, bottom = BUTTON_WIDTH * 0.25f
                    )
                    .height(BUTTON_WIDTH * 7)
                    .width(BUTTON_WIDTH),
                verticalArrangement = Arrangement.spacedBy(
                    BUTTON_WIDTH * 1.5f, Alignment.CenterVertically
                )
            ) {
                VerticalShadow(0f, 0f, 2.5f, 2.5f)
                VerticalShadow(0f, 0f, 2.5f, 2.5f)
            }
            VerticalShadow(2.25f, 2.25f)
            VerticalShadow(1.25f, 1.25f)
            VerticalShadow(0.25f, 0.25f)
        }
    }

    @Composable
    private fun DrawHorizontalShadows() {
        Column(
            modifier = Modifier.size(BUTTON_WIDTH * 7, BUTTON_WIDTH * 7)
        ) {
            HorizontalShadow(0.25f, 0.25f)
            HorizontalShadow(1.25f, 1.25f)
            HorizontalShadow(2.25f, 2.25f)
            Row(
                modifier = Modifier
                    .padding(
                        start = BUTTON_WIDTH * 0.25f, end = BUTTON_WIDTH * 0.25f
                    )
                    .height(BUTTON_WIDTH)
                    .width(BUTTON_WIDTH * 7),
                horizontalArrangement = Arrangement.spacedBy(
                    BUTTON_WIDTH * 1.5f, Alignment.CenterHorizontally
                )
            ) {
                HorizontalShadow(0f, 0f, 2.5f, 2.5f)
                HorizontalShadow(0f, 0f, 2.5f, 2.5f)
            }
            HorizontalShadow(2.25f, 2.25f)
            HorizontalShadow(1.25f, 1.25f)
            HorizontalShadow(0.25f, 0.25f)
        }
    }

    @Composable
    private fun VerticalShadow(
        paddingLeft: Float,
        paddingRight: Float,
        length1: Float = 7f,
        length2: Float = (7 - (paddingLeft + paddingRight))
    ) {
        Box(
            modifier = Modifier
                .height(BUTTON_WIDTH * length1)
                .width(BUTTON_WIDTH * 1f)
                .padding(
                    top = BUTTON_WIDTH * paddingLeft,
                    start = BUTTON_WIDTH * 0.25f,
                    end = BUTTON_WIDTH * 0.25f,
                    bottom = BUTTON_WIDTH * paddingRight
                )
        ) {
            Box(
                modifier = Modifier
                    .alpha(0.5f)
                    .height(BUTTON_WIDTH * length2)
                    .width(BUTTON_WIDTH * 0.5f)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.DarkGray)
            ) {}
        }
    }

    @Composable
    private fun HorizontalShadow(
        paddingLeft: Float,
        paddingRight: Float,
        length1: Float = 7f,
        length2: Float = (7 - (paddingLeft + paddingRight))
    ) {
        Box(
            modifier = Modifier
                .height(BUTTON_WIDTH * 1f)
                .width(BUTTON_WIDTH * length1)
                .padding(
                    top = BUTTON_WIDTH * 0.25f,
                    start = BUTTON_WIDTH * paddingLeft,
                    end = BUTTON_WIDTH * paddingRight,
                    bottom = BUTTON_WIDTH * 0.25f
                )
        ) {
            Box(
                modifier = Modifier
                    .alpha(0.5f)
                    .height(BUTTON_WIDTH * 0.5f)
                    .width(BUTTON_WIDTH * length2)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.DarkGray)
            ) {}
        }
    }

    /**
     * draws a raw of circles
     * @param padding padding
     * @param gap between elements
     * @param range range of indexes
     */
    @Composable
    private fun RowOfCircles(
        padding: Int, gap: Int, range: IntRange
    ) {
        Row(
            modifier = Modifier.padding(start = BUTTON_WIDTH * padding),
            horizontalArrangement = Arrangement.spacedBy(
                BUTTON_WIDTH * gap, Alignment.CenterHorizontally
            ),
            Alignment.CenterVertically,
        ) {
            for (i in range) {
                CircledButton(elementIndex = i) {
                    viewModel.onClick(it)
                }
            }
        }
    }

    /**
     * draws a circles button
     * @param elementIndex index of this circle
     * @param onClick function we execute on click
     */
    @Composable
    private fun CircledButton(elementIndex: Int, onClick: (Int) -> Unit) {
        val uiState = viewModel.uiState.collectAsState().value
        Box(
            modifier = Modifier
                .size(BUTTON_WIDTH),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .size(BUTTON_WIDTH * if (viewModel.data.selectedButton.value == elementIndex) 0.8f else 1f)
                    .background(Color.Transparent, CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (uiState.pos.positions[elementIndex]) {
                        null -> {
                            Color.Transparent
                        }

                        true -> {
                            Color.Green
                        }

                        false -> {
                            Color.Blue
                        }
                    }
                ),
                shape = CircleShape,
                onClick = {
                    onClick(elementIndex)
                },
                border = if (!uiState.moveHints.contains(elementIndex)) null else BorderStroke(
                    BUTTON_WIDTH * 0.1f,
                    Color.DarkGray
                )
            ) {}
        }
    }

    /**
     * renders undo buttons
     */
    @Composable
    fun RenderUndoRedo() {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            FilledIconButton(
                modifier = Modifier
                    .size(BUTTON_WIDTH * 2f),
                onClick = {
                    viewModel.handleUndo()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.redo_move), "undo"
                )
            }
            FilledIconButton(modifier = Modifier
                .size(BUTTON_WIDTH * 2f),
                onClick = {
                    viewModel.handleRedo()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.undo_move), "redo"
                )
            }
        }
    }
}
