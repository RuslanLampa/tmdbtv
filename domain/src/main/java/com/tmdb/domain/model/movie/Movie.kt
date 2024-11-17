package com.tmdb.domain.model.movie

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val description: String? = null,
    val genres: List<String>? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: String? = null
) {
    companion object {
        fun empty(): Movie = Movie(
            id = 0,
            title = "",
            posterPath = ""
        )
    }
}
