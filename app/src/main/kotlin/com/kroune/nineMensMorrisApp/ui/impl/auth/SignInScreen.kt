package com.kroune.nineMensMorrisApp.ui.impl.auth

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.data.remote.AuthResults
import com.kroune.nineMensMorrisApp.data.remote.Common.networkScope
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Draws singing in screen
 * @param nextRoute - next route to switch after successful authentication
 */
@Composable
fun RenderSignInScreen(
    loginValidator: (String) -> Boolean,
    passwordValidator: (String) -> Boolean,
    onLogin: suspend (login: String, password: String) -> Unit,
    navController: NavHostController?,
    resources: Resources,
    nextRoute: Navigation,
    authResult: AuthResults?
) {
    val requestInProcess = remember { mutableStateOf(false) }
    val isUsernameValid = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (authResult) {
            is AuthResults.Failure -> {
                Text(
                    text = resources.getString(authResult.stringId),
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            is AuthResults.Success -> {
                Text(
                    text = resources.getString(R.string.switching_to_next_screen),
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            null -> {
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.3f))
        TextField(
            value = username.value,
            onValueChange = { newValue ->
                username.value = newValue
                isUsernameValid.value = loginValidator(username.value)
            },
            label = {
                if (!isUsernameValid.value) {
                    Text(
                        resources.getString(R.string.invalid_login),
                        modifier = Modifier,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            },
            placeholder = {
                Text(resources.getString(R.string.username))
            },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.username), null)
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        val isPasswordValid = remember { mutableStateOf(false) }
        val password = remember { mutableStateOf("") }
        TextField(
            password.value,
            { newValue ->
                password.value = newValue
                isPasswordValid.value = passwordValidator(password.value)
            },
            label = {
                if (!isPasswordValid.value) {
                    Text(
                        resources.getString(R.string.invalid_password),
                        modifier = Modifier,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            },
            placeholder = { Text(resources.getString(R.string.password)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.password), null
                )
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                requestInProcess.value = true
                CoroutineScope(networkScope).launch {
                    onLogin(username.value, password.value)
                    requestInProcess.value = false
                }
            },
            enabled = isUsernameValid.value && isPasswordValid.value && !requestInProcess.value
        ) {
            Text(resources.getString(R.string.sign_in))
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(resources.getString(R.string.no_account_question))
                TextButton(modifier = Modifier, onClick = {
                    navController?.navigateSingleTopTo(Navigation.SignUp(nextRoute))
                }) {
                    Text(resources.getString(R.string.sign_up), color = Color.Blue)
                }
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
}
