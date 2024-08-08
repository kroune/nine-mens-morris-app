package com.kroune.nineMensMorrisApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kr8ne.mensMorris.Position
import com.kroune.nineMensMorrisApp.ui.impl.AppStartAnimationScreen
import com.kroune.nineMensMorrisApp.ui.impl.WelcomeScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.SignInScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.SignUpScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.ViewAccountScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.GameEndScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.GameWithBotScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.GameWithFriendScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.OnlineGameScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.SearchingForGameScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

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
            "com.kr8ne.mensMorris",
            MODE_PRIVATE
        )
        StorageManager.sharedPreferences = sharedPreferences
        val resources = resources
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Navigation.AppStartAnimation,
                enterTransition = {
                    fadeIn(initialAlpha = 0f, animationSpec = tween(800))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(800))
                }) {
                composable<Navigation.Welcome> {
                    WelcomeScreen(navController, sharedPreferences, resources).InvokeRender()
                }
                composable<Navigation.GameWithBot> {
                    GameWithBotScreen(navController = navController).InvokeRender()
                }
                composable<Navigation.GameWithFriend> {
                    GameWithFriendScreen(navController).InvokeRender()
                }
                composable<Navigation.GameEnd>(
                    typeMap = mapOf(typeOf<Position>() to PositionNavType())
                ) {
                    val pos = it.toRoute<Navigation.GameEnd>().position
                    GameEndScreen(pos, navController).InvokeRender()
                }
                composable<Navigation.SignUp>(
                    typeMap = mapOf(typeOf<Navigation>() to NavigationNavType())
                ) {
                    val route = it.toRoute<Navigation.SignUp>().nextRoute
                    SignUpScreen(navController, route, resources).InvokeRender()
                }
                composable<Navigation.SignIn>(
                    typeMap = mapOf(typeOf<Navigation>() to NavigationNavType())
                ) {
                    val nextRoute = it.toRoute<Navigation.SignIn>().nextRoute
                    SignInScreen(navController, nextRoute, resources).InvokeRender()
                }
                composable<Navigation.SearchingOnlineGame> {
                    SearchingForGameScreen(navController, resources).InvokeRender()
                }
                composable<Navigation.OnlineGame> {
                    val id = it.toRoute<Navigation.OnlineGame>().id
                    OnlineGameScreen(
                        id,
                        navController
                    ).InvokeRender()
                }
                composable<Navigation.AppStartAnimation> {
                    AppStartAnimationScreen(navController).InvokeRender()
                }
                composable<Navigation.ViewAccount> {
                    val id = it.toRoute<Navigation.ViewAccount>().id
                    ViewAccountScreen(id, navController).InvokeRender()
                }
            }
        }
    }
}

/**
 * custom navigation implementation, prevents duplications in backstack entries
 */
fun NavController.navigateSingleTopTo(route: Navigation, builder: NavOptionsBuilder.() -> Unit = {}) {
    val currentScreen = this@navigateSingleTopTo.currentBackStackEntry?.destination?.route
    val newScreen = Json.encodeToString(route)
    println("DEBUG: $currentScreen, $newScreen")
    this@navigateSingleTopTo.navigate(route) {
        builder()
        launchSingleTop = true
    }
}
