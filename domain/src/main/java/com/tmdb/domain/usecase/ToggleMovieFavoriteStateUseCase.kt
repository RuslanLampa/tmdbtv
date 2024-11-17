package com.tmdb.domain.usecase

import com.tmdb.domain.model.movie.Movie
import com.tmdb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleMovieFavoriteStateUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        if (movieRepository.isMovieFavoriteFlow(movie.id).first()) {
            movieRepository.removeFromFavorites(movie.id)
        } else {
            movieRepository.addToFavorites(movie)
        }
    }
}