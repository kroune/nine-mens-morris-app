package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * game with bot model
 */
@HiltViewModel
class SearchingForGameViewModel @Inject constructor(
    private val gameRepositoryI: GameRepositoryI,
    private val accountInfoRepositoryI: AccountInfoRepositoryI
) : ViewModelI() {

    /**
     * id of the game
     * used for callback
     */
    val gameId = MutableStateFlow<Long?>(null)

    init {
        println("init")
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val oldGameId =
                    gameRepositoryI.isPlaying(accountInfoRepositoryI.jwtTokenState.value!!)
                        .getOrNull()
                if (oldGameId != null) {
                    gameId.value = oldGameId
                    return@launch
                }
                val newGameId = gameRepositoryI.startSearchingGame(
                    accountInfoRepositoryI.jwtTokenState.value!!
                ).getOrNull()
                if (newGameId != null) {
                    gameId.value = newGameId
                    break
                } else {
                    delay(20000L)
                }
            }
        }
    }
}
