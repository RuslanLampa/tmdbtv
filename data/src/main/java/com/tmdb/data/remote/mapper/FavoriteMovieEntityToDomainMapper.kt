package com.tmdb.data.remote.mapper

import com.tmdb.common.base.BaseMapper
import com.tmdb.data.local.room.entity.FavoriteMovieEntity
import com.tmdb.domain.model.movie.Movie
import javax.inject.Inject

class FavoriteMovieEntityToMovieDomainMapper @Inject constructor() :
    BaseMapper<FavoriteMovieEntity, Movie>() {

    override fun mapFrom(input: FavoriteMovieEntity): Movie {
        return Movie(
            id = input.id,
            title = input.title,
            posterPath = input.posterPath,
            description = null,
            genres = listOf(),
            backdropPath = null,
            releaseDate = null,
            voteAverage = null
        )
    }

    override fun mapTo(output: Movie): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = output.id,
            title = output.title,
            posterPath = output.posterPath
        )
    }
}