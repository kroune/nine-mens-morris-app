package com.kroune.nineMensMorrisApp.ui.impl.auth

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.LoadingCircle
import com.kroune.nineMensMorrisApp.navigateSingleTopTo

/**
 * Draws viewing account screen
 */
@Composable
fun RenderViewAccountScreen(
    logout: () -> Unit,
    navController: NavHostController,
    ownAccount: Boolean,
    accountCreationDate: String?,
    accountName: String?,
    pictureByteArray: ByteArray?,
    accountRating: Long?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            DrawIcon(pictureByteArray)
            DrawName(accountName)
        }
        DrawRating(accountRating)
        DrawAccountCreationDate(accountCreationDate)
        if (ownAccount) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                DrawOwnAccountOptions(
                    logout = { logout() },
                    navController = navController
                )
            }
        }
    }
}

/**
 * draws user rating
 */
@Composable
fun DrawRating(accountRating: Long?) {
    if (accountRating == null) {
        Text("User rating is loading...")
    } else {
        Text("User rating is $accountRating")
    }
}

/**
 * draws specific settings for our account
 */
@Composable
fun DrawOwnAccountOptions(
    logout: () -> Unit,
    navController: NavHostController
) {
    Button(
        onClick = {
            logout()
            navController.navigateSingleTopTo(Navigation.Welcome)
        }
    ) {
        Text("Log out")
    }
}

/**
 * draws account creation date or info about loading
 */
@Composable
fun DrawAccountCreationDate(accountCreationDate: String?) {
    if (accountCreationDate == null) {
        Text("Account information is loading...")
    } else {
        Text("Account was registered at $accountCreationDate", fontSize = 16.sp)
    }
}

/**
 * draws user icon or empty icon
 */
@Composable
fun DrawIcon(pictureByteArray: ByteArray?) {
    if (pictureByteArray != null) {
        Image(
            bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.size)
                .asImageBitmap(),
            contentDescription = "Profile icon",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
    } else {
        Icon(
            painter = painterResource(R.drawable.baseline_account_circle_48),
            contentDescription = "profile loading icon",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
    }
}

/**
 * draws user name or loading animation
 */
@Composable
fun DrawName(accountName: String?) {
    if (accountName == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingCircle()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("$accountName", fontSize = 20.sp)
        }
    }
}
