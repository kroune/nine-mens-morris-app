package com.kroune.nineMensMorrisApp.viewModel.interfaces

import androidx.lifecycle.ViewModel
import com.kroune.nineMensMorrisApp.data.local.interfaces.DataI

/**
 * model for our view models
 */
abstract class ViewModelI : ViewModel() {

    /**
     * data we need
     */
    open val data: DataI
        get() = notImplemented()
}

/**
 * we want to have a standard names for some properties, so we create an interface
 * however it isn't always needed, so this can be a default value
 */
fun notImplemented(): Nothing = error("something wasn't initialized, see notImplemented() function")
