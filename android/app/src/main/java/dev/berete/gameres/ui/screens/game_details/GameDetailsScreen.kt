package dev.berete.gameres.ui.screens.game_details

import android.util.Log
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.*
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.ui.screens.PlatformLogos
import dev.berete.gameres.ui.screens.home.GameScore
import dev.berete.gameres.ui.utils.allImageUrls
import dev.berete.gameres.ui.utils.bannerUrl
import kotlin.math.absoluteValue

@Composable
@ExperimentalPagerApi
fun GameDetailsScreen(viewModel: GameDetailsViewModel, navController: NavController) {
    val game by viewModel.game.observeAsState()

    if (game != null) {
        GameDetailsScreenBody(game = game!!, navController = navController)
    } else {
        GameDetailsScreenPlaceHolder()
    }
}

@Composable
@ExperimentalPagerApi
fun GameDetailsScreenBody(game: Game, navController: NavController) {
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

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Text(
                buildAnnotatedString {
                    // Game genres
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("Genre: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.genres
                            .filter { it != GameGenre.OTHERS }
                            .joinToString { it.toString() })
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    // Mode
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("\nMode: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.gameModes.joinToString { it.toString() })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Player Perspective
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)) {
                        append("\nPlayer Perspective: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp)) {
                        append(game.playerPerspectives.joinToString { it.toString() })
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = game.summary,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.onSurface.copy(0.8f))

            if (game.videoList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Videos",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(Modifier.height(10.dp))

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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Storyline",
                    style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
                    modifier = Modifier.padding(start = 16.dp),
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = game.storyline,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colors.onSurface.copy(0.8f),
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Images",
            style = MaterialTheme.typography.h6.copy(fontSize = 17.sp),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(10.dp))

        val pagerState =
            rememberPagerState(pageCount = game.allImageUrls.size, initialOffscreenLimit = 2)
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

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )

    }
}

@Composable
fun GameDetailsScreenPlaceHolder() {

}

//@Preview
//@Composable
//fun ScreenBodyPreview() {
//    GameDetailsScreenBody(game = FakeGame, navController = rememberNavController())
//}