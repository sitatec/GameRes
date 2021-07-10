package dev.berete.gameres.ui.screens.game_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.GameGenre
import dev.berete.gameres.ui.screens.PlatformLogos
import dev.berete.gameres.ui.screens.home.GameScore
import dev.berete.gameres.ui.theme.DarkBlue
import dev.berete.gameres.ui.utils.FakeGame
import dev.berete.gameres.ui.utils.bannerUrl

@Composable
fun GameDetailsScreen(gameId: Long, viewModel: GameDetailsViewModel, navController: NavController) {
    val game by viewModel.getGame(gameId).observeAsState()

    if (game != null) {
        GameDetailsScreenBody(game = game!!, navController = navController)
    } else {
        GameDetailsScreenPlaceHolder()
    }
}

@Composable
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

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                buildAnnotatedString {
                    // Game genres
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) {
                        append("Genre: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 13.sp)) {
                        append(game.genres
                            .filter { it != GameGenre.OTHERS }
                            .joinToString { it.toString() })
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    // Mode
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) {
                        append("\nMode: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 13.sp)) {
                        append(game.gameModes.joinToString { it.toString() })
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    // Player Perspective
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) {
                        append("\nPlayer Perspective: ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 13.sp)) {
                        append(game.playerPerspectives.joinToString { it.toString() })
                    }
                }
            )
        }


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