package io.github.kroune.nine_mens_morris_kmp_app.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.component.auth.signIn.SignInScreenState
import io.github.kroune.nine_mens_morris_kmp_app.event.auth.SignInScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.model.AccountIdByJwtTokenApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.model.LoginApiResponse
import io.github.kroune.nine_mens_morris_kmp_app.screen.theme.ExtendedColorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    state: SignInScreenState,
    onEvent: (SignInScreenEvent) -> Unit
) {
    with(state) {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.no_account_question_mark))
                    TextButton(
                        onClick = {
                            onEvent(SignInScreenEvent.SwitchToSignInScreen)
                        },
                        colors = ExtendedColorTheme.colorScheme.linkColors
                    ) {
                        Text(stringResource(R.string.sign_up))
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    username,
                    { newValue ->
                        onEvent(SignInScreenEvent.UsernameUpdate(newValue))
                    },
                    label = {
                        if (!isUsernameValid) {
                            Text(
                                stringResource(R.string.invalid_login),
                                modifier = Modifier,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    placeholder = { Text(stringResource(R.string.login)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.username),
                            "your username"
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                TextField(
                    password,
                    { newValue ->
                        onEvent(SignInScreenEvent.PasswordUpdate(newValue))
                    },
                    label = {
                        if (!isPasswordValid) {
                            Text(
                                stringResource(R.string.invalid_password),
                                modifier = Modifier,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    placeholder = { Text(stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.password),
                            "your password"
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        onEvent(SignInScreenEvent.Login)
                    },
                    enabled = isUsernameValid && isPasswordValid && !requestInProcess
                ) {
                    Text(stringResource(R.string.sign_in))
                }
            }
        }
        HandleSignInError(loginResult, accountIdByJwtTokenResult, scope, snackbarHostState)
    }
}


@Composable
private fun HandleSignInError(
    registrationResult: LoginApiResponse?,
    accountIdByJwtTokenResult: AccountIdByJwtTokenApiResponses?,
    scope: CoroutineScope,
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
    SideEffect {
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }
}
