package com.kroune.nineMensMorrisApp.di

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryImpl
import com.kroune.nineMensMorrisApp.data.remote.auth.AuthRepositoryI
import com.kroune.nineMensMorrisApp.data.remote.auth.AuthRepositoryImpl
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryImpl
import com.kroune.nineMensMorrisApp.data.remote.game.GameRepositoryI
import com.kroune.nineMensMorrisApp.ui.impl.auth.SignInScreen
import com.kroune.nineMensMorrisApp.ui.impl.auth.ViewAccountScreen
import com.kroune.nineMensMorrisApp.viewModel.impl.auth.ViewAccountViewModel
import com.kroune.nineMensMorrisApp.viewModel.impl.game.OnlineGameViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * application module
 * used for di
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    /**
     * auth repository
     * @see SignInScreen
     */
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepositoryI {
        return AuthRepositoryImpl()
    }

    /**
     * general account info
     * @see ViewAccountScreen
     */
    @Provides
    @Singleton
    fun provideAccountInfoRepository(): AccountInfoRepositoryI {
        return AccountInfoRepositoryImpl()
    }

    /**
     * general account info
     * @see ViewAccountScreen
     */
    @Provides
    @Singleton
    fun provideGameRepository(): GameRepositoryI {
        return GameRepositoryImpl()
    }
}

/**
 * used for di
 * provides factories needed for using [@AssistedInject]
 */
@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    /**
     * @return [ViewAccountViewModel.AssistedVMFactory]
     */
    fun viewAccountViewModelFactory(): ViewAccountViewModel.AssistedVMFactory

    /**
     * @return [OnlineGameViewModel.AssistedVMFactory]
     */
    fun onlineGameViewModelFactory(): OnlineGameViewModel.AssistedVMFactory
}

/**
 * provides factories installed in [ActivityComponent]
 */
@Composable
fun factoryProvider(): ViewModelFactoryProvider {
    return EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    )
}
