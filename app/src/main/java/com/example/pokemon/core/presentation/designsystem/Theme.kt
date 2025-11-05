package com.example.pokemon.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = PokemonGreen,
    background = PokemonBlack,
    surface = PokemonDarkGray,
    secondary = PokemonWhite,
    tertiary = PokemonWhite,
    primaryContainer = PokemonGreen30,
    onPrimary = PokemonBlack,
    onBackground = PokemonWhite,
    onSurface = PokemonWhite,
    onSurfaceVariant = PokemonGray,
    error = PokemonDarkRed,
    errorContainer = PokemonDarkRed5
)

@Composable
fun PokemonTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}