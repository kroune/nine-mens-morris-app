package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.SearchingForGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * game with bot model
 */
@HiltViewModel
class SearchingForGameViewModel @Inject constructor(
    private val accountInfoRepository: AccountInfoRepositoryI,
    private val gameRepository: GameRepositoryI
) : ViewModelI() {
    private val searchingForGameUseCase =
        SearchingForGameUseCase(accountInfoRepository, gameRepository)

    /**
     * game id
     */
    val gameId = searchingForGameUseCase.gameId

    /**
     * expected time to wait
     * calculated on the server
     */
    val expectedWaitingTime = searchingForGameUseCase.expectedWaitingTime

    init {
        searchingForGameUseCase.searchForGame()
    }
}
