package com.tmdb.widget.loadingContainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.tv.material3.MaterialTheme
import com.tmdb.theme.tmdbColors

@Composable
fun LoadingContainer() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.tmdbColors.background), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.tmdbColors.accent)
    }
}