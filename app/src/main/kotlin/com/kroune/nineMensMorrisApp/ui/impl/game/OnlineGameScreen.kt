package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisLib.Position


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
    RenderGameBoard(
        pos = pos,
        selectedButton = selectedButton,
        moveHints = moveHints,
        onClick = onClick
    )
    RenderUndoRedo(
        handleUndo = {
            handleUndo()
        },
        handleRedo = {
            handleRedo()
        }
    )
    Column {
        when (isGreen) {
            true -> {
                Text("You are green")
            }

            false -> {
                Text("You are blue")
            }

            null -> {
                Text("Waiting for server info")
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
