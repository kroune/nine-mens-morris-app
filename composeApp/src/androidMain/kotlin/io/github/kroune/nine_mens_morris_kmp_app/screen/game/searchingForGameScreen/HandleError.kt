package io.github.kroune.nine_mens_morris_kmp_app.screen.game.searchingForGameScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.SearchingForGameResponse

@Composable
fun HandleSearchingForGameError(
    uploadingNewPicture: SearchingForGameResponse?,
    snackbarHostState: SnackbarHostState
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
    LaunchedEffect(uploadingNewPicture) {
        snackbarHostState.showSnackbar(text)
    }
}
