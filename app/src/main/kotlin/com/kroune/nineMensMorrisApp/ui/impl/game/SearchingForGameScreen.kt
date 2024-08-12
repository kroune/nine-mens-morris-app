package com.kroune.nineMensMorrisApp.ui.impl.game

import android.content.res.Resources
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.AppTheme


/**
 * renders searching for game screen
 */
@Composable
fun SearchingForGameScreen(resources: Resources) {
    AppTheme {
        Text("${resources.getString(R.string.searching_for_game)}...")
    }
}
