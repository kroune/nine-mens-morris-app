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
import io.github.kroune.nine_mens_morris_kmp_app.component.auth.SignUpScreenState
import io.github.kroune.nine_mens_morris_kmp_app.event.auth.SignUpScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.model.AccountIdByJwtTokenApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.model.RegisterApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.screen.theme.ExtendedColorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    component: SignUpScreenState,
    onEvent: (SignUpScreenEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    with(component) {
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
                    Text(stringResource(R.string.have_account_question_mark))
                    TextButton(
                        modifier = Modifier,
                        onClick = {
                            onEvent(SignUpScreenEvent.SwitchToSignInScreen)
                        },
                        colors = ExtendedColorTheme.colorScheme.linkColors
                    ) {
                        Text(stringResource(R.string.sign_in))
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
                        onEvent(SignUpScreenEvent.UpdateUsername(newValue))
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
                            "your preferred username"
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                TextField(
                    password,
                    { newValue ->
                        onEvent(SignUpScreenEvent.UpdatePassword(newValue))
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
                            "your new password"
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                TextField(
                    passwordRepeated,
                    { newValue ->
                        onEvent(SignUpScreenEvent.UpdateRepeatedPassword(newValue))
                    },
                    label = {
                        if (!isPasswordRepeatedValid) {
                            Text(
                                stringResource(R.string.passes_do_not_match),
                                modifier = Modifier,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    placeholder = { Text(stringResource(R.string.repeat_pass)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.password),
                            stringResource(R.string.repeat_pass)
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        onEvent(SignUpScreenEvent.Register)
                    },
                    enabled = isUsernameValid && isPasswordValid &&
                            isPasswordRepeatedValid && !registrationInProcess
                ) {
                    Text(stringResource(R.string.sign_up))
                }
            }
        }
        HandleSignUpError(
            registrationResult,
            accountIdByJwtTokenResult,
            scope,
            snackbarHostState
        )
    }
}

@Composable
private fun HandleSignUpError(
    registrationResult: RegisterApiResponses?,
    accountIdByJwtTokenResult: AccountIdByJwtTokenApiResponses?,
    scope: CoroutineScope,
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
    SideEffect {
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }
}
