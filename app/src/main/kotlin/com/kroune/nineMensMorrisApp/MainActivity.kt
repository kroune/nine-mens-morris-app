package com.kroune.nineMensMorrisApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kroune.nineMensMorrisApp.data.remote.AuthResults
import com.kroune.nineMensMorrisApp.di.factoryProvider
import com.kroune.nineMensMorrisApp.ui.impl.AppStartAnimationScreen
import com.kroune.nineMensMorrisApp.ui.impl.RenderWelcomeScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.RenderSignInScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.RenderSignUpScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.RenderViewAccountScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderGameEnd
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderGameWithBotScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderGameWithFriendScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.RenderOnlineGameScreen
import com.kroune.nineMensMorrisApp.ui.impl.game.SearchingForGameScreen
import com.kroune.nineMensMorrisApp.viewModel.impl.WelcomeViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.auth.SignInViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.auth.SignUpViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.auth.ViewAccountViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameWithBotViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameWithFriendViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.OnlineGameViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.SearchingForGameViewModel
import com.kroune.nineMensMorrisLib.Position
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
            "com.kroune.nineMensMorrisLib",
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
                    val vm: WelcomeViewModel = hiltViewModel()
                    RenderWelcomeScreen(
                        accountId = vm.accountId.value,
                        checkJwtToken = suspend { vm.checkJwtToken().getOrThrow() },
                        hasJwtToken = { vm.hasJwtToken() },
                        resources = resources,
                        navController = navController
                    )
                }
                composable<Navigation.GameWithBot> {
                    val vm = remember {
                        GameWithBotViewModel(
                            onGameEnd = { navController.navigate(Navigation.GameEnd(it)) }
                        )
                    }
                    RenderGameWithBotScreen(
                        pos = vm.gameBoard.pos.value,
                        selectedButton = vm.gameBoard.selectedButton.value,
                        moveHints = vm.gameBoard.moveHints,
                        onClick = { vm.gameBoard.onClick(it) },
                        handleUndo = { vm.gameBoard.handleUndo() },
                        handleRedo = { vm.gameBoard.handleRedo() },
                    )
                }
                composable<Navigation.GameWithFriend> {
                    val vm = remember {
                        GameWithFriendViewModel(
                            onGameEnd = {
                                navController.navigate(Navigation.GameEnd(this.pos.value))
                            }
                        )
                    }
                    RenderGameWithFriendScreen(
                        vm.pos.value,
                        vm.selectedButton.value,
                        vm.moveHints,
                        { vm.onClick(it) },
                        { vm.handleUndo() },
                        { vm.handleRedo() },
                        vm.positions.value,
                        vm.depth.value,
                        { vm.increaseDepth() },
                        { vm.decreaseDepth() },
                        { vm.startAnalyze() }
                    )
                }
                composable<Navigation.GameEnd>(
                    typeMap = mapOf(typeOf<Position>() to PositionNavType())
                ) {
                    val pos = it.toRoute<Navigation.GameEnd>().position
                    RenderGameEnd(pos = pos) {
                        navController.navigateSingleTopTo(Navigation.Welcome)
                    }
                }
                composable<Navigation.SignUp>(
                    typeMap = mapOf(typeOf<Navigation>() to NavigationNavType())
                ) {
                    val nextRoute = remember {
                        it.toRoute<Navigation.SignIn>().nextRoute
                    }
                    val vm: SignUpViewModel = hiltViewModel()
                    if (vm.authResult.value is AuthResults.Success) {
                        if (nextRoute is Navigation.ViewAccount) {
                            navController.navigateSingleTopTo(Navigation.ViewAccount(vm.accountId!!))
                        } else {
                            navController.navigateSingleTopTo(nextRoute)
                        }
                    }
                    RenderSignUpScreen(
                        loginValidator = {
                            vm.loginValidator(it)
                        },
                        passwordValidator = { password: String ->
                            vm.passwordValidator(password)
                        },
                        onRegister = { login: String, password: String ->
                            vm.register(login, password)
                        },
                        navController = navController,
                        resources = resources,
                        nextRoute = nextRoute,
                        authResult = vm.authResult.value
                    )
                }
                composable<Navigation.SignIn>(
                    typeMap = mapOf(typeOf<Navigation>() to NavigationNavType())
                ) {
                    val nextRoute = remember {
                        it.toRoute<Navigation.SignIn>().nextRoute
                    }
                    val vm: SignInViewModel = hiltViewModel()
                    if (vm.authResult.value is AuthResults.Success) {
                        if (nextRoute is Navigation.ViewAccount) {
                            navController.navigateSingleTopTo(Navigation.ViewAccount(vm.accountId!!))
                        } else {
                            navController.navigateSingleTopTo(nextRoute)
                        }
                    }
                    RenderSignInScreen(
                        loginValidator = {
                            vm.loginValidator(it)
                        },
                        passwordValidator = { password: String ->
                            vm.passwordValidator(password)
                        },
                        onLogin = { login: String, password: String ->
                            vm.login(login, password)
                        },
                        navController = navController,
                        resources = resources,
                        nextRoute = nextRoute,
                        authResult = vm.authResult.value
                    )
                }
                composable<Navigation.SearchingOnlineGame> {
                    val vm: SearchingForGameViewModel = hiltViewModel()
                    val id = vm.gameId.collectAsState().value
                    if (id != null) {
                        navController.navigateSingleTopTo(Navigation.OnlineGame(id)) {
                            popUpTo(Navigation.SearchingOnlineGame) {
                                inclusive = true
                            }
                        }
                    }
                    SearchingForGameScreen(resources)
                }
                composable<Navigation.OnlineGame> {
                    val gameId = remember {
                        it.toRoute<Navigation.OnlineGame>().id
                    }
                    val factory = factoryProvider().onlineGameViewModelFactory()
                    val vm: OnlineGameViewModel =
                        viewModel(factory = OnlineGameViewModel.provideFactory(factory, gameId))
                    RenderOnlineGameScreen(
                        pos = vm.gameBoard.pos.value,
                        selectedButton = vm.gameBoard.selectedButton.value,
                        moveHints = vm.gameBoard.moveHints,
                        onClick = {
                            vm.gameBoard.onClick(it)
                        },
                        handleUndo = { },
                        handleRedo = { },
                        onGiveUp = { vm.giveUp() },
                        gameEnded = vm.gameEnded.value,
                        isGreen = vm.isGreen.value,
                        navController = navController
                    )
                }
                composable<Navigation.AppStartAnimation> {
                    AppStartAnimationScreen() {
                        navController.navigate(Navigation.Welcome)
                    }
                }
                composable<Navigation.ViewAccount> {
                    val id = remember {
                        it.toRoute<Navigation.ViewAccount>().id
                    }
                    val factory = factoryProvider().viewAccountViewModelFactory()
                    val vm: ViewAccountViewModel =
                        viewModel(factory = ViewAccountViewModel.provideFactory(factory, id))
                    RenderViewAccountScreen(
                        logout = { vm.logout() },
                        navController = navController,
                        ownAccount = vm.ownAccount,
                        accountCreationDate = vm.accountCreationDate.value,
                        accountName = vm.accountName.value,
                        pictureByteArray = vm.pictureByteArray.value,
                        accountRating = vm.accountRating.value
                    )
                }
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
