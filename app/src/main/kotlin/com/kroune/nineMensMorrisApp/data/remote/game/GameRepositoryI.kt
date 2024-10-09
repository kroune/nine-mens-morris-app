package com.kroune.nineMensMorrisApp.data.remote.game

import kotlinx.coroutines.channels.Channel

/**
 * interface for repository for interacting with server games
 */
interface GameRepositoryI {
    /**
     * Starts searching for a game.
     *
     * @return [ServerResponse] indicating the success or failure of the search attempt.
     */
    suspend fun startSearchingGame(jwtToken: String): Channel<Pair<Boolean, Long>>

    /**
     * checks if we are currently playing a game
     */
    suspend fun isPlaying(jwtToken: String): Result<Long?>
}
