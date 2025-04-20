package io.github.kroune.nine_mens_morris_kmp_app.screen.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.common.LoadingCircle
import io.github.kroune.nine_mens_morris_kmp_app.component.game.SearchingForGameComponent
import io.github.kroune.nine_mens_morris_kmp_app.model.SearchingForGameResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun SearchingForGameScreen(
    component: SearchingForGameComponent
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.searching_for_game),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val waitingTime =
                    component.expectedWaitingTime.receiveAsFlow().collectAsState(null).value
                if (waitingTime == null) {
                    LoadingCircle()
                } else {
                    Text(
                        "${stringResource(R.string.game_expected_waiting_time)} $waitingTime",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        HandleError(component.searchingForGameError.value, snackbarHostState, scope)
    }
}


@Composable
private fun HandleError(
    uploadingNewPicture: SearchingForGameResponse?,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val text = when (uploadingNewPicture) {
        is SearchingForGameResponse.Success -> {
            stringResource(R.string.image_was_updated)
        }

        is SearchingForGameResponse.ServerError -> {
            stringResource(R.string.server_error)
        }

        is SearchingForGameResponse.NetworkError -> {
            stringResource(R.string.network_error)
        }

        is SearchingForGameResponse.UnknownError -> {
            stringResource(R.string.unknown_error)
        }

        null -> return
    }
    SideEffect {
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }
}