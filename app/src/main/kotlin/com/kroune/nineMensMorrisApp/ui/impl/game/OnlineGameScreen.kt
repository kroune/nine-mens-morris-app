package com.kroune.nineMensMorrisApp.ui.impl.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisApp.di.factoryProvider
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisApp.ui.interfaces.ScreenModelI
import com.kroune.nineMensMorrisApp.viewModel.impl.game.OnlineGameViewModel

/**
 * Game main screen
 */
class OnlineGameScreen(
    private val gameId: Long,
    private val navController: NavController
) : ScreenModelI {

    override lateinit var viewModel: OnlineGameViewModel

    @Composable
    override fun InvokeRender() {
        val factory = factoryProvider().onlineGameViewModelFactory()
        viewModel = viewModel(factory = OnlineGameViewModel.provideFactory(factory, gameId))
        val uiState = viewModel.uiState.collectAsState().value
        val gameEnded = viewModel.gameEnded.value
        if (gameEnded) {
            navController.navigateSingleTopTo(Navigation.GameEnd(uiState.gameBoard.pos.value))
        }
        AppTheme {
            uiState.gameBoard.RenderPieceCount()
            uiState.gameBoard.InvokeRender()
            Column {
                when (uiState.isGreen) {
                    true -> {
                        Text("You are green")
                    }

                    false -> {
                        Text("You are blue")
                    }

                    null -> {
                        Text("Waiting for server info")
                    }
                }
            }
        }
    }
}
