package com.example.player.builder

import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.player.callback.ConfigCallback
import com.example.player.callback.ControlsCallback
import com.example.player.callback.PlaybackStateCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@UnstableApi
class PlayerManager(
    private val videoUrl: String
) {
    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player = _player.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playbackException = MutableStateFlow<PlaybackException?>(null)
    val playbackException = _playbackException.asStateFlow()

    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    val playbackState = _playbackState.asStateFlow()

    private var listener: Player.Listener? = null

    val configCallback by lazy { createConfigCallback() }
    val controlsCallback by lazy { createControlsCallback() }
    val playbackStateCallback by lazy { createPlaybackStateCallback() }

    private var playerLastState = PlayerLastState(
        trackSelectionParams = null
    )

    fun initialize(context: Context) {
        _player.value = ExoPlayer.Builder(context).build()
        setMediaSource(videoUrl = videoUrl, context = context)
        createListener()
        player.value!!.prepare()
        retrieveLastState()
    }

    fun release() {
        _isPlaying.value = false
        saveLastState()
        listener?.let { player.value?.removeListener(it) }
        player.value?.release()
        _player.value = null
    }

    fun playOnReconnect() {
        player.value?.prepare()
        player.value?.play()
    }

    private fun retrieveLastState() {
        if (player.value != null) {
            setPlaybackSpeed(playerLastState.playbackSpeed)
            playerLastState.trackSelectionParams?.let { setResolution(it) }
            player.value!!.playWhenReady = playerLastState.playWhenReady
            player.value!!.seekTo(playerLastState.startDuration)
        }
    }

    private fun saveLastState() {
        if (player.value != null) {
            playerLastState = PlayerLastState(
                playWhenReady = player.value!!.playWhenReady,
                startDuration = player.value!!.currentPosition,
                playbackSpeed = player.value!!.playbackParameters.speed,
                trackSelectionParams = player.value!!.trackSelectionParameters
            )
        }
    }

    private fun setPlaybackSpeed(speed: Float) {
        player.value?.playbackParameters = PlaybackParameters(speed)
    }

    private fun setResolution(trackSelectionParameters: TrackSelectionParameters) {
        player.value?.trackSelectionParameters = trackSelectionParameters.buildUpon()
            .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
            .clearVideoSizeConstraints()
            .clearViewportSizeConstraints()
            .build()
    }

    private fun createControlsCallback(): ControlsCallback {
        return object : ControlsCallback {
            override fun play() {
                player.value?.play()
            }

            override fun pause() {
                player.value?.pause()
            }

            override fun next() {
            }

            override fun previous() {
            }

            override fun seekForward() {
                player.value?.seekForward()
            }

            override fun seekBackward() {
                player.value?.seekBack()
            }
        }
    }

    private fun createConfigCallback(): ConfigCallback {
        return object : ConfigCallback {
            override fun onSettingClick() = Unit

            override fun onCaptionClick() = Unit

            override fun onCastClick() = Unit
        }
    }

    private fun createPlaybackStateCallback(): PlaybackStateCallback {
        return object : PlaybackStateCallback {
            override fun getCurrentPosition(): Long = player.value?.currentPosition ?: 0L

            override fun getDuration(): Long = player.value?.duration ?: 0L

            override fun getBufferPercentage(): Int = player.value?.bufferedPercentage ?: 0
        }
    }

    private fun createListener() {
        listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isPlaying.value = isPlaying
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                _playbackException.update { error }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                _playbackState.update { playbackState }
            }
        }
        player.value?.addListener(listener as Player.Listener)
    }

    @OptIn(UnstableApi::class)
    private fun setMediaSource(videoUrl: String, context: Context) {
        player.value.let { player ->
            val defaultDataSourceFactory = DefaultDataSource.Factory(context)
            val dataSourceFactory: DataSource.Factory =
                DefaultDataSource.Factory(context, defaultDataSourceFactory)
            val source = createMediaSource(videoUrl, dataSourceFactory)
            player?.setMediaSource(source)
        }
    }

    @OptIn(UnstableApi::class)
    private fun createMediaSource(
        videoUrl: String,
        dataSourceFactory: DataSource.Factory
    ): MediaSource {
        val mediaItem: MediaItem = MediaItem.Builder().apply {
            setUri(Uri.parse(videoUrl))
            when {
                videoUrl.contains(".mpd") -> setMimeType(MimeTypes.APPLICATION_MPD)
                videoUrl.contains(".m3u8") -> setMimeType(MimeTypes.APPLICATION_M3U8)
                videoUrl.contains(".mp4") -> setMimeType(MimeTypes.APPLICATION_MP4)
            }
        }.build()

        return when {
            videoUrl.contains(".mpd") -> DashMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(mediaItem)

            videoUrl.contains(".m3u8") -> HlsMediaSource
                .Factory(dataSourceFactory)
                .createMediaSource(mediaItem)

            else -> ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayerManager(videoUrl: String): PlayerManager {
    return remember { PlayerManager(videoUrl) }
}