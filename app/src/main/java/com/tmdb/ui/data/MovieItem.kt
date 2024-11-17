package com.tmdb.ui.data

data class MovieItem(
    val id: Int,
    val title: String,
    val posterImageUrl: String,
    val description: String? = null,
    val genres: String? = null,
    val backdropImageUrl: String? = null,
    val releaseDate: String? = null,
    val voteAverage: String? = null
)