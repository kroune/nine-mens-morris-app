package io.github.kroune.nine_mens_morris_kmp_app.component.other

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import io.github.kroune.nine_mens_morris_kmp_app.component.ComponentContextWithBackHandle
import io.github.kroune.nine_mens_morris_kmp_app.event.other.ViewAccountScreenEvent
import io.github.kroune.nine_mens_morris_kmp_app.interactors.jwtTokenInteractor
import io.github.kroune.nine_mens_morris_kmp_app.model.AccountPictureByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.model.CreationDateByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.model.LoginByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.model.RatingByIdApiResponses
import io.github.kroune.nine_mens_morris_kmp_app.useCases.AccountInfoUseCase

class ViewAccountScreenComponent(
    val onNavigationBack: () -> Unit,
    accountId: Long,
    componentContext: ComponentContext
) : ComponentContext by componentContext, ComponentContextWithBackHandle {

    private val _accountName = mutableStateOf<LoginByIdApiResponses?>(null)
    var accountName by _accountName
    private val _accountRating = mutableStateOf<RatingByIdApiResponses?>(null)
    var accountRating by _accountRating
    private var _accountCreationDate = mutableStateOf<CreationDateByIdApiResponses?>(null)
    var accountCreationDate by _accountCreationDate
    private var _accountPicture = mutableStateOf<AccountPictureByIdApiResponses?>(null)
    var accountPicture by _accountPicture

    private val accountInfoUseCase = AccountInfoUseCase(
        accountId,
        playerInfo = AccountInfoUseCase.PlayerInfo(
            name = _accountName,
            rating = _accountRating,
            creationDate = _accountCreationDate,
            accountPicture = _accountPicture
        )
    )

    fun onEvent(event: ViewAccountScreenEvent) {
        when (event) {
            ViewAccountScreenEvent.Logout -> {
                jwtTokenInteractor.logout()
                onNavigationBack()
            }

            ViewAccountScreenEvent.ReloadCreationDate -> {
                accountInfoUseCase.reloadCreationDate()
            }

            ViewAccountScreenEvent.ReloadIcon -> {
                accountInfoUseCase.reloadPicture()
            }

            ViewAccountScreenEvent.ReloadName -> {
                accountInfoUseCase.reloadName()
            }

            ViewAccountScreenEvent.ReloadRating -> {
                accountInfoUseCase.reloadRating()
            }

            ViewAccountScreenEvent.Back -> {
                onNavigationBack()
            }
        }
    }

    override fun onBackPressed() {
        onEvent(ViewAccountScreenEvent.Back)
    }
}