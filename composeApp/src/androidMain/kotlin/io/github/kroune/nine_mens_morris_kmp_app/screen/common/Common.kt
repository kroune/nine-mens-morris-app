package io.github.kroune.nine_mens_morris_kmp_app.screen.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.AccountPictureByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.CreationDateByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.LoginByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.RatingByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.RoundedCornerShape3
import io.github.kroune.nine_mens_morris_kmp_app.screen.theme.ExtendedColorTheme
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * draws user rating
 */
@Composable
fun DrawRating(
    modifier: Modifier = Modifier
        .size(120.dp, 30.dp),
    onReload: () -> Unit,
    onSuccess: @Composable BoxScope.(Long) -> Unit = @Composable {
        Text(text = it.toString(), overflow = TextOverflow.Ellipsis)
    },
    onLoading: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .matchParentSize()
                .shimmerLoading()
        ) {
        }
    },
    onFailure: @Composable BoxScope.() -> Unit = {
        IconButton(
            onClick = {
                onReload()
            },
            modifier = Modifier
                .matchParentSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.error),
                contentDescription = "Error",
                modifier = Modifier
            )
        }
    },
    accountRating: RatingByIdApiResponses?,
    snackbarHostState: SnackbarHostState,
) {
    AnimatedContent(
        accountRating,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(3000)
            ) togetherWith fadeOut(animationSpec = tween(3000))
        },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier
                .clip(RoundedCornerShape(10))
        ) {
            when (it) {
                null -> {
                    onLoading()
                }

                is RatingByIdApiResponses.Success -> {
                    onSuccess(it.rating)
                }

                else -> {
                    onFailure()
                    val errorText = when (it) {
                        is RatingByIdApiResponses.NetworkError -> {
                            stringResource(R.string.network_error)
                        }

                        is RatingByIdApiResponses.UnknownError -> {
                            stringResource(R.string.unknown_error)
                        }

                        is RatingByIdApiResponses.CredentialsError -> {
                            stringResource(R.string.credentials_error)
                        }

                        is RatingByIdApiResponses.ServerError -> {
                            stringResource(R.string.server_error)
                        }

                        is RatingByIdApiResponses.Success -> return@Box
                    }
                    var retryText = stringResource(R.string.retry)
                    LaunchedEffect(it) {
                        snackbarHostState.showSnackbar(errorText, retryText)
                            .let { snackbarResult ->
                                if (snackbarResult == SnackbarResult.ActionPerformed) {
                                    onReload()
                                }
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawAccountCreationDate(
    modifier: Modifier = Modifier
        .size(120.dp, 30.dp),
    onReload: () -> Unit,
    onSuccess: @Composable (Triple<Int, Int, Int>) -> Unit = @Composable { (day, week, month) ->
        Text("$day.$week.$month")
    },
    onLoading: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .matchParentSize()
                .shimmerLoading()
        ) {}
    },
    onFailure: @Composable BoxScope.() -> Unit = {
        IconButton(onClick = {
            onReload()
        }) {
            Icon(
                painter = painterResource(R.drawable.error),
                contentDescription = "Error",
                modifier = Modifier
                    .matchParentSize()
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
        }
    },
    accountCreationDate: CreationDateByIdApiResponses?,
    snackbarHostState: SnackbarHostState,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(10))
    ) {
        when (accountCreationDate) {
            null -> {
                onLoading()
            }

            is CreationDateByIdApiResponses.Success -> {
                onSuccess(accountCreationDate.creationDate)
            }

            else -> {
                onFailure()
                val exceptionText: String = when (accountCreationDate) {
                    is CreationDateByIdApiResponses.NetworkError -> {
                        stringResource(R.string.network_error)
                    }

                    is CreationDateByIdApiResponses.UnknownError -> {
                        stringResource(R.string.unknown_error)
                    }

                    is CreationDateByIdApiResponses.CredentialsError -> {
                        stringResource(R.string.credentials_error)
                    }

                    is CreationDateByIdApiResponses.ServerError -> {
                        stringResource(R.string.server_error)
                    }

                    is CreationDateByIdApiResponses.Success -> return@Box
                }
                val retryText = stringResource(R.string.retry)
                LaunchedEffect(accountCreationDate) {
                    snackbarHostState.showSnackbar(exceptionText, retryText).let {
                        if (it == SnackbarResult.ActionPerformed) {
                            onReload()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.shimmerLoading(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "",
    )

    val shimmerColor = ExtendedColorTheme.colorScheme.shimmerColor
    return drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    shimmerColor.copy(alpha = 0.2f),
                    shimmerColor.copy(alpha = 1.0f),
                    shimmerColor.copy(alpha = 0.2f),
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
            )
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DrawIcon(
    modifier: Modifier = Modifier,
    onReload: () -> Unit,
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    onSuccess: @Composable BoxScope.(ByteArray) -> Unit = @Composable {
        Image(
            bitmap = it.decodeToImageBitmap(),
            contentDescription = "Profile icon",
            modifier = Modifier
                .clickable { onClick() }
                .matchParentSize()
                .aspectRatio(1f, true)
        )
    },
    onLoading: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .matchParentSize()
                .aspectRatio(1f, true)
                .shimmerLoading()
        ) {
        }
    },
    onFailure: @Composable BoxScope.() -> Unit = {
        IconButton(
            onClick = {
                onReload()
            },
            modifier = Modifier
                .matchParentSize()
                .aspectRatio(1f, true)
        ) {
            Icon(
                painter = painterResource(R.drawable.error),
                contentDescription = "Error",
                modifier = Modifier
            )
        }
    },
    pictureByteArray: AccountPictureByIdApiResponses?,
    snackbarHostState: SnackbarHostState,
) {
    AnimatedContent(
        pictureByteArray,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(500)
            ) togetherWith fadeOut(animationSpec = tween(450))
        }
    ) {
        Box(
            modifier
                .clip(shape)
        ) {
            when (it) {
                null -> {
                    onLoading()
                }

                is AccountPictureByIdApiResponses.Success -> {
                    onSuccess(it.picture)
                }

                else -> {
                    onFailure()
                    val text: String = when (it) {
                        is AccountPictureByIdApiResponses.NetworkError -> {
                            stringResource(R.string.network_error)
                        }

                        is AccountPictureByIdApiResponses.UnknownError -> {
                            stringResource(R.string.unknown_error)
                        }

                        is AccountPictureByIdApiResponses.CredentialsError -> {
                            stringResource(R.string.credentials_error)
                        }

                        is AccountPictureByIdApiResponses.ServerError -> {
                            stringResource(R.string.server_error)
                        }

                        is AccountPictureByIdApiResponses.Success -> return@Box
                    }
                    val retryText = stringResource(R.string.retry)
                    LaunchedEffect(pictureByteArray) {
                        snackbarHostState.showSnackbar(text, retryText).let { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                onReload()
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * draws user name or loading animation
 */
@Composable
fun DrawName(
    modifier: Modifier = Modifier,
    onReload: () -> Unit,
    onSuccess: @Composable BoxScope.(String) -> Unit = @Composable {
        Text(
            it,
            overflow = TextOverflow.Ellipsis
        )
    },
    onLoading: @Composable BoxScope.() -> Unit = {
        Box(
            Modifier
                .clip(RoundedCornerShape3)
                .matchParentSize()
                .shimmerLoading()
        ) {
        }
    },
    onFailure: @Composable BoxScope.() -> Unit = {
        IconButton(onClick = {
            onReload()
        }) {
            Icon(
                painter = painterResource(R.drawable.error),
                contentDescription = "Error",
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
        }
    },
    accountName: LoginByIdApiResponses?,
    snackbarHostState: SnackbarHostState,
) {
    Box(modifier) {
        when (accountName) {
            null -> {
                onLoading()
            }

            is LoginByIdApiResponses.Success -> {
                onSuccess(accountName.login)
            }

            else -> {
                onFailure()
                val exceptionText: String = when (accountName) {
                    is LoginByIdApiResponses.Success -> return@Box

                    is LoginByIdApiResponses.NetworkError -> {
                        stringResource(R.string.network_error)
                    }

                    is LoginByIdApiResponses.CredentialsError -> {
                        stringResource(R.string.credentials_error)
                    }

                    is LoginByIdApiResponses.ServerError -> {
                        stringResource(R.string.server_error)
                    }

                    is LoginByIdApiResponses.UnknownError -> {
                        stringResource(R.string.unknown_error)
                    }
                }
                val retryText = stringResource(R.string.retry)
                LaunchedEffect(accountName) {
                    snackbarHostState.showSnackbar(exceptionText, retryText).let { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            onReload()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackHandler(backHandler: BackHandler, isEnabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val callback =
        remember {
            BackCallback(isEnabled = isEnabled) {
                currentOnBack()
            }
        }
    SideEffect { callback.isEnabled = isEnabled }
    DisposableEffect(backHandler) {
        backHandler.register(callback)
        onDispose { backHandler.unregister(callback) }
    }
}

@Composable
inline fun LimitSize(
    percentage: Float,
    crossinline content: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints {
        Box(
            Modifier
                .sizeIn(
                    maxHeight = this@BoxWithConstraints.maxHeight * percentage,
                    maxWidth = this@BoxWithConstraints.maxWidth * percentage
                )
        ) {
            content()
        }
    }
}

/**
 * a simple infinite loading animation
 */
@Composable
fun LoadingCircle(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000
) {
    val animatedProgress by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .aspectRatio(1f)
    )
}

@Composable
fun <T> StateFlow<T>.collectValue(
    context: CoroutineContext = EmptyCoroutineContext
): T = collectAsState(
    context = context
).value
