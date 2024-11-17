package com.tmdb.ui.screen.details

sealed class DetailsScreenUiEvent {
    data object ToggleFavoriteState: DetailsScreenUiEvent()
    data object Refresh: DetailsScreenUiEvent()
}