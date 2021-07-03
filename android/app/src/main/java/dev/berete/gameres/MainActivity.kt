package dev.berete.gameres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dev.berete.gameres.ui.screens.home.HomeScreen
import dev.berete.gameres.ui.screens.home.HomeViewModel
import dev.berete.gameres.ui.theme.DarkBlue
import dev.berete.gameres.ui.theme.GameResTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameResTheme {
                HomeScreen(viewModel = viewModel)

                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(color = DarkBlue, darkIcons = false)
                }
            }
        }
    }
}
