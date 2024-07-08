package com.kroune.nineMensMorrisApp.data.remote.game

/**
 * interface for repository for interacting with server games
 */
interface GameRepositoryI {
    /**
     * Starts searching for a game.
     *
     * @return [ServerResponse] indicating the success or failure of the search attempt.
     */
    suspend fun startSearchingGame(jwtToken: String): Result<Long>

    /**
     * checks if we are currently playing a game
     */
    suspend fun isPlaying(jwtToken: String): Result<Long?>
}
