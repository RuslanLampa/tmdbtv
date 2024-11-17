package com.tmdb.ui.screen.player

sealed class PlayerScreenUiEvent {
    data object Refresh : PlayerScreenUiEvent()
}