package com.kroune.nineMensMorrisApp.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.move.Movement
import java.util.Stack

/**
 * provides a way to get an element from pair
 * fancy way
 * @param T any type
 * @param index index of the required element
 */
operator fun <T> Triple<T, T, T>.get(index: Int): T {
    return when (index) {
        0 -> {
            first
        }

        1 -> {
            second
        }

        2 -> {
            third
        }

        else -> {
            throw IllegalArgumentException("Illegal index when getting triple element")
        }
    }
}

/**
 * converts all movements to positions
 */
@Suppress("unused")
fun List<Movement>.toPositions(startPos: Position): List<Position> {
    if (this.isEmpty()) {
        return mutableListOf()
    }
    var startPosCopy = startPos
    val result: MutableList<Position> = mutableListOf(startPos)
    this@toPositions.asReversed().forEach {
        startPosCopy = it.producePosition(startPosCopy)
        result.add(startPosCopy)
    }
    return result
}

/**
 * adds a basic background
 * @param function everything ui-related that happens inside of the app
 */
@Composable
inline fun AppTheme(function: BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E7E7E))
    ) {
        function()
    }
}

/**
 * converts list to stack
 * [1, 2, 3, 4]
 * Stack(1, 2, 3, 4)
 */
fun <T> List<T>.toStack(): Stack<T> {
    val stack = Stack<T>()
    this.forEach {
        stack.push(it)
    }
    return stack
}
