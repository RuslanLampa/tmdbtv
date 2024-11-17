package com.tmdb.domain.usecase

import com.tmdb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieFavoriteStateUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return movieRepository.isMovieFavoriteFlow(id)
    }
}