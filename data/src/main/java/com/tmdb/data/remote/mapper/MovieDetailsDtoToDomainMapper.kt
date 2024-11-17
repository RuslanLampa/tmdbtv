package com.tmdb.data.remote.mapper

import com.tmdb.common.base.BaseMapper
import com.tmdb.data.remote.entity.movieDetails.MovieDetailsDto
import com.tmdb.domain.model.movie.Movie
import javax.inject.Inject

class MovieDetailsDtoToDomainMapper @Inject constructor(): BaseMapper<MovieDetailsDto, Movie>() {
    override fun mapFrom(input: MovieDetailsDto): Movie {
        return Movie(
            id = input.id ?: 0,
            title = input.title.orEmpty(),
            description = input.overview.orEmpty(),
            genres = input.genres?.mapNotNull { it.name } ?: emptyList(),
            backdropPath = input.backdropPath.orEmpty(),
            posterPath = input.posterPath.orEmpty(),
            releaseDate = input.releaseDate.orEmpty(),
            voteAverage = input.voteAverage.toString()
        )
    }
}