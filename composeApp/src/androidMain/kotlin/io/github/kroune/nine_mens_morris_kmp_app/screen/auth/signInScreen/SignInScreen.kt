package io.github.kroune.nine_mens_morris_kmp_app.screen.auth.signInScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.component.auth.signIn.SignInScreenState
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.event.auth.SignInScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.RoundedCornerShape3
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.shadowElevation1
import io.github.kroune.nine_mens_morris_kmp_app.screen.theme.ExtendedColorTheme
import androidx.compose.ui.res.painterResource

@Composable
fun SignInScreen(
    state: SignInScreenState,
    onEvent: (SignInScreenEvent) -> Unit
) {
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
                state.username,
                { newValue ->
                    onEvent(SignInScreenEvent.UsernameUpdate(newValue))
                },
                modifier = Modifier
                    .shadow(shadowElevation1, RoundedCornerShape3),
                label = {
                    if (!state.isUsernameValid && state.username.isNotEmpty()) {
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
                },
                shape = RoundedCornerShape3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.025f))
            TextField(
                state.password,
                { newValue ->
                    onEvent(SignInScreenEvent.PasswordUpdate(newValue))
                },
                modifier = Modifier
                    .shadow(shadowElevation1, RoundedCornerShape3),
                label = {
                    if (!state.isPasswordValid && state.password.isNotEmpty()) {
                        Text(
                            stringResource(R.string.invalid_password),
                            modifier = Modifier,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                },
                placeholder = {
                    Text(stringResource(R.string.password))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.password),
                        "your password"
                    )
                },
                shape = RoundedCornerShape3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    onEvent(SignInScreenEvent.Login)
                },
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp
                ),
                enabled = state.isUsernameValid && state.isPasswordValid && !state.requestInProcess,
                shape = RoundedCornerShape3
            ) {
                Text(
                    stringResource(R.string.sign_in)
                )
            }
        }
    }
    HandleSignInError(
        state.loginResult,
        state.accountIdByJwtTokenResult,
        snackbarHostState
    )
}
