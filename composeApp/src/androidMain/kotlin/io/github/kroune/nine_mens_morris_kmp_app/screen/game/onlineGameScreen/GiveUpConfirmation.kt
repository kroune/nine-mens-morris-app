package io.github.kroune.nine_mens_morris_kmp_app.screen.game.onlineGameScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.ktor.websocket.Frame.Text

@Composable
fun GiveUpConfirmation(
    onGiveUpDiscarded: () -> Unit,
    onGiveUp: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onGiveUpDiscarded()
        },
        title = {
            Text(stringResource(R.string.want_to_give_up))
        },
        confirmButton = {
            Button(
                onClick = {
                    onGiveUp()
                }
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onGiveUpDiscarded()
                }
            ) {
                Text(stringResource(R.string.no))
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}
