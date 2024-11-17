package com.tmdb.ui.mapper

import com.tmdb.common.base.BaseMapper
import com.tmdb.common.manager.ImageSize
import com.tmdb.common.manager.ImageUrlManager
import com.tmdb.domain.model.movie.Movie
import com.tmdb.ui.data.MovieItem
import javax.inject.Inject

class MovieDomainToMovieItemMapper @Inject constructor(
    private val imageUrlManager: ImageUrlManager
) : BaseMapper<Movie, MovieItem>() {
    override fun mapFrom(input: Movie): MovieItem {
        return MovieItem(
            id = input.id,
            title = input.title,
            posterImageUrl = imageUrlManager.getUrl(
                imagePath = input.posterPath,
                size = ImageSize.W342
            ),
            description = input.description.orEmpty(),
            genres = input.genres?.joinToString(),
            backdropImageUrl = input.backdropPath?.let {
                imageUrlManager.getUrl(
                    imagePath = it,
                    size = ImageSize.Original
                )
            },
            releaseDate = input.releaseDate.orEmpty(),
            voteAverage = "%.1f".format(input.voteAverage?.toDoubleOrNull() ?: 0.0)
        )
    }
}