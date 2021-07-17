package dev.berete.gameres.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.berete.gameres.ui.screens.game_details.GameDetailsScreen
import dev.berete.gameres.ui.screens.game_details.GameDetailsViewModel
import dev.berete.gameres.ui.screens.home.HomeScreen
import dev.berete.gameres.ui.screens.new_games.NewGamesScreen
import dev.berete.gameres.ui.screens.new_games.NewGamesViewModel
import dev.berete.gameres.ui.screens.popular_games.PopularGamesScreen
import dev.berete.gameres.ui.screens.popular_games.PopularGamesViewModel
import dev.berete.gameres.ui.screens.upcoming_releases.UpcomingReleaseScreen

object Routes {
    const val Home = "home"
    const val GameDetails = "game_details/{gameId}"
    const val NewGames = "new_games/{minReleaseTimestamp}/{subtitle}"
    const val UpcomingReleases = "upcoming_releases"
    const val PopularGames = "popular_games/{minReleaseTimestamp}/{subtitle}"
    const val Search = "search"

    fun gameDetails(gameId: Long) = "game_details/$gameId"
    fun newGames(minReleaseTimestamp: Long, subtitle: String) =
        "new_games/$minReleaseTimestamp/$subtitle"

    fun popularGames(minReleaseTimestamp: Long, subtitle: String) =
        "popular_games/$minReleaseTimestamp/$subtitle"
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = Modifier.background(MaterialTheme.colors.background),
    ) {
        composable(Routes.Home) {
            HomeScreen(viewModel = hiltViewModel(), navController = navController)
        }

        composable(
            Routes.GameDetails,
            arguments = listOf(navArgument("gameId") { type = NavType.LongType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments!!.getLong("gameId")
            GameDetailsScreen(
                viewModel = hiltViewModel<GameDetailsViewModel>().apply { initialize(gameId) },
                navController = navController,
            )
        }

        composable(Routes.NewGames, arguments = listOf(navArgument("minReleaseTimestamp") {
            type = NavType.LongType
        })) { backStackEntry ->
            val minReleaseTimestamp = backStackEntry.arguments!!.getLong("minReleaseTimestamp")
            val subtitle = backStackEntry.arguments!!.getString("subtitle")!!
            NewGamesScreen(
                viewModel = hiltViewModel<NewGamesViewModel>().apply {
                    initialize(
                        minReleaseTimestamp
                    )
                },
                navController = navController,
                subtitle = subtitle,
            )
        }

        composable(Routes.UpcomingReleases) {
            UpcomingReleaseScreen(viewModel = hiltViewModel(), navController = navController)
        }

        composable(Routes.PopularGames, arguments = listOf(navArgument("minReleaseTimestamp") {
            type = NavType.LongType
        })) { backStackEntry ->
            val minReleaseTimestamp = backStackEntry.arguments!!.getLong("minReleaseTimestamp")
            val subtitle = backStackEntry.arguments!!.getString("subtitle")!!
            val viewModel = hiltViewModel<PopularGamesViewModel>().apply {
                initializes(minReleaseTimestamp)
            }

            PopularGamesScreen(
                viewModel = viewModel,
                navController = navController,
                subtitle = subtitle,
            )
        }
    }
}