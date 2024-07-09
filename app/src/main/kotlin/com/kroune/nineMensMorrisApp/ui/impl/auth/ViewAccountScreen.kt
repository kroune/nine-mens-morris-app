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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kroune.nineMensMorrisApp.Navigation
import com.kroune.nineMensMorrisApp.R
import com.kroune.nineMensMorrisApp.common.AppTheme
import com.kroune.nineMensMorrisApp.common.LoadingCircle
import com.kroune.nineMensMorrisApp.di.factoryProvider
import com.kroune.nineMensMorrisApp.navigateSingleTopTo
import com.kroune.nineMensMorrisApp.ui.interfaces.ScreenModelI
import com.kroune.nineMensMorrisApp.viewModel.impl.auth.ViewAccountViewModel

/**
 * account view screen
 * @param id id of the account
 */
class ViewAccountScreen(
    private val id: Long,
    private val navController: NavHostController
) : ScreenModelI {
    override lateinit var viewModel: ViewAccountViewModel

    @Composable
    override fun InvokeRender() {
        val factory = factoryProvider().viewAccountViewModelFactory()
        viewModel = viewModel(factory = ViewAccountViewModel.provideFactory(factory, id))
        AppTheme {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    DrawIcon()
                    DrawName()
                }
                DrawRating()
                DrawAccountCreationDate()
                if (viewModel.ownAccount) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        DrawOwnAccountOptions()
                    }
                }
            }
        }
    }

    /**
     * draws user rating
     */
    @Composable
    fun DrawRating() {
        val rating = viewModel.accountRating.collectAsState().value
        if (rating == null) {
            Text("User rating is loading...")
        } else {
            Text("User rating is $rating")
        }
    }

    /**
     * draws specific settings for our account
     */
    @Composable
    fun DrawOwnAccountOptions() {
        Button(
            onClick = {
                viewModel.logout()
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
    fun DrawAccountCreationDate() {
        val date = viewModel.accountCreationDate.collectAsState().value
        if (date == null) {
            Text("Account information is loading...")
        } else {
            Text("Account was registered at $date", fontSize = 16.sp)
        }
    }

    /**
     * draws user icon or empty icon
     */
    @Composable
    fun DrawIcon() {
        val byteArray = viewModel.pictureByteArray.collectAsState()
        if (byteArray.value != null) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(byteArray.value!!, 0, byteArray.value!!.size)
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
    fun DrawName() {
        val name = viewModel.accountName.collectAsState().value
        if (name == null) {
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
                Text("$name", fontSize = 20.sp)
            }
        }
    }
}
