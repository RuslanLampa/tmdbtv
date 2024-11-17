package com.tmdb.ui.screen.home

import com.tmdb.ui.data.FilterItem

sealed class HomeScreenUiEvent {
    data class OnFilterButtonClick(val filter: FilterItem) : HomeScreenUiEvent()
    data object Refresh : HomeScreenUiEvent()
}