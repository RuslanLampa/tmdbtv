package com.tmdb.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.tv.material3.MaterialTheme

@Composable
fun TMDBTheme(theme: TMDBAppTheme = TMDBAppTheme.Default, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalCustomColorsPalette provides theme.palette) {
        MaterialTheme(
            colorScheme = LocalCustomColorsPalette.current.material,
            shapes = MaterialTheme.shapes,
            typography = Typography,
            content = content
        )
    }
}