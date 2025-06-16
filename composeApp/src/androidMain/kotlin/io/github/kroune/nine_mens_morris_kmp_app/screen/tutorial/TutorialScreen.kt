package io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderFlyingMovesTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderIndicatorsTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderLoseTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderNormalMovesTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderPlacementTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderRemovalMovesTutorialScreen
import io.github.kroune.nine_mens_morris_kmp_app.screen.tutorial.elements.RenderTriplesTutorialScreen
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource

/**
 * stores order of tutorials (used for slider)
 */
private val tutorialScreens: List<@Composable () -> Unit> = listOf(
    {
        RenderIndicatorsTutorialScreen()
    },
    {
        RenderLoseTutorialScreen()
    },
    {
        RenderPlacementTutorialScreen()
    },
    {
        RenderNormalMovesTutorialScreen()
    },
    {
        RenderFlyingMovesTutorialScreen()
    },
    {
        RenderTriplesTutorialScreen()
    },
    {
        RenderRemovalMovesTutorialScreen()
    }
)

@Composable
fun TutorialScreen() {
    val coroutine = rememberCoroutineScope()
    val page = rememberPagerState(0) { tutorialScreens.size }
    Row(
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
            .zIndex(5f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            coroutine.launch {
                page.animateScrollToPage((page.currentPage + page.pageCount - 1) % page.pageCount)
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.left_arrow), "to the left",
                modifier = Modifier.alpha(0.5f)
            )
        }
        IconButton(onClick = {
            coroutine.launch {
                page.animateScrollToPage((page.currentPage + 1) % page.pageCount)
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.right_arrow), "to the right",
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
    HorizontalPager(page) {
        tutorialScreens[it]()
    }
    Row(
        modifier = Modifier
            .zIndex(5f)
            .fillMaxHeight()
            .width(tutorialScreens.size * 3 * 7.dp)
            .padding(bottom = 50.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        (0..page.pageCount).forEach { index ->
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (page.currentPage == index) Color.Blue else MaterialTheme.colorScheme.primary)
            )
        }
    }
}