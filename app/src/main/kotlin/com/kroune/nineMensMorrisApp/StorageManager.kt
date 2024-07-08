package com.kroune.nineMensMorrisApp

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * simple storage manager
 * lifecycle = app lifecycle
 */
object StorageManager {
    /**
     * shared preference for the app
     */
    lateinit var sharedPreferences: SharedPreferences

    /**
     * puts string into the sharedPreferences
     */
    fun putString(key: String, value: String?) {
        sharedPreferences.edit {
            this.putString(key, value)
        }
    }

    /**
     * gets string from sharedPreferences
     */
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * puts long into the sharedPreferences
     */
    fun putLong(key: String, value: Long?) {
        sharedPreferences.edit {
            if (value == null) {
                this.remove(key)
            } else {
                this.putLong(key, value)
            }
        }
    }

    /**
     * gets long from sharedPreferences
     */
    fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, -1L)
        if (value == -1L) {
            return null
        }
        return value
    }
}
