package com.kroune.nineMensMorrisApp.viewModel.impl.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kroune.nineMensMorrisApp.data.remote.Common.networkScope
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * view model for viewing account
 */
class ViewAccountViewModel @AssistedInject constructor(
    private val accountInfoRepositoryI: AccountInfoRepositoryI,
    private val authRepositoryI: AccountInfoRepositoryI,
    /**
     * account id
     */
    @Assisted val id: Long
) : ViewModelI() {

    /**
     * if it is our own account
     */
    val ownAccount = accountInfoRepositoryI.accountIdState.value == id

    /**
     * factory for [AssistedInject]
     */
    @AssistedFactory
    interface AssistedVMFactory {
        /**
         * creates [ViewAccountViewModel] using id
         */
        fun create(id: Long): ViewAccountViewModel
    }

    companion object {
        /**
         * provides factory
         */
        fun provideFactory(
            assistedVMFactory: AssistedVMFactory, id: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST") return assistedVMFactory.create(id) as T
            }
        }
    }

    /**
     * account name or null if it is still loading
     */
    val accountName = mutableStateOf<String?>(null)

    /**
     * file with account picture or null if it is still loading
     */
    val pictureByteArray = mutableStateOf<ByteArray?>(null)

    /**
     * account creation date or null if it is still loading
     */
    val accountCreationDate = mutableStateOf<String?>(null)

    /**
     * account rating or null if it is still loading
     */
    val accountRating = mutableStateOf<Long?>(null)

    /**
     * updates [accountRating]
     */
    fun getRatingById() {
        if (accountRating.value != null) {
            return
        }
        CoroutineScope(networkScope).launch {
            val requestResult =
                accountInfoRepositoryI.getAccountRatingById(id).getOrNull() ?: return@launch
            accountRating.value = requestResult
        }
    }

    /**
     * updates [accountCreationDate]
     */
    fun getLoginById() {
        if (accountCreationDate.value != null) {
            return
        }
        CoroutineScope(networkScope).launch {
            val requestResult =
                accountInfoRepositoryI.getAccountDateById(id).getOrNull() ?: return@launch
            val finalString =
                "${requestResult.first}-${requestResult.second}-${requestResult.third}"
            accountCreationDate.value = finalString
        }
    }

    /**
     * updates [pictureByteArray]
     */
    fun getProfilePicture() {
        if (pictureByteArray.value != null) {
            return
        }
        CoroutineScope(networkScope).launch {
            val buffer =
                accountInfoRepositoryI.getAccountPictureById(id).getOrNull() ?: return@launch
            pictureByteArray.value = buffer
        }
    }

    /**
     * updates [accountName]
     */
    fun getProfileName() {
        if (accountName.value != null) {
            return
        }
        CoroutineScope(networkScope).launch {
            val name = accountInfoRepositoryI.getAccountNameById(id).getOrNull() ?: return@launch
            accountName.value = name
        }
    }

    /**
     * logs out of the account
     */
    fun logout() {
        authRepositoryI.logout()
    }

    init {
        getRatingById()
        getProfilePicture()
        getLoginById()
        getProfileName()
    }
}
