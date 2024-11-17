package com.tmdb.domain.usecase

import com.tmdb.domain.model.movie.Movie
import com.tmdb.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Result<Movie> {
        return movieRepository.getMovieDetails(id)
    }
}