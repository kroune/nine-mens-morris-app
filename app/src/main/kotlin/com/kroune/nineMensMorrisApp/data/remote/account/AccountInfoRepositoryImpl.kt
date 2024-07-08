package com.kroune.nineMensMorrisApp.data.remote.account

import android.util.Log
import com.kroune.nineMensMorrisApp.StorageManager
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

/**
 * remote repository implementation
 */
class AccountInfoRepositoryImpl : AccountInfoRepositoryI {
    override val accountIdState = MutableStateFlow(StorageManager.getLong("accountId"))

    override fun updateAccountIdState(value: Long?) {
        Log.d("AccountId", "account id has changed to $value")
        StorageManager.putLong("accountId", value)
        accountIdState.value = value
    }

    override val jwtTokenState = MutableStateFlow(StorageManager.getString("jwtToken"))

    override fun updateJwtTokenState(value: String?) {
        Log.d("JwtToken", "jwt token has changed to $value")
        if (jwtTokenState.value == value) {
            // nothing changed, no need to update anything
            return
        }
        jwtTokenState.value = value
        StorageManager.putString("jwtToken", value)
        if (value == null) {
            updateAccountIdState(null)
            return
        }
        // we don't want to use incorrect account id
        // it will be null until a new one is received
        updateAccountIdState(null)
        CoroutineScope(Dispatchers.IO).launch {
            var failureCounter = 0
            while (true) {
                val idResult = getIdByJwtToken(value)
                if (idResult.isFailure) {
                    // error making request
                    delay(3000L)
                    continue
                }
                val newId = idResult.getOrThrow()
                if (newId == null) {
                    failureCounter++
                    if (failureCounter > 5) {
                        // some wierd shit is happening, let's just sign out
                        updateAccountIdState(null)
                        updateJwtTokenState(null)
                        Log.e(
                            "ACCOUNT",
                            "User successfully logged in, but server could not provide id for this account"
                        )
                        return@launch
                    }
                    continue
                }
                // we were able to update account id
                updateAccountIdState(newId)
                break
            }
        }
    }

    override suspend fun getAccountDateById(id: Long): Result<Triple<Int, Int, Int>?> {
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-creation-date-by-id") {
                method = HttpMethod.Get
                url {
                    parameters["id"] = id.toString()
                }
            }.bodyAsText()
            Json.decodeFromString<Triple<Int, Int, Int>?>(request)
        }.onFailure {
            println("error getting account creation date $id")
            it.printStackTrace()
        }
    }

    override suspend fun getAccountNameById(id: Long): Result<String?> {
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-login-by-id") {
                method = HttpMethod.Get
                url {
                    parameters["id"] = id.toString()
                }
            }.bodyAsText()
            Json.decodeFromString<String?>(request)
        }.onFailure {
            println("error getting account name $id")
            it.printStackTrace()
        }
    }

    override suspend fun getIdByJwtToken(jwtToken: String): Result<Long?> {
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-id-by-jwt-token") {
                method = HttpMethod.Get
                url {
                    parameters["jwtToken"] = jwtToken
                }
            }.bodyAsText()
            Json.decodeFromString<Long?>(request)
        }.onFailure {
            println("error getting account id $jwtToken")
            it.printStackTrace()
        }
    }

    // TODO: implement this on server
    override suspend fun getAccountPictureById(id: Long): Result<ByteArray> {
        return runCatching<AccountInfoRepositoryImpl, ByteArray> {
            val url = "https://shapka-youtube.ru/wp-content/uploads/2020/08/man-silhouette.jpg"
            val httpResponse: HttpResponse = network.get(url)
            httpResponse.body()
        }.onFailure {
            println("error getting account picture $id")
            it.printStackTrace()
        }
    }
}
