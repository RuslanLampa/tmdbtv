package com.tmdb.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

val LocalCustomColorsPalette = staticCompositionLocalOf { TMDBColorsDefault() }

val MaterialTheme.tmdbColors: TMDBColorsDefault
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorsPalette.current

sealed class TMDBAppTheme {
    data object Default : TMDBAppTheme()

    val palette: TMDBColorsDefault
        get() = TMDBColorsDefault(
            darkColorScheme()
        )
}