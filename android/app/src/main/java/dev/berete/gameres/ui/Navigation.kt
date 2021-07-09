package dev.berete.gameres.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dev.berete.gameres.ui.screens.home.HomeScreen
import dev.berete.gameres.ui.screens.home.HomeViewModel

object Routes {
    const val Home = "home"
    const val GameDetails = "game_details/{gameId}"
    const val Search = "search"

    fun gameDetails(gameId: Long) = "game_details/$gameId"
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(
            Routes.Home,
            arguments = listOf(navArgument("gameId") { type = NavType.LongType }),
        ) {
            HomeScreen(viewModel = hiltViewModel(), navController = navController)
        }

        composable(Routes.GameDetails) {
            TODO()
        }
    }
}