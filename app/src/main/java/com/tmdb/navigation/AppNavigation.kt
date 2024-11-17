package com.tmdb.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tmdb.common.navigation.NavRoute
import com.tmdb.ui.screen.details.DetailsScreen
import com.tmdb.ui.screen.details.DetailsViewModel
import com.tmdb.ui.screen.home.HomeScreen
import com.tmdb.ui.screen.player.PlayerScreen
import com.tmdb.ui.screen.player.PlayerViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.HomeNavRoute
    ) {
        composable<NavRoute.HomeNavRoute> {
            HomeScreen {
                navController.navigate(it)
            }
        }

        composable<NavRoute.DetailNavRoute> { entry ->
            val detail = entry.toRoute<NavRoute.DetailNavRoute>()

            val viewModel =
                hiltViewModel<DetailsViewModel, DetailsViewModel.DetailViewModelFactory> {
                    it.create(detail.movieId)
                }

            DetailsScreen(
                viewModel = viewModel,
                navigateTo = { navController.navigate(it) })
        }

        composable<NavRoute.PlayerNavRoute> { entry ->
            val detail = entry.toRoute<NavRoute.PlayerNavRoute>()

            val viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.PlayerViewModelFactory> {
                it.create(detail.movieId)
            }

            PlayerScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() })
        }
    }
}