package com.ricardaparicio.cryptodemo.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = OrangePeel,
    primaryVariant = BuddhaGold,
    secondary = BurningOrange,
    secondaryVariant = AtomicTangerine,
    onPrimary = Color.White,
    background = MineShaft,
)

private val LightColorPalette = lightColors(
    primary = OrangePeel,
    primaryVariant = LaserLemon,
    secondary = BurningOrange,
    secondaryVariant = Thunderbird,
    onPrimary = Color.Black,
    background = Silver,
)

@Composable
fun CryptoDemoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colors.primaryVariant,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}