package io.github.kroune.nine_mens_morris_kmp_app.screen.auth.signUpScreen

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
import io.github.kroune.nine_mens_morris_kmp_app.component.auth.singUp.SignUpScreenState
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.event.auth.SignUpScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.RoundedCornerShape3
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.shadowElevation1
import io.github.kroune.nine_mens_morris_kmp_app.screen.theme.ExtendedColorTheme
import androidx.compose.ui.res.painterResource

@Composable
fun SignUpScreen(
    component: SignUpScreenState,
    onEvent: (SignUpScreenEvent) -> Unit
) {
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
                    modifier = Modifier
                        .shadow(shadowElevation1, RoundedCornerShape3),
                    label = {
                        if (!isUsernameValid && username.isNotEmpty()) {
                            Text(
                                stringResource(R.string.invalid_login),
                                modifier = Modifier,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    },
                    placeholder = {
                        Text(stringResource(R.string.login))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.username),
                            "your preferred username"
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
                    password,
                    { newValue ->
                        onEvent(SignUpScreenEvent.UpdatePassword(newValue))
                    },
                    modifier = Modifier
                        .shadow(shadowElevation1, RoundedCornerShape3),
                    label = {
                        if (!isPasswordValid && password.isNotEmpty()) {
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
                            "your new password"
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
                    passwordRepeated,
                    { newValue ->
                        onEvent(SignUpScreenEvent.UpdateRepeatedPassword(newValue))
                    },
                    modifier = Modifier
                        .shadow(shadowElevation1, RoundedCornerShape3),
                    label = {
                        if (!isPasswordRepeatedValid && passwordRepeated.isNotEmpty()) {
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
                    onClick = {
                        onEvent(SignUpScreenEvent.Register)
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 5.dp
                    ),
                    enabled = isUsernameValid && isPasswordValid &&
                            isPasswordRepeatedValid && !registrationInProcess,
                    shape = RoundedCornerShape3
                ) {
                    Text(stringResource(R.string.sign_up))
                }
            }
        }
        HandleSignUpError(
            registrationResult,
            accountIdByJwtTokenResult,
            snackbarHostState
        )
    }
}
