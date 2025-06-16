package io.github.kroune.nine_mens_morris_kmp_app.screen.other.welcomeScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.AccountIdByJwtTokenApiResponses

@Composable
fun HandleWelcomeScreenError(
    result: AccountIdByJwtTokenApiResponses?,
    snackbarHostState: SnackbarHostState
) {
    val text: String = when (result) {
        is AccountIdByJwtTokenApiResponses.UnknownError -> {
            stringResource(R.string.unknown_error)
        }

        is AccountIdByJwtTokenApiResponses.NetworkError -> {
            stringResource(R.string.network_error)
        }

        is AccountIdByJwtTokenApiResponses.CredentialsError -> {
            stringResource(R.string.credentials_error)
        }

        is AccountIdByJwtTokenApiResponses.ServerError -> {
            stringResource(R.string.server_error)
        }

        is AccountIdByJwtTokenApiResponses.Success, null -> return
    }
    LaunchedEffect(result) {
        snackbarHostState.showSnackbar(text)
    }
}