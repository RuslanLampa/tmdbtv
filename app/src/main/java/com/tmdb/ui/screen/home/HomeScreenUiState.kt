package com.tmdb.ui.screen.home

import com.tmdb.common.utils.ErrorModel
import com.tmdb.ui.data.FilterItem
import com.tmdb.ui.data.MovieItem

data class HomeScreenUiState(
    val status: HomeScreenStatus = HomeScreenStatus.Loading,
    val filters: List<FilterItem>,
    val selectedFilter: FilterItem,
    val movies: List<MovieItem>
) {
    companion object {
        fun empty() = HomeScreenUiState(
            filters = listOf(
                FilterItem.Popular,
                FilterItem.NowPlaying,
                FilterItem.MyFavorites
            ),
            selectedFilter = FilterItem.Popular,
            movies = emptyList()
        )
    }
}

sealed class HomeScreenStatus {
    data object Loading : HomeScreenStatus()
    data class Error(val error: ErrorModel) : HomeScreenStatus()
    data object Content : HomeScreenStatus()
}