package dev.berete.gameres.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = LightGreen,
    primaryVariant = LightGreen,
    secondary = LightGreen,
    onSecondary = Color.White,
    secondaryVariant = LightGreen,
    surface = DarkBlue,
    onSurface = Color.White,
    background = DarkBlue,
    onBackground = Color.White,
    error = Red800,
    onError = Color.White
)

//private val LightColorPalette = lightColors(
//    primary = Purple500,
//    primaryVariant = Purple700,
//    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
//)

@Composable
fun GameResTheme(content: @Composable () -> Unit) {
    // TODO setup LightColorPalette

    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}