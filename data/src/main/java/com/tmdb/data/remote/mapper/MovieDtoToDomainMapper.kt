package com.tmdb.data.remote.mapper

import com.tmdb.common.base.BaseMapper
import com.tmdb.data.remote.entity.common.MovieDto
import com.tmdb.domain.model.movie.Movie
import javax.inject.Inject

class MovieDtoToDomainMapper @Inject constructor(): BaseMapper<MovieDto, Movie>() {
    override fun mapFrom(input: MovieDto): Movie {
        return Movie(
            id = input.id ?: 0,
            title = input.title.orEmpty(),
            posterPath = input.posterPath.orEmpty()
        )
    }
}