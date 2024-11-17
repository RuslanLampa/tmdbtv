package com.tmdb.ui.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Text
import coil3.compose.rememberAsyncImagePainter
import coil3.request.crossfade
import com.tmdb.common.navigation.NavRoute
import com.tmdb.widget.buttons.FocusableImageButton
import com.tmdb.widget.errorContainer.ErrorContainer
import com.tmdb.widget.loadingContainer.LoadingContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import coil3.request.ImageRequest
import coil3.size.Size
import com.tmdb.common.R
import com.tmdb.widget.textWithIcon.TextWithIcon
import com.tmdb.theme.tmdbColors
import com.tmdb.ui.data.MovieItem

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    navigateTo: (NavRoute) -> Unit
) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    when (screenState.status) {
        DetailScreenStatus.Content -> Content(
            movie = screenState.movieItem,
            isFavorite = screenState.isFavorite,
            onEvent = viewModel::onEvent,
            navigateTo = navigateTo
        )

        is DetailScreenStatus.Error -> ErrorContainer {
            viewModel.onEvent(DetailsScreenUiEvent.Refresh)
        }

        DetailScreenStatus.Loading -> LoadingContainer()
    }
}

@Composable
private fun Content(
    movie: MovieItem?,
    isFavorite: Boolean,
    onEvent: (DetailsScreenUiEvent) -> Unit,
    navigateTo: (NavRoute) -> Unit
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movie?.backdropImageUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester = focusRequester)
    ) {
        Image(modifier = Modifier.fillMaxSize(), painter = painter, contentDescription = null)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color.Black, Color.Transparent)))
                .padding(horizontal = 48.dp, vertical = 48.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = movie?.title.orEmpty(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.tmdbColors.text
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextWithIcon(
                        text = movie?.voteAverage.orEmpty(),
                        iconResId = R.drawable.ic_rating
                    )
                    TextWithIcon(
                        text = movie?.releaseDate.orEmpty(),
                        iconResId = R.drawable.ic_date
                    )
                    TextWithIcon(text = movie?.genres.orEmpty(), iconResId = R.drawable.ic_category)
                }

                Text(
                    text = movie?.description.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.tmdbColors.textCaption
                )

                Spacer(Modifier.height(46.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FocusableImageButton(
                        iconResId = R.drawable.ic_play,
                        selectedIconResId = R.drawable.ic_play,
                        focusRequester = FocusRequester(),
                        onClick = {
                            movie?.id?.let { movieId ->
                                navigateTo(NavRoute.PlayerNavRoute(movieId))
                            }
                        }
                    )

                    FocusableImageButton(
                        iconResId = R.drawable.ic_star,
                        selectedIconResId = R.drawable.ic_star_filled,
                        focusRequester = FocusRequester(),
                        isSelected = isFavorite,
                        onClick = { onEvent(DetailsScreenUiEvent.ToggleFavoriteState) }
                    )
                }
            }
        }
    }
}