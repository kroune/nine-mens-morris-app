package com.kroune.nineMensMorrisApp.common

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * parallelogram shape
 */
class ParallelogramShape(
    private val bottomLineLeftOffset: Float = 0f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val x = size.width
                val y = size.height

                // top line
                moveTo(0f, 0f)
                lineTo(x, 0f)
                // bottom line
                lineTo(x, y)
                lineTo(x - bottomLineLeftOffset, y)
            }
        )
    }
}
