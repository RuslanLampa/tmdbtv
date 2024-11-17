package com.tmdb.data.remote.mapper

import com.tmdb.data.local.room.entity.FavoriteMovieEntity
import com.tmdb.data.remote.entity.common.MovieDto
import com.tmdb.domain.model.movie.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    private fun mapToDomain(movieDto: MovieDto?): Movie? {
        return movieDto?.let { dto ->
            return Movie(
                id = dto.id ?: return null,
                title = dto.title.orEmpty(),
                posterPath = dto.posterPath.orEmpty()
            )
        }
    }

    fun mapToDomain(entity: FavoriteMovieEntity): Movie {
        return Movie(
            id = entity.id,
            title = entity.title,
            posterPath = entity.posterPath
        )
    }

    fun mapFromDto(dtoList: List<MovieDto>?): List<Movie> {
        return dtoList?.mapNotNull { mapToDomain(it) } ?: emptyList()
    }

    fun mapFromEntity(dtoList: List<FavoriteMovieEntity>): List<Movie> {
        return dtoList.map { mapToDomain(it) }
    }

    fun mapToEntity(movie: Movie): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = movie.id,
            title = movie.title,
            posterPath = movie.posterPath
        )
    }
}