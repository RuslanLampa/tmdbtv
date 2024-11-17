package com.tmdb.ui.screen.details

import com.tmdb.common.utils.ErrorModel
import com.tmdb.domain.model.movie.Movie
import com.tmdb.ui.data.MovieItem

data class DetailScreenUiState(
    val status: DetailScreenStatus = DetailScreenStatus.Loading,
    val movieItem: MovieItem? = null,
    val isFavorite: Boolean = false,
    val movie: Movie? = null
)

sealed class DetailScreenStatus {
    data object Loading : DetailScreenStatus()
    data class Error(val error: ErrorModel) : DetailScreenStatus()
    data object Content : DetailScreenStatus()
}