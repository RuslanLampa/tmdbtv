package com.tmdb.domain.repository

import com.tmdb.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun isMovieFavoriteFlow(id: Int): Flow<Boolean>

    suspend fun addToFavorites(movie: Movie)

    suspend fun getPopularMovies(): Result<List<Movie>>

    suspend fun getNowPlayingMovies(): Result<List<Movie>>

    suspend fun getFavoriteMovies(): Flow<List<Movie>>

    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun removeFromFavorites(id: Int)
}