package com.kroune.nineMensMorrisApp.data.remote.auth

/**
 * interface for auth repository
 * @see AuthRepositoryImpl
 */
interface AuthRepositoryI {
    /**
     * checks if jwt token is valid
     */
    suspend fun checkJwtToken(jwtToken: String): Result<Boolean>

    /**
     * Attempts to login the user with the provided credentials.
     *
     * @return [ServerResponse] indicating the success or failure of the login attempt.
     */
    suspend fun login(login: String, password: String): Result<String>

    /**
     * Attempts to register the user with the provided credentials.
     *
     * @return [ServerResponse] indicating the success or failure of the registration attempt.
     */
    suspend fun register(login: String, password: String): Result<String>

    /**
     * Validates the provided login.
     *
     * @param login The login to be validated.
     * @return True if the login is valid, false otherwise.
     */
    fun loginValidator(login: String): Boolean

    /**
     * Validates the provided password.
     *
     * @param password The password to be validated.
     * @return True if the password is valid, false otherwise.
     */
    fun passwordValidator(password: String): Boolean
}
