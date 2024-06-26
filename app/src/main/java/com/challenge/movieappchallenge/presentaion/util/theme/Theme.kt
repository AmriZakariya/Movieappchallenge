package com.challenge.movieappchallenge.presentaion.util.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = darkBlue,
    primaryVariant = lightBlue,
    secondary = lightGreen,
    onPrimary = Color.White,
    onSecondary = Color.Black.copy(alpha = .8f)
)

private val LightColorPalette = lightColors(
    primary = darkBlue,
    primaryVariant = lightBlue,
    secondary = lightGreen,
    onPrimary = Color.Black.copy(alpha = .8f),
    onSecondary = Color.Black
)

@Composable
fun MovieAppChallengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
private fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colors.primary
    )
}
