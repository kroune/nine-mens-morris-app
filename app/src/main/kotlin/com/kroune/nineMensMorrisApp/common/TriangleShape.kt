package com.kroune.nineMensMorrisApp.common

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * @return triangle shape
 * used, because [triangleShape] looks better than [TriangleShape()] and looks compose like
 * [CircleShape]
 */
val triangleShape: TriangleShape = TriangleShape()

/**
 * triangle shape
 * looks like this
 * -------
 *   -----
 *     ---
 *       -
 */
class TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val x = size.width
                val y = size.height

                moveTo(0f, 0f)
                lineTo(x, 0f)
                lineTo(x, y)
            }
        )
    }
}
