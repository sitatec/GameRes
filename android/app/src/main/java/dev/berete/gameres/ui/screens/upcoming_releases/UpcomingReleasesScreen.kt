package dev.berete.gameres.ui.screens.upcoming_releases

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Release
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.new_games.NewGamesViewModel
import dev.berete.gameres.ui.screens.shared.components.*
import dev.berete.gameres.ui.utils.ReleaseCardPlaceholder
import dev.berete.gameres.ui.utils.buildFakeRelease
import dev.berete.gameres.ui.utils.gameTypeNames

@Composable
fun UpcomingReleaseScreen(
    viewModel: UpcomingReleasesViewModel,
    navController: NavController,
) {
    val scaffoldState = rememberScaffoldState()

    ScaffoldWrapper{
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                GameResTopAppBar(
                    title = { Text("Upcoming Releases", fontSize = 18.sp) },
                    scaffoldState = scaffoldState,
                    // TODO implement sorting feature
//                actions = {
//                    Icon(
//                        imageVector = Icons.Default.Sort,
//                        contentDescription = null,
//                        modifier = Modifier.padding(end = 8.dp),
//                    )
//                }
                )
            },
            drawerContent = {
                NavDrawer(
                    navController = navController,
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface),
                    state = scaffoldState.drawerState,
                )
            },
            drawerBackgroundColor = Color.Transparent,
            drawerScrimColor = MaterialTheme.colors.surface.copy(0.63f),
            drawerElevation = 0.dp,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            UpcomingReleaseScreenBody(
                viewModel = viewModel,
                navController = navController,
            )
        }
    }
}

@Composable
fun UpcomingReleaseScreenBody(
    viewModel: UpcomingReleasesViewModel,
    navController: NavController,
) {
    val upcomingReleases by viewModel.upcomingReleases.observeAsState(emptyList())
    val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 250

    Column {
        Tabs(titles = gameTypeNames, viewModel::onGameTypeSelected)


        SwipeRefresh(
            onRefresh = { viewModel.loadNextPage() },
            bottomRefreshIndicatorState = rememberSwipeRefreshState(isRefreshing = viewModel.isNextPageLoading),
        ) {
            LazyColumn(Modifier.padding(horizontal = 16.dp)) {

                item {
                    Spacer(Modifier.height(16.dp))
                }

                if (upcomingReleases.isNullOrEmpty()) {
                    items(items = List(10) { it }.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (release in rowItems) {
                                ReleaseCardPlaceholder(
                                    Modifier
                                        .size(250.dp, 140.dp)
                                        .weight(1F),
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
                } else {
                    items(items = upcomingReleases.chunked(numberOfItemsByRow)) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            for (release in rowItems) {
                                ReleaseCard(
                                    release = release,
                                    onClick = {
                                        navController.navigate(Routes.gameDetails(release.gameId))
                                    },
                                    modifier = Modifier
                                        .size(250.dp, 140.dp)
                                        .weight(1F),
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
                                stringResource(R.string.end_of_the_page_reached_msg),
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


@Composable
fun UpcomingReleasesPlaceholder() {

}

