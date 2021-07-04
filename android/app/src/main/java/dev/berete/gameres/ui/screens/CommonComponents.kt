package dev.berete.gameres.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.domain.models.enums.Platform
import dev.berete.gameres.ui.theme.GameResTheme
import dev.berete.gameres.ui.theme.Sportypo
import dev.berete.gameres.ui.utils.FakeGame
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
    platformList: List<Platform>,
    modifier: Modifier = Modifier,
    singleLogoModifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        for (platform in platformList) {
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
