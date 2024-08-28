package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisLib.Position
import kotlin.math.roundToInt


/**
 * renders online game screen
 */
@Composable
fun RenderOnlineGameScreen(
    pos: Position,
    selectedButton: Int?,
    moveHints: List<Int>,
    onClick: (Int) -> Unit,
    handleUndo: () -> Unit,
    handleRedo: () -> Unit,

    onGiveUp: () -> Unit,
    gameEnded: Boolean,
    isGreen: Boolean?,
    navController: NavHostController
) {
    if (gameEnded) {
        navController.navigateSingleTopTo(
            Navigation.GameEnd(pos)
        )
    }

    // Vertically Draggable Modifier
    var offsetY by remember { mutableStateOf(0f) }

    AppTheme {
        Column() {

            PlayersUI()

            if (displayGiveUpConfirmation.value) {
                GiveUpConfirm(
                    giveUp = {
                        onGiveUp()
                    },
                    navController = navController
                )
            }
            if (!gameEnded) {
                BackHandler {
                    displayGiveUpConfirmation.value = true
                }
            }
            RenderPieceCount(
                pos = pos
            )

            Box(
                modifier = Modifier
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            offsetY += delta
                        }
                    )) {
                RenderGameBoard(
                    pos = pos,
                    selectedButton = selectedButton,
                    moveHints = moveHints,
                    onClick = onClick
                )
            }

            RenderUndoRedo(
                handleUndo = {
                    handleUndo()
                },
                handleRedo = {
                    handleRedo()
                }
            )
            Column {
                if (isGreen == null) {Text("Waiting for server info")}
            }
        }

    }
}

private val displayGiveUpConfirmation = mutableStateOf(false)

@Composable
private fun GiveUpConfirm(
    giveUp: () -> Unit,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), contentAlignment = Alignment.Center
    ) {
        Column() {
            Text("Are you sure you want to give up?")
            Button(onClick = {
                giveUp()
                navController.navigateSingleTopTo(Navigation.Welcome)
            }) {
                Text("Yes")
            }
            Button(onClick = {
                displayGiveUpConfirmation.value = false
            }) {
                Text("No")
            }
        }
    }
}

@Composable
fun TurnTimerUI() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "Time left: 20 s",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Composable
fun PlayerCard(
    playerName: String,
    avatarRes: Int,
    chipColor: Color,
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = playerName, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(chipColor, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: $rating", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PlayersUI() {
//    val uiState = viewModel.gameBoard.pos.collectAsState().value //экземпляр состояния доски из VM
    //где pos - это позиции фишек, в классе Position, и там же счётчик с колличеством
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerCard(
                playerName = "Player 1",
                avatarRes = R.drawable.pv,
                chipColor = Color.Green,
                rating = 123,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            PlayerCard(
                playerName = "Player 2",
                avatarRes = R.drawable.chert_risunok_26,
                chipColor = Color.Blue,
                rating = 456,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TurnTimerUI() // Отдельный элемент для таймера хода
    }
}
