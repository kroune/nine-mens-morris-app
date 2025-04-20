package io.github.kroune.nine_mens_morris_kmp_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.io.IOException

@Composable
fun getScreenIntSize(): IntSize {
    with(LocalDensity.current) {
        return IntSize(
            LocalConfiguration.current.screenWidthDp.dp.roundToPx(),
            LocalConfiguration.current.screenHeightDp.dp.roundToPx()
        )
    }
}

fun <T> Result<T>.recoverNativeNetworkError(networkException: T): Result<T> {
    return recoverCatching {
        if (it is java.io.IOException || it is java.nio.channels.UnresolvedAddressException)
            return@recoverCatching networkException
        throw it
    }
}

@Composable
fun getScreenDpSize(): DpSize {
    with(
        LocalDensity.current
    ) {
        return DpSize(
            getScreenIntSize().width.toDp(),
            getScreenIntSize().height.toDp()
        )
    }
}

fun <T> Result<T>.recoverNetworkError(networkException: T): Result<T> {
    return recoverCatching {
        if (it is IOException)
            return@recoverCatching networkException
        throw it
    }.recoverNativeNetworkError(networkException)
}
