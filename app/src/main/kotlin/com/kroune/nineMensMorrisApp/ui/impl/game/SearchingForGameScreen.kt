package com.kroune.nineMensMorrisApp.ui.impl.game

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kroune.nineMensMorrisApp.R


/**
 * renders searching for game screen
 */
@Composable
fun SearchingForGameScreen(
    expectedWaitingTime: Long?,
    resources: Resources
) {
    Column {
        Text("${resources.getString(R.string.searching_for_game)}...")
        // null = it is still loading
        if (expectedWaitingTime != null) {
            Text("${resources.getString(R.string.game_expected_waiting_time)} $expectedWaitingTime")
        }
    }
}
