package io.github.kroune.nine_mens_morris_kmp_app.screen.other.viewOwnAccountScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import io.github.kroune.nine_mens_morris_kmp_app.R
import io.github.kroune.nine_mens_morris_kmp_app.domain.entities.api.UploadPictureApiResponses

@Composable
fun HandleOwnAccountScreenError(
    uploadingNewPicture: UploadPictureApiResponses?,
    snackbarHostState: SnackbarHostState
) {
    val text = when (uploadingNewPicture) {
        is UploadPictureApiResponses.Success -> {
            stringResource(R.string.image_was_updated)
        }

        is UploadPictureApiResponses.ServerError -> {
            stringResource(R.string.server_error)
        }

        is UploadPictureApiResponses.NetworkError -> {
            stringResource(R.string.network_error)
        }

        is UploadPictureApiResponses.CredentialsError -> {
            stringResource(R.string.credentials_error)
        }

        is UploadPictureApiResponses.TooLargeImage -> {
            stringResource(
                R.string.image_too_large,
                uploadingNewPicture.maxWidth,
                uploadingNewPicture.maxHeight
            )
        }

        is UploadPictureApiResponses.UnknownError -> {
            stringResource(R.string.unknown_error)
        }

        null -> return
    }
    LaunchedEffect(uploadingNewPicture) {
        snackbarHostState.showSnackbar(text)
    }
}
