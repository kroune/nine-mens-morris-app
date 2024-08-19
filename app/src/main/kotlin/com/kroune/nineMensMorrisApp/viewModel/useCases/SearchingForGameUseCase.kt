package com.kroune.nineMensMorrisApp.viewModel.useCases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class SearchingForGameUseCase(
    val accountInfoRepository: AccountInfoRepositoryI,
    val gameRepository: GameRepositoryI
) {
    /**
     * id of the game
     * used for callback
     */
    val gameId = mutableStateOf<Long?>(null)

    val expectedWaitingTime: MutableState<Long?> = mutableStateOf(null)

    private suspend fun getCurrentGameId(): Long? {
        return gameRepository.isPlaying(accountInfoRepository.jwtTokenState.value!!).getOrNull()
    }

    fun searchForGame() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val currentGameId = getCurrentGameId()
                if (currentGameId != null) {
                    gameId.value = currentGameId
                    return@launch
                }
                val channel = gameRepository.startSearchingGame(
                    accountInfoRepository.jwtTokenState.value!!
                )
                channel.consumeEach {
                    // if this is a game id or expected waiting time
                    if (!it.first) {
                        gameId.value = it.second
                        return@launch
                    } else {
                        expectedWaitingTime.value = it.second
                    }
                }
            }
        }
    }
}