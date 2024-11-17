package com.tmdb.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.common.utils.ErrorModel
import com.tmdb.common.utils.toErrorModel
import com.tmdb.domain.usecase.GetMyFavoritesMoviesUseCase
import com.tmdb.domain.usecase.GetNowPlayingMoviesUseCase
import com.tmdb.domain.usecase.GetPopularMoviesUseCase
import com.tmdb.ui.data.FilterItem
import com.tmdb.ui.data.MovieItem
import com.tmdb.ui.mapper.MovieDomainToMovieItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getNowPlayingMovies: GetNowPlayingMoviesUseCase,
    private val getMyFavoritesMovies: GetMyFavoritesMoviesUseCase,
    private val movieDomainToMovieItemMapper: MovieDomainToMovieItemMapper
) : ViewModel() {

    private val selectedFilter = MutableStateFlow<FilterItem>(FilterItem.Popular)
    private val favoriteMovies = MutableStateFlow<List<MovieItem>?>(null)
    private val nowPlayingMovies = MutableStateFlow<List<MovieItem>?>(null)
    private val popularMovies = MutableStateFlow<List<MovieItem>?>(null)

    private val _uiState = MutableStateFlow(HomeScreenUiState.empty())
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    init {
        // Start collecting favorites in the background
        collectFavorites()

        // Combine all flows and update UI state accordingly
        viewModelScope.launch {
            combine(
                selectedFilter,
                favoriteMovies,
                nowPlayingMovies,
                popularMovies
            ) { filter, favorites, nowPlaying, popular ->
                when (filter) {
                    FilterItem.MyFavorites -> {
                        _uiState.value.copy(
                            selectedFilter = filter,
                            status = HomeScreenStatus.Content,
                            movies = favorites ?: emptyList()
                        )
                    }

                    FilterItem.NowPlaying -> {
                        if (nowPlaying.isNullOrEmpty()) {
                            fetchNowPlayingMovies()
                            _uiState.value.copy(
                                selectedFilter = filter,
                                status = HomeScreenStatus.Loading,
                                movies = emptyList()
                            )
                        } else {
                            _uiState.value.copy(
                                selectedFilter = filter,
                                status = HomeScreenStatus.Content,
                                movies = nowPlaying
                            )
                        }
                    }

                    FilterItem.Popular -> {
                        if (popular.isNullOrEmpty()) {
                            fetchPopularMovies()
                            _uiState.value.copy(
                                selectedFilter = filter,
                                status = HomeScreenStatus.Loading,
                                movies = emptyList()
                            )
                        } else {
                            _uiState.value.copy(
                                selectedFilter = filter,
                                status = HomeScreenStatus.Content,
                                movies = popular
                            )
                        }
                    }
                }
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.OnFilterButtonClick -> {
                selectedFilter.value = event.filter
            }

            HomeScreenUiEvent.Refresh -> {
                when (selectedFilter.value) {
                    FilterItem.Popular -> fetchPopularMovies()
                    FilterItem.NowPlaying -> fetchNowPlayingMovies()
                    FilterItem.MyFavorites -> Unit
                }
            }
        }
    }

    private fun collectFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            getMyFavoritesMovies()
                .map { movies -> movies.map { movieDomainToMovieItemMapper.mapFrom(it) } }
                .catch { favoriteMovies.value = emptyList() }
                .collect { mappedMovies ->
                    favoriteMovies.value = mappedMovies
                }
        }
    }

    private fun fetchNowPlayingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                nowPlayingMovies.value = null
                val result = getNowPlayingMovies()
                if (result.isSuccess) {
                    val movies = result.getOrNull()?.map {
                        movieDomainToMovieItemMapper.mapFrom(it)
                    } ?: emptyList()
                    nowPlayingMovies.value = movies
                } else {
                    val error = result.exceptionOrNull()?.toErrorModel() ?: ErrorModel.unknown()
                    _uiState.value = _uiState.value.copy(
                        status = HomeScreenStatus.Error(error),
                        movies = emptyList()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = HomeScreenStatus.Error(e.toErrorModel()),
                    movies = emptyList()
                )
            }
        }
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                popularMovies.value = null
                val result = getPopularMovies()
                if (result.isSuccess) {
                    val movies = result.getOrNull()?.map {
                        movieDomainToMovieItemMapper.mapFrom(it)
                    } ?: emptyList()
                    popularMovies.value = movies
                } else {
                    val error = result.exceptionOrNull()?.toErrorModel() ?: ErrorModel.unknown()
                    _uiState.value = _uiState.value.copy(
                        status = HomeScreenStatus.Error(error),
                        movies = emptyList()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = HomeScreenStatus.Error(e.toErrorModel()),
                    movies = emptyList()
                )
            }
        }
    }
}

