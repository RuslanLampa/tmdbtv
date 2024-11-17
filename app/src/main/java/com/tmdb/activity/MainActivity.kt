package com.tmdb.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tmdb.navigation.AppNavigation
import com.tmdb.theme.TMDBTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TMDBTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        val navController = rememberNavController()

        Box(Modifier.fillMaxSize()) {
            AppNavigation(
                navController = navController
            )
        }
    }
}