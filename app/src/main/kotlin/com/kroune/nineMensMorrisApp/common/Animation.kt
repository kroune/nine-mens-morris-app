package com.kroune.nineMensMorrisApp.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

/**
 * a simple infinite loading animation
 */
@Composable
fun LoadingCircle() {
    val animatedProgress by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    CircularProgressIndicator(
        progress = { animatedProgress },
        gapSize = 0.dp
    )
}
