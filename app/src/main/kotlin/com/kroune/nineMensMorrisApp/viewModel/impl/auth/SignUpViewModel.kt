package com.kroune.nineMensMorrisApp.viewModel.impl.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.data.remote.AuthResults
import com.kroune.nineMensMorrisApp.data.remote.ClientNetworkException
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.auth.AuthRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This class is responsible for managing the data and UI rendering for the sign-up screen
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepositoryI,
    private val accountInfoRepository: AccountInfoRepositoryI
) : ViewModelI() {
    /**
     * result of the authentication
     */
    val authResult: MutableState<AuthResults?> = mutableStateOf(null)

    /**
     * current id of the user account
     */
    val accountId: Long?
        get() {
            return accountInfoRepository.accountIdState.value
        }

    /**
     * returns a valid resource id for the exception description
     */
    fun handleRegisterClientNetworkException(exception: ClientNetworkException): Int {
        return when (exception.reason) {
            "no [login] parameter found" -> {
                R.string.auth_app_error
            }

            "no [password] parameter found" -> {
                R.string.auth_app_error
            }

            "login + password aren't present in the db" -> {
                R.string.auth_no_account_found
            }

            else -> {
                if (exception.status in 500..526) {
                    R.string.auth_server_error
                } else {
                    println("unrecognised server response ${exception.reason} ${exception.status}")
                    exception.printStackTrace()
                    R.string.auth_app_error
                }
            }
        }
    }

    /**
     * returns a valid resource id for the exception description
     */
    fun handleGettingIdClientNetworkException(exception: ClientNetworkException): Int {
        return when (exception.reason) {
            "no [jwtToken] parameter found" -> {
                R.string.auth_app_error
            }

            "[jwtToken] parameter is not valid" -> {
                R.string.auth_app_error
            }

            else -> {
                if (exception.status in 500..526) {
                    R.string.auth_server_error
                } else {
                    println("unrecognised server response ${exception.reason} ${exception.status}")
                    exception.printStackTrace()
                    R.string.auth_app_error
                }
            }
        }
    }

    /**
     * logins into the account
     */
    suspend fun register(login: String, password: String) {
        val authResult = authRepository.register(login, password)
        if (authResult.isFailure) {
            val responseResourceId = when (val exception = authResult.exceptionOrNull()!!) {
                is ClientNetworkException -> {
                    handleRegisterClientNetworkException(exception)
                }

                else -> {
                    println("unrecognised exception")
                    exception.printStackTrace()
                    R.string.auth_app_error
                }
            }
            this@SignUpViewModel.authResult.value = AuthResults.Failure(responseResourceId)
            return
        }
        val jwtToken = authResult.getOrThrow()
        val accountIdResult = accountInfoRepository.getIdByJwtToken(jwtToken)
        if (accountIdResult.isFailure) {
            val exception = accountIdResult.exceptionOrNull()!!
            val responseResourceId = when (exception) {
                is ClientNetworkException -> {
                    handleGettingIdClientNetworkException(exception)
                }

                else -> {
                    println("unrecognised exception")
                    exception.printStackTrace()
                    R.string.auth_app_error
                }
            }
            this@SignUpViewModel.authResult.value = AuthResults.Failure(responseResourceId)
            return
        }
        val id = accountIdResult.getOrThrow()
        updateJwtToken(jwtToken)
        updateId(id)
        this@SignUpViewModel.authResult.value = AuthResults.Success()
    }

    /**
     * login validation function
     */
    fun loginValidator(login: String): Boolean {
        return authRepository.loginValidator(login)
    }

    /**
     * password validation function
     */
    fun passwordValidator(password: String): Boolean {
        return authRepository.passwordValidator(password)
    }

    /**
     * updates jwt token
     */
    fun updateJwtToken(token: String) {
        return accountInfoRepository.updateJwtTokenState(token)
    }

    /**
     * updates id according to the new value
     */
    fun updateId(id: Long) {
        return accountInfoRepository.updateAccountIdState(id)
    }
}
