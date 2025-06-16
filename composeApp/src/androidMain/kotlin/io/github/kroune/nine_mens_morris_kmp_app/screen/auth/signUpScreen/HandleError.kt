package io.github.kroune.nine_mens_morris_kmp_app.screen.auth.signUpScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.AccountIdByJwtTokenApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.RegisterApiResponses
import androidx.compose.ui.res.stringResource

@Composable
fun HandleSignUpError(
    registrationResult: RegisterApiResponses?,
    accountIdByJwtTokenResult: AccountIdByJwtTokenApiResponses?,
    snackbarHostState: SnackbarHostState
) {
    val text = when (registrationResult) {
        null -> {
            return
        }

        is RegisterApiResponses.Success -> {
            when (accountIdByJwtTokenResult) {
                is AccountIdByJwtTokenApiResponses.CredentialsError -> {
                    stringResource(R.string.credentials_error)
                }

                is AccountIdByJwtTokenApiResponses.NetworkError -> {
                    stringResource(R.string.network_error)
                }

                is AccountIdByJwtTokenApiResponses.ServerError -> {
                    stringResource(R.string.server_error)
                }

                is AccountIdByJwtTokenApiResponses.UnknownError -> {
                    stringResource(R.string.unknown_error)
                }

                is AccountIdByJwtTokenApiResponses.Success, null -> return
            }
        }

        is RegisterApiResponses.UnknownError -> {
            stringResource(R.string.unknown_error)
        }

        is RegisterApiResponses.LoginAlreadyInUse -> {
            stringResource(R.string.login_in_use)
        }

        is RegisterApiResponses.NetworkError -> {
            stringResource(R.string.network_error)
        }

        is RegisterApiResponses.ServerError -> {
            stringResource(R.string.server_error)
        }
    }
    LaunchedEffect(registrationResult) {
        snackbarHostState.showSnackbar(text)
    }
}
