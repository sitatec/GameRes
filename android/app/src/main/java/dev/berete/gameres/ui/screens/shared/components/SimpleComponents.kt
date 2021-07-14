package dev.berete.gameres.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.PlatformType
import dev.berete.gameres.ui.theme.*
import dev.berete.gameres.ui.utils.bannerUrl
import dev.berete.gameres.ui.utils.logo

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

