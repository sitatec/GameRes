package dev.berete.gameres.ui.screens.game_details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.*
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.GameCompany
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.ui.Routes
import dev.berete.gameres.ui.screens.GameCard
import dev.berete.gameres.ui.screens.GameScore
import dev.berete.gameres.ui.screens.PlatformLogos
import dev.berete.gameres.ui.utils.allImageUrls
import dev.berete.gameres.ui.utils.bannerUrl
import dev.berete.gameres.ui.utils.formattedInitialReleaseDate
import kotlin.math.absoluteValue

@Composable
@ExperimentalPagerApi
fun GameDetailsScreen(viewModel: GameDetailsViewModel, navController: NavController) {
    val game by viewModel.game.observeAsState()
    val similarGames by viewModel.similarGames.observeAsState(emptyList())

    if (game != null) {
        GameDetailsScreenBody(game = game!!, similarGames, navController = navController)
    } else {
        GameDetailsScreenPlaceHolder()
    }
}

@Composable
@ExperimentalPagerApi
fun GameDetailsScreenBody(game: Game, similarGames: List<Game>, navController: NavController) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Box(contentAlignment = Alignment.BottomStart) {
            Image(
                painter = rememberCoilPainter(request = game.bannerUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colors.surface)),
                )
                .padding(top = 48.dp)) {

                Row(verticalAlignment = Alignment.Bottom) {
                    Image(
                        painter = rememberCoilPainter(request = game.coverUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 8.dp)
                            .clip(MaterialTheme.shapes.small),
                    )
                    Column {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onSurface),
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
                                platformList = game.platformList,
                                singleLogoModifier = Modifier.height(14.dp),
                            )
                            Spacer(Modifier.width(16.dp))
                            GameScore(
                                score = game.rating.toInt(),
                                style = MaterialTheme.typography.subtitle2,
                                modifier = Modifier.padding(end = 8.dp),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                buildAnnotatedString {
                    // Game genres
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("Genre: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(
                            game.genres
                                .filter { it != GameGenre.OTHERS }
                                .joinToString { it.toString() },
                        )
                    }
                    // Mode
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("\nMode: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.gameModes.joinToString { it.toString() })
                    }
                    // Player Perspective
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("\nPlayer Perspective: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.playerPerspectives.joinToString { it.toString() })
                    }
                    // First Release date
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("\nInitial Release: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.formattedInitialReleaseDate)
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 21.sp,
            )

            val pagerState = rememberPagerState(
                pageCount = game.allImageUrls.size,
                initialOffscreenLimit = 2,
                initialPage = 1, // Initial image is the second in the list because the first
                // may be show in the header. And by starting at with the second image, it will be more
                // intuitive for the user to know that he can swipe on the image without showing a indicator.
            )

            Spacer(Modifier.height(25.dp))
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { pageIndex ->
                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(pageIndex).absoluteValue

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1.5f),
                ) {
                    Image(
                        painter = rememberCoilPainter(request = game.allImageUrls[pageIndex]),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.TopCenter,
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = game.summary,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.onSurface.copy(0.8f))

            if (game.videoList.isNotEmpty()) {
                SectionTitle("Videos")

                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    for (video in game.videoList) {
                        Spacer(Modifier.width(16.dp))
                        Card(Modifier.size(280.dp, 160.dp)) {
                            Image(
                                painter = rememberCoilPainter(request = video.thumbnailUrl),
                                contentDescription = video.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Surface(
                                modifier = Modifier.wrapContentSize(Alignment.TopCenter),
                                shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp),
                                color = Color.Black.copy(0.7f),
                            ) {
                                Text(
                                    text = video.title,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (game.storyline.isNotBlank()) {
                SectionTitle("Storyline")
                Text(
                    text = game.storyline,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colors.onSurface.copy(0.8f),
                )
            }
        }

//        SectionTitle("Medias")


        SectionTitle("Recommendations")

        Row(Modifier.horizontalScroll(rememberScrollState())) {
            for (similarGame in similarGames) {
                Spacer(Modifier.width(16.dp))
                GameCard(similarGame, modifier = Modifier.size(220.dp, 190.dp), onClick = {
                    navController.navigate(Routes.gameDetails(similarGame.id))
                })
            }
        }

        Spacer(modifier = Modifier.height(35.dp))
        FlowRow(
            mainAxisSpacing = 20.dp,
            crossAxisSpacing = 10.dp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .alpha(0.95f),
        ) {
            for (website in game.websiteList) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberCoilPainter(website.logoUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(23.dp)
                            .clip(MaterialTheme.shapes.small),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(text = website.name)
                }
            }
        }

        SectionTitle("Platforms", spaceAfter = 8.dp, spaceBefore = 30.dp)

        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 3.dp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .alpha(0.9f),
        ) {
            for ((index, platform) in game.platformList.withIndex()) {
                Row {
                    Text(platform.platformName, color = MaterialTheme.colors.primary)
                    if (index < game.platformList.lastIndex) {
                        Text(",")
                    }
                }
            }
        }

        SectionTitle("Releases")
        
        Column{
            Row {
                TableTitleCell("Date")
                TableTitleCell("Platform", 1.2f)
                TableTitleCell("Region")
            }
            for(release in game.releases){
                Row(Modifier.border(1.dp, Color.DarkGray)) {
                    TableCell(release.formattedDate)
                    TableCell(release.platform.platformName, 1.2f)
                    TableCell(release.region)
                }
            }
        }

        SectionTitle("Age Rating")

        Row(Modifier
            .horizontalScroll(rememberScrollState())
            .alpha(0.9f)) {
            for (ageRating in game.ageRatings) {
                Spacer(Modifier.width(16.dp))
                Image(painter = rememberCoilPainter(ageRating.labelUrl), contentDescription = null)
            }
        }


        SectionTitle("Developer")

        Column(Modifier.padding(horizontal = 16.dp)) {
            for (developer in game.developers) {
                CompanyCard(company = developer)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        SectionTitle("Publisher")

        Column(Modifier.padding(horizontal = 16.dp)) {
            for (publisher in game.publishers) {
                CompanyCard(company = publisher)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun GameDetailsScreenPlaceHolder() {

}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    spaceBefore: Dp = 24.dp,
    spaceAfter: Dp = 16.dp,
) {
    Spacer(modifier = Modifier.height(spaceBefore))
    Text(
        text = title,
        style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
        modifier = modifier.padding(start = 16.dp),
    )
    Spacer(Modifier.height(spaceAfter))
}

@Composable
fun CompanyCard(company: GameCompany, modifier: Modifier = Modifier) {
    Card(elevation = 1.dp, modifier = modifier) {
        Row(Modifier.padding(5.dp)) {
            Image(
                rememberCoilPainter(company.logoUrl),
                contentDescription = null,
                Modifier.fillMaxHeight(),
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(8.dp),
            ) {
                Text(text = company.name)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = company.country, color = Color.White.copy(0.6f))
                    Text("Learn more", color = MaterialTheme.colors.primary)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableTitleCell(
    text: String,
    weight: Float = 1f
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.DarkGray)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun RowScope.TableCell(text: String, weight: Float = 1f) {
    Text(text = text, Modifier.weight(weight).padding(8.dp))
}

//@Preview
//@Composable
//fun ScreenBodyPreview() {
//    GameDetailsScreenBody(game = FakeGame, navController = rememberNavController())
//}