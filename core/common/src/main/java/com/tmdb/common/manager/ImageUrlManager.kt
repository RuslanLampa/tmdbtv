package com.tmdb.common.manager

import com.tmdb.common.BuildConfig

class ImageUrlManager {
    private val baseUrl = BuildConfig.IMAGE_BASE_URL

    fun getUrl(imagePath: String, size: ImageSize = ImageSize.Original): String {
        return baseUrl + size.value + '/' + imagePath
    }
}

sealed class ImageSize(val value: String) {
    data object W92 : ImageSize("w92")
    data object W154 : ImageSize("w154")
    data object W342 : ImageSize("w342")
    data object W500 : ImageSize("w500")
    data object W780 : ImageSize("w780")
    data object Original : ImageSize("original")
}