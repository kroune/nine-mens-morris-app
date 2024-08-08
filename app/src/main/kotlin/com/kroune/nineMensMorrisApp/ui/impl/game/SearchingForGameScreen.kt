package com.kroune.nineMensMorrisApp.ui.impl.game

import android.content.res.Resources
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisApp.ui.interfaces.ScreenModelI
import com.kroune.nineMensMorrisApp.viewModel.impl.game.SearchingForGameViewModel

/**
 * Game main screen
 */
class SearchingForGameScreen(
    private val navController: NavHostController,
    private val resources: Resources,
) : ScreenModelI {

    override lateinit var viewModel: SearchingForGameViewModel

    @Composable
    override fun InvokeRender() {
        viewModel = hiltViewModel()
        val id = viewModel.gameId.collectAsState().value
        if (id != null) {
            navController.navigateSingleTopTo(Navigation.OnlineGame(id)) {
                popUpTo(Navigation.SearchingOnlineGame) {
                    inclusive = true
                }
            }
        }
        AppTheme {
            Text("${resources.getString(R.string.searching_for_game)}...")
        }
    }
}
