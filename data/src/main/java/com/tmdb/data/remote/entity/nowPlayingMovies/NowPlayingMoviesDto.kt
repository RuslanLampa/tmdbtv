package com.tmdb.data.remote.entity.nowPlayingMovies

import com.google.gson.annotations.SerializedName
import com.tmdb.data.remote.entity.common.MovieDto

data class NowPlayingMoviesDto(
    @SerializedName("dates")
    val dates: DatesDto? = null,

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<MovieDto>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
)