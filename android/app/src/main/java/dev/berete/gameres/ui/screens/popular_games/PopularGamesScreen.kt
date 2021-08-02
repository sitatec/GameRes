package dev.berete.gameres.ui.screens.popular_games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.shared.components.*
import dev.berete.gameres.ui.utils.GameCardPlaceholder
import dev.berete.gameres.ui.utils.gameTypeNames

@Composable
fun PopularGamesScreen(
    viewModel: PopularGamesViewModel,
    navController: NavController,
    subtitle: String,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    CollapsingTabBarScaffold(
        topAppBar = {
            GameResTopAppBar(
                title = { Text("Popular Games", fontSize = 18.sp) },
                drawerState = drawerState,
            )
                    },
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                navController = navController,
                modifier = Modifier.background(MaterialTheme.colors.surface),
                state = drawerState,
            )
        }) {
        NewGamesScreenBody(
            viewModel = viewModel,
            navController = navController,
            subtitle = subtitle,
        )
    }
}

@Composable
fun NewGamesScreenBody(
    viewModel: PopularGamesViewModel,
    navController: NavController,
    subtitle: String,
) {
    val popularGames by viewModel.popularGames.observeAsState(emptyList())
    val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 200

    Column {
        Tabs(titles = gameTypeNames, viewModel::onGameTypeSelected)
        SwipeRefresh(
            onRefresh = { viewModel.loadNextPage() },
            bottomRefreshIndicatorState = rememberSwipeRefreshState(isRefreshing = viewModel.isNextPageLoading),
        ) {
            LazyColumn(Modifier.padding(horizontal = 16.dp)) {

                item {
                    if (subtitle.isNotBlank()) {
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                        )
                    }
                    Spacer(Modifier.height(13.dp))
                }

                if (popularGames.isEmpty()) {
                    items(items = List(10){it}.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (item in rowItems) {
                                GameCardPlaceholder(Modifier.weight(1F))
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                } else {
                    items(items = popularGames.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (game in rowItems) {
                                GameCard(
                                    game = game,
                                    onClick = {
                                        navController.navigate(Routes.gameDetails(game.id))
                                    },
                                    modifier = Modifier.weight(1F),
                                )
                            }
                            // If the last row do not contains enough items to fill the row without
                            // expanding them, we add placeholders to keep the same size for all items.
                            repeat(numberOfItemsByRow - rowItems.size) {
                                Box(Modifier.weight(1F)) {}
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                }

                if (viewModel.isLastPageReached) {
                    item {
                        Card(elevation = 15.dp, backgroundColor = Color.Gray.copy(0.1F)) {
                            Text(
                                "Oops, there are no more games to load, you have reached the end of the page.",
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
