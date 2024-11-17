package com.tmdb.theme

import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.darkColorScheme

data class TMDBColorsDefault(
    val material: ColorScheme = darkColorScheme()
) {
    val primary: Color get() = Primary
    val onPrimary: Color get() = OnPrimary
    val secondary: Color get() = Secondary
    val onSecondary: Color get() = OnSecondary
    val accent: Color get() = Accent
    val background: Color get() = Background
    val text: Color get() = Text
    val textCaption: Color get() = TextCaption
}