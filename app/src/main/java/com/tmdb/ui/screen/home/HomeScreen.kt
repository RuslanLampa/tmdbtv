package com.tmdb.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import com.tmdb.common.R
import com.tmdb.common.navigation.NavRoute
import com.tmdb.widget.buttons.FocusableButton
import com.tmdb.widget.errorContainer.ErrorContainer
import com.tmdb.widget.loadingContainer.LoadingContainer
import com.tmdb.widget.movieCard.MovieCard
import com.tmdb.theme.tmdbColors
import com.tmdb.ui.data.FilterItem
import com.tmdb.ui.data.MovieItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateTo: (NavRoute) -> Unit
) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    when (screenState.status) {
        is HomeScreenStatus.Error -> {
            ErrorContainer {
                viewModel.onEvent(HomeScreenUiEvent.Refresh)
            }
        }

        is HomeScreenStatus.Loading -> {
            LoadingContainer()
        }

        is HomeScreenStatus.Content -> {
            Content(
                filters = screenState.filters,
                selectedFilter = screenState.selectedFilter,
                movies = screenState.movies,
                onEvent = viewModel::onEvent,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun Content(
    filters: List<FilterItem>,
    selectedFilter: FilterItem,
    movies: List<MovieItem>,
    onEvent: (HomeScreenUiEvent) -> Unit,
    navigateTo: (NavRoute) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.tmdbColors.background)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(18.dp)
                    .padding(end = 8.dp)
            )

            filters.forEach { f ->
                val isSelected = f == selectedFilter
                FocusableButton(
                    text = stringResource(id = f.titleRes),
                    focusRequester = if (isSelected)
                        focusRequester else FocusRequester(),
                    isSelected = isSelected,
                ) {
                    onEvent(HomeScreenUiEvent.OnFilterButtonClick(f))
                }
            }
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movieItem ->
                MovieCard(
                    imageUrl = movieItem.posterImageUrl,
                    onClick = { navigateTo(NavRoute.DetailNavRoute(movieItem.id)) }
                )
            }
        }
    }
}