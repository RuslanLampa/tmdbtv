package com.tmdb.domain.usecase

import com.tmdb.domain.model.movie.Movie
import com.tmdb.domain.repository.MovieRepository
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class)
class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): Result<List<Movie>> {
        return movieRepository.getPopularMovies()
    }
}