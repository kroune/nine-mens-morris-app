package com.kroune.nineMensMorrisApp

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameEndViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameWithBotViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.GameWithFriendViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.OnlineGameViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.SearchingForGameViewModel
import com.kroune.nineMensMorrisLib.Position
import kotlin.reflect.typeOf

@Composable
fun NavHost(context: Context) {
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
                checkJwtToken = suspend { vm.checkJwtToken() },
                hasJwtToken = { vm.hasJwtToken() },
                resources = context.resources,
                navController = navController,
                vm.hasSeen,
                { vm.hasSeen = true }
            )
        }
        composable<Navigation.GameWithBot> {
            val vm = remember {
                GameWithBotViewModel(
                    onGameEnd = {
                        navController.navigate(
                            Navigation.GameEnd(
                                it,
                                this.movesHistory
                            )
                        )
                    }
                )
            }
            RenderGameWithBotScreen(
                pos = vm.gameBoard.pos.value,
                selectedButton = vm.gameBoard.selectedButton.value,
                moveHints = vm.gameBoard.moveHints.value,
                onClick = { vm.gameBoard.onClick(it) },
                handleUndo = { vm.gameBoard.handleUndo() },
                handleRedo = { vm.gameBoard.handleRedo() },
            )
        }
        composable<Navigation.GameWithFriend> {
            val vm = remember {
                GameWithFriendViewModel(
                    onGameEnd = {
                        navController.navigate(
                            Navigation.GameEnd(
                                this.pos.value,
                                this.movesHistory
                            )
                        )
                    }
                )
            }
            RenderGameWithFriendScreen(
                vm.pos.value,
                vm.selectedButton.value,
                vm.moveHints.value,
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
            typeMap = mapOf(
                typeOf<Position>() to PositionNavType(),
                typeOf<List<Position>>() to ListPositionNavType()
            )
        ) {
            val pos = it.toRoute<Navigation.GameEnd>().position
            val movesHistory = it.toRoute<Navigation.GameEnd>().movesHistory
            val vm: GameEndViewModel = remember {
                GameEndViewModel(pos, movesHistory)
            }
            RenderGameEnd(
                pos = vm.pos.value,
                { navController.navigateSingleTopTo(Navigation.Welcome) },
                { vm.handleUndo() },
                { vm.handleRedo() }
            )
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
                    navController.navigateSingleTopTo(Navigation.ViewAccount(vm.accountId!!)) {
                        popUpTo(Navigation.SignUp(nextRoute)) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigateSingleTopTo(nextRoute) {
                        popUpTo(Navigation.SignUp(nextRoute)) {
                            inclusive = true
                        }
                    }
                }
            }
            RenderSignUpScreen(
                loginValidator = { vm.loginValidator(it) },
                passwordValidator = { password: String ->
                    vm.passwordValidator(password)
                },
                onRegister = { login: String, password: String ->
                    vm.register(login, password)
                },
                navController = navController,
                resources = context.resources,
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
                    navController.navigateSingleTopTo(Navigation.ViewAccount(vm.accountId!!)) {
                        popUpTo(Navigation.SignIn(nextRoute)) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigateSingleTopTo(nextRoute) {
                        popUpTo(Navigation.SignIn(nextRoute)) {
                            inclusive = true
                        }
                    }
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
                resources = context.resources,
                nextRoute = nextRoute,
                authResult = vm.authResult.value
            )
        }
        composable<Navigation.SearchingOnlineGame> {
            val vm: SearchingForGameViewModel = hiltViewModel()
            val id = vm.gameId.value
            val expectedWaitingTime = vm.expectedWaitingTime.value
            if (id != null) {
                navController.navigateSingleTopTo(Navigation.OnlineGame(id)) {
                    popUpTo(Navigation.SearchingOnlineGame) {
                        inclusive = true
                    }
                }
            }
            SearchingForGameScreen(expectedWaitingTime, context.resources)
        }
        composable<Navigation.OnlineGame> {
            val gameId = remember {
                it.toRoute<Navigation.OnlineGame>().id
            }
            val vm: OnlineGameViewModel = hiltViewModel()
            vm.setVariables(gameId)
            vm.init()
            if (vm.gameEnded.value) {
                navController.navigateSingleTopTo(
                    Navigation.GameEnd(
                        vm.pos.value,
                        vm.movesHistory
                    )
                )
            }
            RenderOnlineGameScreen(
                pos = vm.pos.value,
                selectedButton = vm.selectedButton.value,
                moveHints = vm.moveHints.value,
                onClick = {
                    vm.onClick(it)
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