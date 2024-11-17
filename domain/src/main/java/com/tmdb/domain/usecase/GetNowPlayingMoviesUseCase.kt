package com.tmdb.domain.usecase

import com.tmdb.domain.model.movie.Movie
import com.tmdb.domain.repository.MovieRepository
import javax.inject.Inject


class GetNowPlayingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> {
        return movieRepository.getNowPlayingMovies()
    }
}