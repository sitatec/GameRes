package dev.berete.gameres.ui.screens.shared.components

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.Release
import dev.berete.gameres.domain.models.enums.PlatformType
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.theme.*
import dev.berete.gameres.ui.utils.bannerUrl
import dev.berete.gameres.ui.utils.getYearTimestamp
import dev.berete.gameres.ui.utils.logo
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

const val TopAppBarHeight = 50
const val TopAppBarVerticalPadding = 8

@Composable
fun GameResLogo(modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.body1) {
    Text(
        text = buildAnnotatedString {
            append("Game")
            withStyle(SpanStyle(MaterialTheme.colors.primary)) {
                append("Res")
            }
        },
        fontFamily = Sportypo,
        style = style,
        modifier = modifier,
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
            Icon(
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
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    fadeIn = true,
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 0.dp),
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
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
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
            .padding(horizontal = 16.dp, vertical = TopAppBarVerticalPadding.dp)
            .height(TopAppBarHeight.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 1.dp
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun NavDrawer(navController: NavController, state: DrawerState, modifier: Modifier = Modifier) {
    val currentDate = Clock.System.now()
    val coroutineScope = rememberCoroutineScope()
    val closeDrawer = {
        coroutineScope.launch {
            state.close()
        }
    }

    Card(elevation = 8.dp) {
        Column(
            modifier
                .fillMaxHeight()
                .width((LocalConfiguration.current.screenWidthDp * 0.75).dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.surface),
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_drawer_header),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
            )

            Divider(Modifier.padding(top = 8.dp))

            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Home",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.Home)
                            closeDrawer()
                        }
                        .padding(8.dp)
                )
            }
            Row(
                Modifier
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Search",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.Search)
                            closeDrawer()
                        }
                        .padding(8.dp)
                )
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upcoming_release_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(19.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    "Upcoming Releases",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Routes.UpcomingReleases)
                            closeDrawer()
                        }
                        .padding(8.dp)
                )
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 11.dp)
            ) {
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
                                closeDrawer()
                            }
                            .padding(8.dp),
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
                                closeDrawer()
                            }
                            .padding(8.dp),
                    )
                    Text(
                        "This year",
                        Modifier
                            .clickable {
                                navController.navigate(
                                    Routes.newGames(getYearTimestamp(), "Released this year"),
                                )
                                closeDrawer()
                            }
                            .padding(8.dp),
                    )
                }
            }

            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 13.dp)
            ) {
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
                        "Popular Games",
                        style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    )

                    Text(
                        "This year",
                        Modifier
                            .clickable {
                                navController.navigate(
                                    Routes.popularGames(getYearTimestamp(), "Released this year")
                                )
                                closeDrawer()
                            }
                            .padding(8.dp),
                    )
                    Text(
                        "Last 5 years",
                        Modifier
                            .clickable {
                                coroutineScope.launch {
                                    state.close()
                                }
                                navController.navigate(
                                    Routes.popularGames(
                                        getYearTimestamp(currentDate.toLocalDateTime(TimeZone.currentSystemDefault()).year - 5),
                                        "Released in the last 5 years"
                                    )
                                )
                            }
                            .padding(8.dp),
                    )
                    Text(
                        "All time",
                        Modifier
                            .clickable {
                                navController.navigate(
                                    Routes.popularGames(0, " ")
                                )
                                closeDrawer()
                            }
                            .padding(8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ReleaseCard(
    release: Release,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier.clickable { onClick() },
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        elevation = 5.dp
    ) {
        Image(
            painter = rememberCoilPainter(
                request = release.artWorkUrl,
                previewPlaceholder = R.drawable.apex_legends_artwork,
                fadeIn = true,
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colors.surface
                        )
                    )
                )
                .padding(vertical = 8.dp),
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = release.gameCoverUrl,
                    previewPlaceholder = R.drawable.apex_legends_cover,
                    fadeIn = true,
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.small),
            )
            Column {
                Text(
                    text = release.gameName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    release.formattedDate, color = MaterialTheme.colors.primary,
                    fontSize = 14.sp,
                )
                ProvideTextStyle(
                    value = TextStyle(fontSize = 12.sp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(release.region)
                        Text(
                            release.platform.name,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                    }
                }
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

@Composable
fun CollapsingTabBarScaffold(
    topAppBar: @Composable () -> Unit,
    drawerState: DrawerState,
    drawerContent: @Composable ColumnScope.() -> Unit,
    drawerBackgroundColor: Color = Color.Transparent,
    drawerScrimColor: Color = MaterialTheme.colors.surface.copy(0.63f),
    drawerElevation: Dp = 0.dp,
    scaffoldContent: @Composable () -> Unit,
) {
    // TODO Refactor
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val totalTopAppBarHeight =
        remember { (TopAppBarHeight + (TopAppBarVerticalPadding * 2)).toFloat() }
    val localDensity = LocalDensity.current

    ModalDrawer(
        drawerState = drawerState,
        drawerBackgroundColor = drawerBackgroundColor,
        scrimColor = drawerScrimColor,
        drawerElevation = drawerElevation,
        drawerContent = drawerContent,
        content = {
            Box(
                Modifier
                    .verticalScroll(scrollState)
                    .nestedScroll(object : NestedScrollConnection {
                        var yOffset = 0f
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            if (available.y == 0f || source != NestedScrollSource.Drag) return Offset.Zero

                            return if (available.y > 0) {
                                if (yOffset == 0f) return Offset.Zero
                                val tempOffset = available.y - yOffset
                                val dragConsumed = yOffset + tempOffset
                                yOffset = (yOffset + dragConsumed).coerceAtMost(0f)

                                coroutineScope.launch {
                                    scrollState.animateScrollTo(-localDensity.run {
                                        yOffset.toInt().dp
                                            .toPx()
                                            .toInt()
                                    }, animationSpec = SpringSpec(stiffness = 500f))
                                }

                                Offset(0f, dragConsumed)

                            } else {
                                if (yOffset <= -totalTopAppBarHeight) return Offset.Zero
                                val previousOffset = yOffset
                                yOffset =
                                    -((previousOffset.absoluteValue + available.y.absoluteValue).coerceAtMost(
                                        totalTopAppBarHeight
                                    ))

                                coroutineScope.launch {
                                    scrollState.animateScrollTo(localDensity.run {
                                        yOffset.toInt().dp
                                            .toPx()
                                            .toInt()
                                    }.absoluteValue, animationSpec = SpringSpec(stiffness = 500f))
                                }
                                val dragConsumed = (yOffset.absoluteValue - previousOffset.absoluteValue)
                                Offset(0f, -dragConsumed)
                            }
                        }
                    })
                    .height((LocalConfiguration.current.screenHeightDp + totalTopAppBarHeight).dp),
//            .offset(y = yOffsetDp.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column {
                    topAppBar()
                    scaffoldContent()
                }
            }
        }
    )
}