package com.kroune.nineMensMorrisApp.ui.interfaces

import androidx.compose.runtime.Composable
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.notImplemented

/**
 * creates interface for screen creation
 */
interface ScreenModelI {

    /**
     * invokes render part
     * launched on the main thread
     */
    @Composable
    fun InvokeRender()

    /**
     * viewModel attached to the screen
     */
    val viewModel: ViewModelI
        get() {
            notImplemented()
        }
}
