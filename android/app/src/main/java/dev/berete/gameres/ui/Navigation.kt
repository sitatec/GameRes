package dev.berete.gameres.ui

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.berete.gameres.ui.screens.game_details.GameDetailsScreen
import dev.berete.gameres.ui.screens.game_details.GameDetailsViewModel
import dev.berete.gameres.ui.screens.home.HomeScreen
import dev.berete.gameres.ui.screens.home.HomeViewModel

object Routes {
    const val Home = "home"
    const val GameDetails = "game_details/{gameId}"
    const val Search = "search"

    fun gameDetails(gameId: Long) = "game_details/$gameId"
}

@ExperimentalPagerApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home, modifier = Modifier.background(MaterialTheme.colors.background)) {
        composable(
            Routes.Home,
            arguments = listOf(navArgument("gameId") { type = NavType.LongType }),
        ) {
            HomeScreen(viewModel = hiltViewModel(), navController = navController)
        }

        composable(Routes.GameDetails) { backStackEntry ->
            val gameId = backStackEntry.arguments!!.getString("gameId")!!.toLong()
            GameDetailsScreen(
                viewModel = hiltViewModel<GameDetailsViewModel>().apply { initialize(gameId) },
                navController = navController,
            )
        }
    }
}