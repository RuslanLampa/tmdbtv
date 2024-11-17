package com.tmdb.ui.data

import androidx.annotation.StringRes
import com.tmdb.common.R

sealed class FilterItem(@StringRes val titleRes: Int) {
    data object Popular : FilterItem(R.string.popular)
    data object NowPlaying : FilterItem(R.string.now_playing)
    data object MyFavorites : FilterItem(R.string.my_favorites)
}