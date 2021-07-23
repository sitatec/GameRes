package dev.berete.gameres.ui.video_player

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class VideoState(
    var videoId: String,
    var currentSecond: Float = 0f,
    var isFullScreen: Boolean = false,
    var state: PlayerState = PlayerState.UNKNOWN,
) : Parcelable,
    YouTubePlayerListener {

    override fun onApiChange(youTubePlayer: YouTubePlayer) {}

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentSecond = second
    }


    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality,
    ) {
    }

    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate,
    ) {
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {}
    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        when(state){
            PlayerConstants.PlayerState.UNKNOWN,
            PlayerConstants.PlayerState.BUFFERING,
            PlayerConstants.PlayerState.VIDEO_CUED -> this.state = PlayerState.UNKNOWN
            PlayerConstants.PlayerState.UNSTARTED -> this.state = PlayerState.UNSTARTED
            PlayerConstants.PlayerState.ENDED -> this.state = PlayerState.ENDED
            PlayerConstants.PlayerState.PLAYING -> this.state = PlayerState.PLAYING
            PlayerConstants.PlayerState.PAUSED -> this.state = PlayerState.PAUSED
        }
    }
}

enum class PlayerState {
    PLAYING,
    PAUSED,
    ENDED,
    UNSTARTED,
    UNKNOWN,
}