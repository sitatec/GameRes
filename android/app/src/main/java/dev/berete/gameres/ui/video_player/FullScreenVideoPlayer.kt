package dev.berete.gameres.ui.video_player

import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat

import android.os.Bundle
import android.view.View

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import android.os.Build

import android.view.WindowInsetsController

import android.view.WindowInsets
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import dev.berete.gameres.R

class FullScreenVideoPlayer : AppCompatActivity() {
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var videoState: VideoState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBars()
        youTubePlayerView = YouTubePlayerView(this)
        setContentView(youTubePlayerView)
        videoState = intent.getParcelableExtra(VIDEO_STATE_ARG_KEY)!!
        initYouTubePlayerView()
    }


    private fun initYouTubePlayerView() {

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.enterFullScreen()
        youTubePlayerView.apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(
                        videoId = videoState.videoId,
                        startSeconds = videoState.currentSecond,
                    )
                    this@FullScreenVideoPlayer.youTubePlayer = youTubePlayer
                    addCustomActionsToPlayer()
                }
            })
            addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {}

                override fun onYouTubePlayerExitFullScreen() {
                    finish()
                }
            })
            addYouTubePlayerListener(videoState)
        }
    }


    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private fun addCustomActionsToPlayer() {
        val fastForward = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward_24)
        val fastRewind = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind_24)

        youTubePlayerView.getPlayerUiController().setCustomAction1(fastRewind!!) {
            youTubePlayer.seekTo(videoState.currentSecond - 10)
        }

        youTubePlayerView.getPlayerUiController().setCustomAction2(fastForward!!) {
            youTubePlayer.seekTo(videoState.currentSecond + 10)
        }
    }

    private fun hideNavigationBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.decorView.windowInsetsController!!.hide(WindowInsets.Type.systemBars())
            window.decorView.windowInsetsController!!.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else hideNavigationBarForOldAPIs()
    }

    private fun hideNavigationBarForOldAPIs() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) hideNavigationBarForOldAPIs()
    }

    companion object {
        const val VIDEO_STATE_ARG_KEY = "VIDEO_STATE_KEY"
    }

}