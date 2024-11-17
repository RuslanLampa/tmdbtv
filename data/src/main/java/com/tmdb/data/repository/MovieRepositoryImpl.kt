package com.tmdb.data.repository

import com.tmdb.data.local.room.dao.FavoriteMoviesDao
import com.tmdb.data.remote.api.MovieApi
import com.tmdb.data.remote.mapper.FavoriteMovieEntityToMovieDomainMapper
import com.tmdb.data.remote.mapper.MovieDetailsDtoToDomainMapper
import com.tmdb.data.remote.mapper.MovieDtoToDomainMapper
import com.tmdb.domain.model.movie.Movie
import com.tmdb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDetailsDtoToDomainMapper: MovieDetailsDtoToDomainMapper,
    private val favoriteMoviesDao: FavoriteMoviesDao,
    private val movieDtoToDomainMapper: MovieDtoToDomainMapper,
    private val favoriteMovieEntityToMovieDomainMapper: FavoriteMovieEntityToMovieDomainMapper
) : MovieRepository {
    override suspend fun getPopularMovies(): Result<List<Movie>> {
        return try {
            val response = movieApi.getPopularMovies()
            val data =
                response.body()?.results?.map { movieDtoToDomainMapper.mapFrom(it) } ?: emptyList()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>> {
        return try {
            val response = movieApi.getNowPlayingMovies()
            val data =
                response.body()?.results?.map { movieDtoToDomainMapper.mapFrom(it) } ?: emptyList()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteMoviesDao.getAllFavorites().map {
            it.map { m -> favoriteMovieEntityToMovieDomainMapper.mapFrom(m) }
        }
    }

    override fun isMovieFavoriteFlow(id: Int): Flow<Boolean> {
        return favoriteMoviesDao.getFavoriteMovie(id).map { it != null }
    }

    override suspend fun addToFavorites(movie: Movie) {
        favoriteMoviesDao.addToFavorites(favoriteMovieEntityToMovieDomainMapper.mapTo(movie))
    }

    override suspend fun removeFromFavorites(id: Int) {
        favoriteMoviesDao.removeFromFavorites(id)
    }

    override suspend fun getMovieDetails(id: Int): Result<Movie> {
        return try {
            val response = movieApi.getMovieDetails(id)
            val movie = response.body()?.let { movieDetailsDtoToDomainMapper.mapFrom(it) }
            Result.success(movie ?: Movie.empty())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
