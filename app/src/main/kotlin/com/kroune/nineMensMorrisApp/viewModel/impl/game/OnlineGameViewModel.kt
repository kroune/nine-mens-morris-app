package com.kroune.nineMensMorrisApp.viewModel.impl.game

import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
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
    private val accountInfoRepository: AccountInfoRepositoryI
) : ViewModelI() {
    private val useCase = OnlineGameUseCase(accountInfoRepository)

    /**
     * handles clicking
     */
    fun onClick(index: Int) {
        useCase.gameBoard.onClick(index)
    }

    /**
     * performs give up actions
     */
    fun giveUp() {
        useCase.giveUp()
    }

    /**
     * if the player move first
     */
    val movesFirst = useCase.isGreen

    /**
     * move hints
     */
    val moveHints = useCase.gameBoard.moveHints

    /**
     * selected button
     */
    val selectedButton = useCase.gameBoard.selectedButton

    /**
     * current position
     */
    val pos = useCase.gameBoard.pos

    /**
     * history of played moves
     */
    val movesHistory = useCase.gameBoard.movesHistory

    /**
     * game id
     */
    var gameId: Long? = null

    /**
     * returns if game has ended
     */
    val gameEnded = useCase.gameEnded

    /**
     * passes game it to the vm
     */
    fun setVariables(gameIdData: Long) {
        gameId = gameIdData
    }

    /**
     * starts creating the game
     * we can't put this code in init {} block, because we firstly need to
     * [setVariables] to pass gameId to vm
     * you can also do this, using assistedInject, but it is much more complicated
     * and we have to use our own vm factory
     */
    fun init() {
        useCase.createGameConnection(gameId!!)
    }
}
