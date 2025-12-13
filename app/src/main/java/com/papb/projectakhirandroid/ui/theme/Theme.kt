package com.papb.projectakhirandroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green,
    primaryVariant = Green, // Anda bisa menggunakan varian hijau yang lebih gelap jika ada
    secondary = Green, // Atau varian lain
    background = Color.Black, // Contoh warna latar belakang untuk mode gelap
    surface = Color.DarkGray, // Contoh warna permukaan untuk mode gelap
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Green,
    primaryVariant = Green, // Anda bisa menggunakan varian hijau yang lebih gelap jika ada
    secondary = Green, // Atau varian lain
    background = GrayBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun GroceriesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = TypographyStyle,
        shapes = Shapes,
        content = content,
    )
}