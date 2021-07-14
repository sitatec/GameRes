package dev.berete.gameres.ui.screens.game_details

import android.os.Parcelable
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoState(var videoId: String, var currentSecond: Float = 0f) : Parcelable,
    YouTubePlayerListener {

    override fun onApiChange(youTubePlayer: YouTubePlayer) {}

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentSecond = second
    }


    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality,
    ) {}

    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate,
    ) {}

    override fun onReady(youTubePlayer: YouTubePlayer) {}
    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
}