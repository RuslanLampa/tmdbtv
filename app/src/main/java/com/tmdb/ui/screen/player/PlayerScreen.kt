package com.tmdb.ui.screen.player

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import com.tmdb.widget.errorContainer.ErrorContainer
import com.tmdb.widget.loadingContainer.LoadingContainer
import com.tmdb.widget.utils.ComposableLifecycle
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {}
) {
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    when (screenState.status) {
        is PlayerScreenStatus.Error -> ErrorContainer {
            viewModel.onEvent(PlayerScreenUiEvent.Refresh)
        }
        PlayerScreenStatus.Loading -> LoadingContainer()
        PlayerScreenStatus.Content -> screenState.videoUri?.let {
            PlayerScreenContent(
                videoUri = it,
                navigateBack = navigateBack
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreenContent(
    videoUri: Uri,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    var player: ExoPlayer? by remember {
        mutableStateOf(null)
    }
    var isPlaying by remember { mutableStateOf(true) }
    var isEnded by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var volume by remember { mutableFloatStateOf(100f) }
    var lastVolume by remember { mutableFloatStateOf(100f) }

    // Initialize player view
    val playerView = createPlayerView(player)

    // Create a focus requester to focus the controls
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Update state based on player's playing status
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    ExoPlayer.STATE_ENDED -> {
                        isEnded = true
                    }

                    Player.STATE_BUFFERING -> {
                        isEnded = false
                    }

                    Player.STATE_IDLE -> {
                        isEnded = false
                    }

                    Player.STATE_READY -> {
                        isEnded = false
                    }
                }
                currentPosition = player?.currentPosition ?: 0L
                duration = player?.duration ?: 0L
                volume = player?.volume ?: 100f
            }
        }

        player?.addListener(listener)

        onDispose {
            player?.removeListener(listener)
        }
    }

    // Periodically update the current position of the player
    LaunchedEffect(player) {
        while (true) {
            player?.let {
                currentPosition = it.currentPosition
                duration = it.duration
                volume = it.volume
            }
            delay(1000L) // Update every second
        }
    }

    // Define player controls logic
    fun playPause() {
        player?.let {
            when {
                isEnded -> {
                    it.seekTo(0)
                    it.play()
                }

                it.isPlaying -> it.pause()
                else -> it.play()
            }
        }
    }

    fun rewind() {
        player?.seekBack()
    }

    fun fastForward() {
        player?.seekForward()
    }

    fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
    ) {
        ComposableLifecycle(
            onStart = {
                player = initPlayer(context, videoUri)
                playerView.onResume()
            },
            onStop = {
                playerView.apply {
                    player?.release()
                    onPause()
                    player = null
                }
            }
        )

        // Add AndroidView for the video player
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .focusProperties { canFocus = false },
            factory = { playerView }
        )

        // Overlay the PlayerController on top of the video player
        PlayerController(
            isPlaying = isPlaying,
            isEnded = isEnded,
            currentPosition = currentPosition,
            duration = duration,
            volume = volume,
            onPlayPause = { playPause() },
            onRewind = { rewind() },
            onFastForward = { fastForward() },
            onSeek = { seekTo(it) },
            onMuteClick = {
                player?.let {
                    if (it.volume <= 0.0f) {
                        volume = lastVolume
                        it.volume = lastVolume
                    } else {
                        lastVolume = it.volume
                        volume = 0f
                        it.volume = 0f
                    }
                }
            },
            navigateBack = { navigateBack() }
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun createPlayerView(player: Player?): PlayerView {
    val context = LocalContext.current

    val playerView = remember {
        PlayerView(context).apply {
            this.player = player
            controllerAutoShow = false
            useController = false
            keepScreenOn = true
            subtitleView?.visibility = View.VISIBLE
        }
    }

    DisposableEffect(player) {
        playerView.player = player

        onDispose {
            playerView.player = null
        }
    }

    return playerView
}

@OptIn(UnstableApi::class)
fun initPlayer(context: Context, videoUri: Uri): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()

        val mediaSource = buildMediaSource(
            videoUri,
            defaultHttpDataSourceFactory,
            null
        )

        setMediaSource(mediaSource)
        playWhenReady = true
        prepare()
    }
}

@OptIn(UnstableApi::class)
fun buildMediaSource(
    uri: Uri,
    defaultHttpDataSourceFactory: DefaultHttpDataSource.Factory,
    overrideExtension: String?,
): MediaSource {
    val type = if (overrideExtension.isNullOrEmpty()) {
        Util.inferContentType(uri)
    } else {
        Util.inferContentTypeForExtension(overrideExtension)
    }
    return when (type) {
        C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        else -> {
            throw IllegalStateException("Unsupported type: $type")
        }
    }
}