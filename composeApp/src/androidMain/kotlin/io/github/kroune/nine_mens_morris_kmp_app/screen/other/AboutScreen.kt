package io.github.kroune.nine_mens_morris_kmp_app.screen.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.component.other.aboutScreenComponent.AboutScreenState
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.event.other.AboutScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.RoundedCornerShape3
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.padding2
import io.github.kroune.nine_mens_morris_kmp_app.screen.UiConstants.shadowElevation2
import androidx.compose.ui.res.painterResource

@Composable
fun AboutScreen(
    onEvent: (AboutScreenEvent) -> Unit,
    state: AboutScreenState
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(padding2)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.about_app),
                    fontWeight = FontWeight.W500,
                    fontSize = 23.sp
                )
                IconButton(
                    {
                        onEvent(AboutScreenEvent.OnBackPressed)
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.close),
                        "close",
                        Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(padding2)
        ) {
            val uriHandler = LocalUriHandler.current
            Surface(
                modifier = Modifier
                    .padding(horizontal = padding2)
                    .fillMaxWidth()
                    .clickable {
                        onEvent(AboutScreenEvent.OnNavigationToReportAnIssue)
                        uriHandler.openUri(state.githubIssue.webLink)
                    },
                shape = RoundedCornerShape3,
                shadowElevation = shadowElevation2
            ) {
                Row(
                    modifier = Modifier
                        .padding(padding2),
                    horizontalArrangement = Arrangement.spacedBy(padding2)
                ) {
                    Icon(
                        painterResource(R.drawable.report),
                        "github",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(stringResource(R.string.report_an_issue))
                }
            }
            Surface(
                modifier = Modifier
                    .padding(horizontal = padding2)
                    .fillMaxWidth()
                    .clickable {
                        onEvent(AboutScreenEvent.OnNavigationToSourceCode)
                        uriHandler.openUri(state.github.webLink)
                    },
                shape = RoundedCornerShape3,
                shadowElevation = shadowElevation2
            ) {
                Row(
                    modifier = Modifier
                        .padding(padding2),
                    horizontalArrangement = Arrangement.spacedBy(padding2)
                ) {
                    Icon(
                        painterResource(R.drawable.github),
                        "github",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(stringResource(R.string.source_code_link))
                }
            }
            Surface(
                modifier = Modifier
                    .padding(horizontal = padding2)
                    .fillMaxWidth()
                    .clickable {
                        onEvent(AboutScreenEvent.OnNavigationToCreatorTelegram)
                        uriHandler.openUri(state.telegram.webLink)
                    },
                shape = RoundedCornerShape3,
                shadowElevation = shadowElevation2
            ) {
                Row(
                    modifier = Modifier
                        .padding(padding2),
                    horizontalArrangement = Arrangement.spacedBy(padding2)
                ) {
                    Icon(
                        painterResource(R.drawable.telegram),
                        "telegram",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(stringResource(R.string.creator_telegram_link))
                }
            }
        }
    }
}