package com.tmdb.ui.screen.player

import android.net.Uri
import com.tmdb.common.utils.ErrorModel

data class PlayerScreenUiState(
    val status: PlayerScreenStatus = PlayerScreenStatus.Loading,
    val videoUri: Uri? = null
)

sealed class PlayerScreenStatus {
    data object Loading : PlayerScreenStatus()
    data class Error(val error: ErrorModel) : PlayerScreenStatus()
    data object Content : PlayerScreenStatus()
}