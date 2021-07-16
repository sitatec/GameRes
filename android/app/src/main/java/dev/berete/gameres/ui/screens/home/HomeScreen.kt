package dev.berete.gameres.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.shared.components.*
import dev.berete.gameres.ui.theme.*
import dev.berete.gameres.ui.utils.FakeGameList
import dev.berete.gameres.ui.utils.bannerUrl
import dev.berete.gameres.ui.utils.gameTypeNames

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { GameResTopAppBar(title = { GameResLogo() }, scaffoldState = scaffoldState) },
        drawerContent = {
            NavDrawer(
                navController = navController,
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        HomeScreenBody(viewModel, navController)
    }
}

@Composable
fun HomeScreenBody(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val gameList by viewModel.gameList.observeAsState(initial = emptyList())
    val mostPopularGames by viewModel.mostPopularGames.observeAsState(initial = emptyList())
    val newGames by viewModel.newGames.observeAsState(initial = emptyList())
    val upcomingRelease by viewModel.upComingReleases.observeAsState(initial = emptyList())

    Column {

        Tabs(titles = gameTypeNames, viewModel::onGameTypeSelected)

        val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 200

        SwipeRefresh(
            onRefresh = { viewModel.loadNextPage() },
            bottomRefreshIndicatorState = rememberSwipeRefreshState(isRefreshing = viewModel.isNextPageLoading),
        ) {
            LazyColumn(modifier = modifier) {
                item {
                    Spacer(Modifier.height(16.dp))
                    GamesSection(
                        title = stringResource(R.string.popular_txt),
                        gameList = mostPopularGames,
                        onGameSelected = {
                            navController.navigate(Routes.gameDetails(it.id))
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                    GamesSection(
                        title = stringResource(R.string.new_txt),
                        gameList = newGames,
                        onGameSelected = {
                            navController.navigate(Routes.gameDetails(it.id))
                        },
                    )
                    Spacer(Modifier.height(16.dp))

                    Column(modifier) {
                        Text(
                            text = stringResource(R.string.upcoming_txt),
                            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                            modifier = Modifier.padding(start = 16.dp),
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow {
                            items(items = upcomingRelease) { release ->
                                Spacer(Modifier.width(13.dp))
                                ReleaseCard(
                                    release = release,
                                    onClick = {
                                        navController.navigate(Routes.gameDetails(release.gameId))
                                    },
                                )
                            }
                            item{
                                Spacer(Modifier.width(16.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.highly_rated_txt),
                        style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                        modifier = Modifier.padding(start = 16.dp),
                    )
                    Spacer(Modifier.height(10.dp))
                }

                items(items = gameList.chunked(numberOfItemsByRow)) { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(horizontal = 16.dp),
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
                    }
                    Spacer(Modifier.height(14.dp))
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


@Composable
fun GamesSection(
    gameList: List<Game>,
    title: String,
    onGameSelected: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(10.dp))
        LazyRow {
            items(items = gameList) { game ->
                Spacer(Modifier.width(14.dp))
                LargeGameCard(game = game, onClick = { onGameSelected(game) })
            }
            item{
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun LargeGameCard(game: Game, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        Modifier
            .clickable { onClick() }
            .size(250.dp, 150.dp)
            .then(modifier),
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        elevation = 5.dp
    ) {
        Image(
            painter = rememberCoilPainter(
                request = game.bannerUrl,
                previewPlaceholder = R.drawable.apex_legends_artwork,
            ),
            contentDescription = game.name,
            contentScale = ContentScale.Crop,
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colors.surface
                        )
                    )
                )
                .padding(bottom = 8.dp),
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = game.coverUrl,
                    previewPlaceholder = R.drawable.apex_legends_cover,
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.small),
            )
            Column {
                Text(
                    text = game.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
                PlatformLogos(
                    remember(game.id) {
                        game.platformList.groupBy { it.platformType }.keys.toList()
                    },
                )
            }
        }
    }
}


// ------------------------- PREVIEWS -------------------------- //

@ExperimentalFoundationApi
@Preview
@Composable
fun HomeScreenPreview() {
    GameResTheme {
        Scaffold(topBar = { GameResTopAppBar({ GameResLogo() }, rememberScaffoldState()) }) {
            val mostPopularGames = FakeGameList
            val trendingGameList = FakeGameList
            val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 150

            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                item {
                    GamesSection(
                        gameList = mostPopularGames,
                        onGameSelected = {}, title = "Popular Games"
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Trending",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 16.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(trendingGameList.chunked(numberOfItemsByRow)) { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        for (game in rowItems) {
                            GameCard(
                                game = game,
                                onClick = { },
                                modifier = Modifier.weight(1F)
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun LargeGameCardPreview() {
//    GameResTheme {
//        LargeGameCard(game = FakeGame, onClick = {})
//    }
//}