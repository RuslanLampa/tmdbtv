package com.tmdb.data.remote.entity.popularMovies

import com.google.gson.annotations.SerializedName
import com.tmdb.data.remote.entity.common.MovieDto

data class PopularMoviesDto(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<MovieDto>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
)