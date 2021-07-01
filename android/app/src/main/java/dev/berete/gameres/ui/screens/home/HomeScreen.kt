package dev.berete.gameres.ui.screens.home

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import dev.berete.gameres.R
import dev.berete.gameres.domain.models.Game
import dev.berete.gameres.ui.screens.PlatformLogos
import dev.berete.gameres.ui.theme.GameResTheme
import dev.berete.gameres.ui.utils.FakeGame


@Composable
fun LargeGameCard(game: Game, onClick: (Game) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .clickable { onClick(game) }
            .size(300.dp, 180.dp)
            .then(modifier),
    ) {
        Image(
            painter = rememberCoilPainter(
                request = game.artWorkUrls.first(),
                fadeIn = true,
                previewPlaceholder = R.drawable.apex_legends_artwork,
            ),
            contentDescription = game.name,
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
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
                    style = MaterialTheme.typography.h6,
                )
                Spacer(Modifier.height(5.dp))
                PlatformLogos(game.platformList)
            }
        }
    }
}

@Preview
@Composable
fun LargeGameCardPreview() {
    GameResTheme {
        LargeGameCard(game = FakeGame, onClick = {})
    }
}