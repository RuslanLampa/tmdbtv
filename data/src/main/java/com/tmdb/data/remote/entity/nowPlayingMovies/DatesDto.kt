package com.tmdb.data.remote.entity.nowPlayingMovies

import com.google.gson.annotations.SerializedName

data class DatesDto(
    @SerializedName("maximum")
    val maximum: String,

    @SerializedName("minimum")
    val minimum: String
)