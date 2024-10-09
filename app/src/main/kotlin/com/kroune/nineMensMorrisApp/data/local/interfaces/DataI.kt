package com.kroune.nineMensMorrisApp.data.local.interfaces

/**
 * interface for all data files
 */
abstract class DataI : DataInterface {
    init {
        this.invokeBackend()
    }
}

/**
 * model we use for all data providing classes
 */
private interface DataInterface {

    /**
     * invokes backend part
     * launched with coroutine
     */
    fun invokeBackend() {}
}
