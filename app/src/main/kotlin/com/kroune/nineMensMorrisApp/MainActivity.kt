package com.kroune.nineMensMorrisApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.kroune.nineMensMorrisApp.common.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * shows how thick our pieces & board will be
 */
val BUTTON_WIDTH = 35.dp

/**
 * activity our app is launched from
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * we initialize all important stuff here
     */
    @Suppress("LongMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(
            "com.kroune.nineMensMorrisLib",
            MODE_PRIVATE
        )
        StorageManager.sharedPreferences = sharedPreferences
        setContent {
            AppTheme {
                NavHost(baseContext)
            }
        }
    }
}

/**
 * custom navigation implementation, prevents duplications in backstack entries
 */
fun NavController.navigateSingleTopTo(
    route: Navigation,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val currentScreen = this@navigateSingleTopTo.currentBackStackEntry?.destination?.route
    val newScreen = Json.encodeToString(route)
    println("DEBUG: $currentScreen, $newScreen")
    this@navigateSingleTopTo.navigate(route) {
        builder()
        launchSingleTop = true
    }
}
