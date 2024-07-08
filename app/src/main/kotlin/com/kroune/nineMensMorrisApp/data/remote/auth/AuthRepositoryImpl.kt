package com.kroune.nineMensMorrisApp.data.remote.auth

import com.kroune.NetworkResponse
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.utils.io.printStack
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * contains ways to authenticate
 */
class AuthRepositoryImpl @Inject constructor() : AuthRepositoryI {
    override fun loginValidator(login: String): Boolean {
        val length = login.length in 5..12
        val content = login.all { it.isLetterOrDigit() }
        return length && content
    }

    override fun passwordValidator(password: String): Boolean {
        val length = password.length in 7..14
        val validString = password.all { it.isLetterOrDigit() }
        val anyDigits = password.any { it.isDigit() }
        val anyLetters = password.any { it.isLetter() }
        return length && validString && anyDigits && anyLetters
    }

    override suspend fun register(login: String, password: String): Result<String> {
        return runCatching {
            val registerResult =
                network.get("http${SERVER_ADDRESS}${USER_API}/reg") {
                    method = HttpMethod.Get
                    url {
                        parameters["login"] = login
                        parameters["password"] = password
                    }
                }.bodyAsText()
            val registerResultText = Json.decodeFromString<NetworkResponse>(registerResult)
            return when (registerResultText.code) {
                // TODO: finish this
                200 -> {
                    Result.success(registerResultText.message!!)
                }

                else -> {
                    Result.failure(ServerException(registerResultText.message!!))
                }
            }
        }.onFailure {
            println("error accessing ${"http${SERVER_ADDRESS}${USER_API}/reg"}")
            it.printStack()
        }
    }

    override suspend fun login(login: String, password: String): Result<String> {
        return runCatching {
            val loginResult =
                network.get("http${SERVER_ADDRESS}${USER_API}/login") {
                    method = HttpMethod.Get
                    url {
                        parameters["login"] = login
                        parameters["password"] = password
                    }
                }.bodyAsText()
            val loginResultText = Json.decodeFromString<NetworkResponse>(loginResult)
            return when (loginResultText.code) {
                // TODO: finish this
                200 -> {
                    Result.success(loginResultText.message!!)
                }

                else -> {
                    Result.failure(ServerException(loginResultText.message!!))
                }
            }
        }.onFailure {
            println("error accessing ${"http${SERVER_ADDRESS}${USER_API}/login"}")
            it.printStack()
        }
    }

    override suspend fun checkJwtToken(jwtToken: String): Result<Boolean> {
        return runCatching {
            val result = network.get("http$SERVER_ADDRESS$USER_API/check-jwt-token") {
                method = HttpMethod.Get
                url {
                    parameters["jwtToken"] = jwtToken
                }
            }
            val text = Json.decodeFromString<NetworkResponse>(result.bodyAsText())
            if (text.code == 200) {
                text.message!!.toBooleanStrict()
            } else {
                error("response code != 200 ${result.bodyAsText()}")
            }
        }.onFailure {
            println("error checking jwt token")
            it.printStack()
        }
    }
}

/**
 * Represents the server's response to client requests.
 */
class ServerException(
    /**
     * server exception description
     */
    val text: String
) : Exception()
