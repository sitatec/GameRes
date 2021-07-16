package dev.berete.gameres.ui.screens.shared.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.PlatformType
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.theme.*
import dev.berete.gameres.ui.utils.bannerUrl
import dev.berete.gameres.ui.utils.getYearTimestamp
import dev.berete.gameres.ui.utils.logo
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.sql.Timestamp
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Composable
fun GameResLogo(style: TextStyle = MaterialTheme.typography.body1) {
    Text(
        text = buildAnnotatedString {
            append("Game")
            withStyle(SpanStyle(MaterialTheme.colors.primary)) {
                append("Res")
            }
        },
        fontFamily = Sportypo,
        style = style,
    )
}

@Composable
fun PlatformLogos(
    platformTypeList: List<PlatformType>,
    modifier: Modifier = Modifier,
    singleLogoModifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        for (platform in platformTypeList) {
            Image(
                painter = painterResource(platform.logo),
                contentDescription = null,
                modifier = singleLogoModifier
                    .height(12.dp)
                    .padding(end = 8.dp),
            )
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = Color.Gray.copy(0.05F),
        shape = MaterialTheme.shapes.small,
    ) {
        Column {
            Image(
                painter = rememberCoilPainter(
                    request = game.bannerUrl,
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
                PlatformLogos(
                    platformTypeList = remember(game.id) {
                        game.platformList.groupBy { it.platformType }.keys.toList()
                    },
                )
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
    borderWidth: Dp = (1.5).dp,
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
        modifier = modifier
            .border(width = borderWidth, color = scoreColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = (3.5).dp, vertical = 2.dp)
    )
}


@Composable
fun GameResTopAppBar(
    title: @Composable () -> Unit,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_btn_content_description),
            modifier = Modifier.padding(end = 8.dp),
        )
    },
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu_button_content_description),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        },
        actions = actions,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 1.dp
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun NavDrawer(navController: NavController, modifier: Modifier = Modifier) {
    val currentDate = Clock.System.now()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Image(
            painter = painterResource(id = R.drawable.nav_drawer_header),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
        )

        Divider(modifier = Modifier.padding(top = 8.dp))

        Row(Modifier.padding(horizontal = 16.dp).padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Home",
                style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                modifier = Modifier.clickable { navController.navigate(Routes.Home) }
            )
        }
        Row(Modifier.padding(horizontal = 16.dp).padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Search",
                style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                modifier = Modifier.clickable { navController.navigate(Routes.Search) }
            )
        }

        Row(Modifier.padding(horizontal = 16.dp).padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.upcoming_release_icon),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(19.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                "Upcoming Releases",
                style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
            )
        }

        Row(Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.new_release_icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .size(19.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "New Games",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                )
                Text(
                    "Last 7 days",
                    Modifier
                        .clickable {
                            navController.navigate(
                                Routes.newGames(
                                    currentDate
                                        .minus(Duration.days(7))
                                        .toEpochMilliseconds(),
                                    "Released in the last 7 days",
                                )
                            )
                        }
                        .padding(vertical = 8.dp),
                )
                Text(
                    "Last 30 days",
                    Modifier
                        .clickable {
                            navController.navigate(
                                Routes.newGames(
                                    currentDate
                                        .minus(Duration.days(30))
                                        .toEpochMilliseconds(),
                                    "Released in the last 30 days",
                                )
                            )
                        }
                        .padding(vertical = 8.dp),
                )
                Text(
                    "This year",
                    Modifier
                        .clickable {
                            navController.navigate(
                                Routes.newGames(getYearTimestamp(), "Released this year"),
                            )
                        }
                        .padding(vertical = 8.dp),
                )
            }
        }

        Row(Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.top_100_icon),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 3.dp)
                    .size(20.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "Top 100",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                )

                Text(
                    "All time",
                    Modifier
                        .clickable { }
                        .padding(vertical = 8.dp),
                )
                Text(
                    "This year",
                    Modifier
                        .clickable { }
                        .padding(vertical = 8.dp),
                )
                Text(
                    "Last 5 years",
                    Modifier
                        .clickable { }
                        .padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
fun Tabs(titles: List<String>, onTabSelected: (selectedTabTitle: String) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                height = 2.dp,
                modifier = Modifier
                    .tabIndicatorOffset((tabPositions[selectedTabIndex]))
                    .width(3.dp),
                color = MaterialTheme.colors.primary,
            )
        },
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                selected = index == selectedTabIndex,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.87F),
                text = { Text(text = title, fontSize = 13.sp) },
                onClick = {
                    if (selectedTabIndex != index) {
                        selectedTabIndex = index
                        onTabSelected(title)
                    }
                },
            )
        }
    }
}