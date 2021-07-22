package dev.berete.gameres

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleObserver
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dev.berete.gameres.ui.Navigation
import dev.berete.gameres.ui.theme.DarkBlue
import dev.berete.gameres.ui.theme.GameResTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this // Refactor
        setContent {
            GameResTheme {
                Surface{
                    Navigation()
                }

                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(color = DarkBlue, darkIcons = false)
                }
            }
        }
    }

    companion object{
        private lateinit var instance: MainActivity
        fun addLifecycleObserver(observer: LifecycleObserver){
            //TODO REFACTOR
            instance.lifecycle.addObserver(observer)
        }
    }

}