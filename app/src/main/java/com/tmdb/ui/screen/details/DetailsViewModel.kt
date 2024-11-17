package com.tmdb.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.common.utils.ErrorModel
import com.tmdb.common.utils.toErrorModel
import com.tmdb.domain.usecase.GetMovieDetailsUseCase
import com.tmdb.domain.usecase.GetMovieFavoriteStateUseCase
import com.tmdb.domain.usecase.ToggleMovieFavoriteStateUseCase
import com.tmdb.ui.mapper.MovieDomainToMovieItemMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.DetailViewModelFactory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted val id: Int,
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val getMovieFavoriteState: GetMovieFavoriteStateUseCase,
    private val toggleMovieFavoriteState: ToggleMovieFavoriteStateUseCase,
    private val movieDomainToMovieItemMapper: MovieDomainToMovieItemMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailScreenUiState())
    val uiState: StateFlow<DetailScreenUiState> = _uiState

    init {
        fetchMovie()
        collectFavoriteState()
    }

    fun onEvent(event: DetailsScreenUiEvent) {
        when (event) {
            DetailsScreenUiEvent.ToggleFavoriteState -> toggleFavoriteState()
            DetailsScreenUiEvent.Refresh -> fetchMovie()
        }
    }

    private fun fetchMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getMovieDetails(id)

                if (result.isSuccess) {
                    result.getOrNull()?.let { movie ->
                        _uiState.update {
                            it.copy(
                                status = DetailScreenStatus.Content,
                                movieItem = movieDomainToMovieItemMapper.mapFrom(movie),
                                movie = movie
                            )
                        }
                    }
                } else {
                    val error = result.exceptionOrNull()?.toErrorModel() ?: ErrorModel.unknown()
                    _uiState.update {
                        it.copy(status = DetailScreenStatus.Error(error))
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = DetailScreenStatus.Error(e.toErrorModel()))
                }
            }
        }
    }

    private fun collectFavoriteState() {
        viewModelScope.launch(Dispatchers.IO) {
            getMovieFavoriteState(id).collect { isFavorite ->
                _uiState.update {
                    it.copy(isFavorite = isFavorite)
                }
            }
        }
    }

    private fun toggleFavoriteState() {
        viewModelScope.launch {
            _uiState.value.movie?.let {
                toggleMovieFavoriteState(it)
            }
        }
    }

    @AssistedFactory
    interface DetailViewModelFactory {
        fun create(movieId: Int): DetailsViewModel
    }
}