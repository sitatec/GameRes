package dev.berete.gameres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dev.berete.gameres.ui.Navigation
import dev.berete.gameres.ui.theme.DarkBlue
import dev.berete.gameres.ui.theme.GameResTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

