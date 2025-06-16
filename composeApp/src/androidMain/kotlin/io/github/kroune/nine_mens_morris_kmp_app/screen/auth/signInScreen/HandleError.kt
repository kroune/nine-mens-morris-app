package io.github.kroune.nine_mens_morris_kmp_app.screen.auth.signInScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.AccountIdByJwtTokenApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.LoginApiResponse

@Composable
fun HandleSignInError(
    registrationResult: LoginApiResponse?,
    accountIdByJwtTokenResult: AccountIdByJwtTokenApiResponses?,
    snackbarHostState: SnackbarHostState
) {
    val text = when (registrationResult) {
        null -> {
            return
        }

        is LoginApiResponse.Success -> {
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

        is LoginApiResponse.UnknownError -> {
            stringResource(R.string.unknown_error)
        }

        is LoginApiResponse.NetworkError -> {
            stringResource(R.string.network_error)
        }

        is LoginApiResponse.ServerError -> {
            stringResource(R.string.server_error)
        }

        is LoginApiResponse.CredentialsError -> {
            stringResource(R.string.wrong_pass_or_login)
        }
    }
    LaunchedEffect(registrationResult, accountIdByJwtTokenResult) {
        snackbarHostState.showSnackbar(text)
    }
}
