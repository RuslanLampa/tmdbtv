package com.tmdb.ui.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.tmdb.common.R
import com.tmdb.widget.buttons.PlayerButton
import com.tmdb.widget.utils.handleDPadBackKeyEvent
import com.tmdb.widget.utils.handleDPadKeyEvents
import com.tmdb.widget.utils.handleDPadKeyLongEvents
import com.tmdb.widget.utils.onAnyKeyUpEvent
import com.tmdb.theme.tmdbColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayerController(
    isPlaying: Boolean,
    isEnded: Boolean,
    currentPosition: Long,
    duration: Long,
    volume: Float,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    onSeek: (Long) -> Unit,
    onMuteClick: () -> Unit,
    navigateBack: () -> Unit
) {
    var controlsAreShown by remember { mutableStateOf(true) }
    var isSliderFocused by remember { mutableStateOf(false) }
    var timerJob by remember { mutableStateOf<Job?>(null) }

    val mainFocusRequester = remember { FocusRequester() }
    val playPauseFocusRequester = remember { FocusRequester() }
    val sliderFocusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val timerDelay = 5000L

    var sliderPosition by remember { mutableLongStateOf(currentPosition) }

    var isSliderInteracting by remember { mutableStateOf(false) }

    var sliderSpeedMultiplier by remember { mutableIntStateOf(1) }

    fun showControlsIfNotShown() {
        if (!controlsAreShown) {
            controlsAreShown = true
        }

        timerJob?.cancel()
        timerJob = coroutineScope.launch(Dispatchers.IO) {
            delay(timerDelay)

            if (isSliderInteracting || isEnded) return@launch

            controlsAreShown = false
        }
    }

    LaunchedEffect(controlsAreShown) {
        if (controlsAreShown) {
            playPauseFocusRequester.requestFocus()
        } else {
            mainFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(isEnded) {
        if (isEnded) showControlsIfNotShown()
    }

    // Ensure valid duration and current position
    val validDuration = if (duration > 0) duration else 0L
    val validCurrentPosition = sliderPosition.coerceIn(0L, validDuration)

    LaunchedEffect(currentPosition) {
        if (!isSliderInteracting) {
            sliderPosition = currentPosition
        }
    }

    LaunchedEffect(isSliderInteracting) {
        if (isSliderInteracting) {
            while (true) {
                delay(500)
                sliderSpeedMultiplier += 1
            }
        } else {
            sliderSpeedMultiplier = 1
            showControlsIfNotShown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.tmdbColors.background.copy(
                    alpha = if (controlsAreShown) 0.7f else 0f
                )
            )
            .focusRequester(mainFocusRequester)
            .focusable()
            .onAnyKeyUpEvent {
                showControlsIfNotShown()
            }
            .handleDPadBackKeyEvent {
                if (controlsAreShown && !isEnded) {
                    controlsAreShown = false
                } else {
                    navigateBack()
                }
            }
    ) {

        if (controlsAreShown) {
            // Show controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = formatTime(validCurrentPosition), color = MaterialTheme.tmdbColors.accent)

                    // Progress
                    Slider(
                        value = validCurrentPosition.toFloat(),
                        valueRange = 0f..validDuration.toFloat(),
                        onValueChange = {},
                        colors = SliderDefaults.colors(
                            thumbColor = if (isSliderFocused) {
                                MaterialTheme.tmdbColors.accent
                            } else {
                                MaterialTheme.tmdbColors.accent.copy(alpha = 0.5f)
                            },
                            activeTrackColor = MaterialTheme.tmdbColors.accent,
                            inactiveTrackColor = MaterialTheme.tmdbColors.primary,
                            disabledThumbColor = MaterialTheme.tmdbColors.accent
                        ),
                        modifier = Modifier
                            .handleDPadKeyLongEvents(
                                onLeft = {
                                    isSliderInteracting = true
                                    sliderPosition -= (15000L * sliderSpeedMultiplier)
                                },
                                onRight = {
                                    isSliderInteracting = true
                                    sliderPosition += (15000L * sliderSpeedMultiplier)
                                }
                            )
                            .handleDPadKeyEvents(
                                onLeft = {
                                    isSliderInteracting = false
                                    onSeek(sliderPosition)
                                },
                                onRight = {
                                    isSliderInteracting = false
                                    onSeek(sliderPosition)
                                },
                                onDown = {
                                    playPauseFocusRequester.requestFocus()
                                }
                            )
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp)
                            .onFocusChanged { focusState ->
                                isSliderFocused = focusState.isFocused
                            }
                            .focusRequester(sliderFocusRequester)
                            .focusProperties {
                                right = sliderFocusRequester
                                left = sliderFocusRequester
                                down = playPauseFocusRequester
                            }
                            .focusable()
                    )

                    Text(text = formatTime(duration), color = MaterialTheme.tmdbColors.accent)
                }

                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 48.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        PlayerButton(
                            modifier = Modifier,
                            iconId = R.drawable.ic_rew,
                            onItemClick = {
                                onRewind()
                                showControlsIfNotShown()
                            }
                        )

                        PlayerButton(
                            modifier = Modifier,
                            iconId = when {
                                isEnded -> R.drawable.ic_replay
                                isPlaying -> R.drawable.ic_pause
                                else -> R.drawable.ic_play_rounded
                            },
                            focusRequester = playPauseFocusRequester,
                            onItemClick = {
                                onPlayPause()
                                showControlsIfNotShown()
                            }
                        )

                        PlayerButton(
                            modifier = Modifier,
                            iconId = R.drawable.ic_forward,
                            onItemClick = {
                                onFastForward()
                                showControlsIfNotShown()
                            },
                            enabled = !isEnded
                        )
                    }

                    PlayerButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        iconId = if (volume == 0f)
                            R.drawable.ic_volume_off else R.drawable.ic_volume,
                        onItemClick = {
                            onMuteClick()
                            showControlsIfNotShown()
                        }
                    )
                }
            }
        }
    }
}


// Helper function to format time in hh:mm:ss
fun formatTime(timeMillis: Long): String {
    val totalSeconds = timeMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}
