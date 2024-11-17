package com.tmdb.ui.screen.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlayerViewModel.PlayerViewModelFactory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted val movieId: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerScreenUiState())
    val uiState: StateFlow<PlayerScreenUiState> = _uiState

    init {
        fetchVideo()
    }

    private fun fetchVideo() {
        // This ViewModel as plain as possible, the main point is to showcase the player itself
        // Handling player and other errors takes large amount of time, so we skip it here
        // Just to show how the player works
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(status = PlayerScreenStatus.Loading)
            }
            // val movie = GetHlsMovieDataUseCase()
            delay(2000)
            val videoUri =
                Uri.parse("https://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8")
            _uiState.update {
                it.copy(status = PlayerScreenStatus.Content, videoUri = videoUri)
            }
        }
    }

    fun onEvent(event: PlayerScreenUiEvent) {
        when (event) {
            PlayerScreenUiEvent.Refresh -> fetchVideo()
        }
    }

    @AssistedFactory
    interface PlayerViewModelFactory {
        fun create(movieId: Int): PlayerViewModel
    }
}