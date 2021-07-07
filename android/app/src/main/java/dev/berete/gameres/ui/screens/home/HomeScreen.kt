package dev.berete.gameres.ui.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.ui.screens.GameResLogo
import dev.berete.gameres.ui.screens.PlatformLogos
import dev.berete.gameres.ui.theme.*
import dev.berete.gameres.ui.utils.FakeGame
import dev.berete.gameres.ui.utils.FakeGameList

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Scaffold(topBar = { GameResTopAppBar({ GameResLogo() }) }) {
        Spacer(modifier = Modifier.height(8.dp))
        HomeScreenBody(viewModel)
    }
}

@Composable
fun GameResTopAppBar(title: @Composable () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = title,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_button_content_description),
                modifier = modifier.padding(start = 8.dp),
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_btn_content_description),
                modifier = modifier.padding(end = 8.dp),
            )
        },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 1.dp
    )
}

@Composable
fun HomeScreenBody(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    val trendingGameList by viewModel.trendingGameList.observeAsState(initial = emptyList())
    val mostPopularGames by viewModel.mostPopularGames.observeAsState(initial = emptyList())
    var selectedTabIndex by remember { mutableStateOf(0) }
    val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 200

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset((tabPositions[selectedTabIndex])).width(3.dp),
                    color = MaterialTheme.colors.primary,
                )
            },
        ) {
            viewModel.gameGenreNames.forEachIndexed { index, genreName ->
                Tab(
                    selected = index == selectedTabIndex,
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.87F),
                    onClick = {
                        if(selectedTabIndex != index) {
                            selectedTabIndex = index
                            viewModel.onGameTypeSelected(genreName)
                        }
                    },
                    text = { Text(text = genreName, fontSize = 13.sp) },
                )
            }
        }
        LazyColumn(modifier = modifier) {
            item {
                Spacer(Modifier.height(16.dp))
                MostPopularGamesSection(mostPopularGames = mostPopularGames, onGameSelected = {})
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Highly Rated",
                    style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(Modifier.height(10.dp))
            }

            items(items = trendingGameList.chunked(numberOfItemsByRow)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    for (game in rowItems) {
                        GameCard(game = game, onClick = { }, modifier = Modifier.weight(1F))
                    }
                }
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}

// ------------------------- MOST POPULAR GAMES SECTION -------------------------- //

@Composable
fun MostPopularGamesSection(
    mostPopularGames: List<Game>,
    onGameSelected: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = "Popular Games",
            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(10.dp))
        LazyRow {
            items(items = mostPopularGames, key = { it.id }) { game ->
                Spacer(Modifier.width(14.dp))
                LargeGameCard(game = game, onClick = { onGameSelected(game) })
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
                request = if (game.artWorkUrls.isEmpty()) "" else game.artWorkUrls.first(),
                fadeIn = true,
                previewPlaceholder = R.drawable.apex_legends_artwork,
            ),
            contentDescription = game.name,
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color.Transparent,
                    MaterialTheme.colors.surface)))
                .padding(bottom = 8.dp),
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = game.coverUrl,
                    fadeIn = true,
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
                PlatformLogos(game.platformList)
            }
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.clickable { onClick() }) {
        Column {
            Image(
                painter = rememberCoilPainter(
                    request = if (game.artWorkUrls.isEmpty()) game.coverUrl else game.artWorkUrls.first(),
                    fadeIn = true,
                    previewPlaceholder = R.drawable.apex_legends_cover,
                ),
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )
            Text(
                text = game.name,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 0.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp, top = 4.dp)
                    .fillMaxWidth(),
            ) {
                PlatformLogos(platformList = game.platformList)
                GameScore(score = game.rating.toInt())
            }
        }
    }
}

@Composable
fun GameScore(
    score: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default.copy(fontSize = 11.sp),
) {
    val scoreColor = remember {
        when {
            score > 70 -> HighScoreColor
            score > 50 -> MediumScoreColor
            score > 30 -> BadScoreColor
            else -> WorseScoreColor
        }
    }
    Text(
        text = score.toString(),
        style = style,
        color = scoreColor,
        modifier = Modifier
            .border(width = (1.5).dp, color = scoreColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .then(modifier),
    )
}

// ------------------------- PREVIEWS -------------------------- //

@ExperimentalFoundationApi
@Preview
@Composable
fun HomeScreenPreview() {
    GameResTheme {
        Scaffold(topBar = { GameResTopAppBar({ GameResLogo() }) }) {
            val mostPopularGames = FakeGameList
            val trendingGameList = FakeGameList
            val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 150

            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                item {
                    MostPopularGamesSection(mostPopularGames = mostPopularGames,
                        onGameSelected = {})
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
                            GameCard(game = game,
                                onClick = { },
                                modifier = Modifier.weight(1F))
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

//@Preview
@Composable
fun LargeGameCardPreview() {
    GameResTheme {
        LargeGameCard(game = FakeGame, onClick = {})
    }
}