package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.OnlineGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * welcome model
 * called when app is launched
 */
@HiltViewModel
class OnlineGameViewModel @Inject constructor(
    private val accountInfoRepository: AccountInfoRepositoryI,
    private val gameRepository: GameRepositoryI
) : ViewModelI() {
    private val useCase = OnlineGameUseCase(accountInfoRepository, gameRepository)

    fun onClick(index: Int) {
        useCase.gameBoard.onClick(index)
    }

    fun giveUp() {
        useCase.giveUp()
    }

    val isGreen = useCase.isGreen
    val moveHints = useCase.gameBoard.moveHints
    val selectedButton = useCase.gameBoard.selectedButton
    val pos = useCase.gameBoard.pos
    val movesHistory = useCase.gameBoard.movesHistory

    var gameId: Long? = null

    val gameEnded = useCase.gameEnded

    fun setVariables(gameIdData: Long) {
        gameId = gameIdData
    }

    fun init() {
        useCase.createGameConnection(gameId!!)
    }
}
