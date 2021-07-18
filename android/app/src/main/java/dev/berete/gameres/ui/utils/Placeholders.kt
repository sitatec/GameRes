package dev.berete.gameres.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.berete.gameres.ui.screens.game_details.SectionTitle
import java.util.*

@Composable
fun GameCardPlaceholder(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Gray.copy(0.05F),
        shape = MaterialTheme.shapes.small,
        modifier = modifier.width(100.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
            )
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .fillMaxWidth(0.72f)
                    .height(15.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp, top = 4.dp)
                    .fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )


                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
            }
        }
    }
}

@Composable
fun ReleaseCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        elevation = 5.dp
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .placeholder(visible = true)
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
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(100.dp)
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
            )
            Column {
                Box(
                    modifier = Modifier
                        .height(15.dp)
                        .fillMaxWidth(0.84f)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.5f)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )

                Spacer(Modifier.height(8.dp))

                ProvideTextStyle(
                    value = TextStyle(fontSize = 12.sp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .height(12.dp)
                                .weight(1f)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                )
                        )
                        Spacer(Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .height(12.dp)
                                .weight(1f)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LargeGameCardPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(250.dp, 150.dp),
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
        elevation = 5.dp
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .placeholder(visible = true),
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
                .padding(bottom = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(100.dp)
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
            )
            Column {
                Box(
                    modifier = Modifier
                        .height(15.dp)
                        .fillMaxWidth(0.84f)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.5f)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
            }
        }
    }
}

@Composable
fun GameDetailsPlaceholder() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), false)
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight * 0.4).dp),
            contentAlignment = Alignment.BottomCenter,
        ) {

            Box(
                Modifier
                    .fillMaxSize()
                    .placeholder(visible = true),
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
                    .padding(bottom = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(125.dp)
                        .padding(horizontal = 8.dp)
                        .clip(MaterialTheme.shapes.small)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        ),
                )
                Column(Modifier.fillMaxWidth(0.7f)) {
                    Box(
                        modifier = Modifier
                            .height(15.dp)
                            .fillMaxWidth(0.7f)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            )
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .fillMaxWidth(0.45f)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            )
                    )
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(32.dp)
                            .padding(end = 8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        repeat(4) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
                Spacer(Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                )
            }

            Spacer(Modifier.height(7.dp))
        }

        Spacer(Modifier.height(25.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1.6f)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        val random = remember { Random() }
        repeat(10) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(
                        // Randomize the line width to simulate a long text
                        (random.nextFloat()).coerceAtMost(0.3f) + 0.7f,
                    )
                    .height(14.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        SectionTitle(
            title = "Videos",
            modifier = Modifier
                .padding(start = 16.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
        )

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState(), false),
        ) {
            repeat(5) {
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        )
                        .width(280.dp)
                        .aspectRatio(16f / 9)
                )
            }
        }


        SectionTitle(
            title = "Storyline",
            modifier = Modifier
                .padding(start = 16.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
        )

        repeat(20) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(
                        // Randomize the line width to simulate a long text
                        (random.nextFloat()).coerceAtMost(0.3f) + 0.7f,
                    )
                    .height(14.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}