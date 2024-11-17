package com.tmdb.data.remote.entity.movieDetails

import com.google.gson.annotations.SerializedName

data class SpokenLanguageDto(
    @SerializedName("english_name")
    val englishName: String? = null,

    @SerializedName("iso_639_1")
    val iso: String? = null,

    @SerializedName("name")
    val name: String? = null
)