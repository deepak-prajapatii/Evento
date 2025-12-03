package com.evento.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrightTeal,
    onPrimary = NearBlack,
    secondary = SteelBlue,
    onSecondary = CoolWhite,
    tertiary = DeepForestTeal,
    onTertiary = Aqua,
    background = CharcoalDark,
    onBackground = OffWhiteDark,
    surface = NavyBlue,
    onSurface = SoftWhite,
    error = Crimson,
    onError = OffWhiteDark,
    outline = Graphite,
    surfaceVariant = SteelGray,
    onSurfaceVariant = FrostGray,
    inversePrimary = Aqua,
    scrim = Color.Black,
)


private val LightColorScheme = lightColorScheme(
    primary = Teal,
    onPrimary = PureWhite,
    secondary = SoftGray,
    onSecondary = SlateGray,
    tertiary = Mint,
    onTertiary = DeepTeal,
    background = OffWhite,
    onBackground = Charcoal,
    surface = PureWhite,
    onSurface = Charcoal,
    error = Red,
    onError = PureWhite,
    outline = LightBorderGray,
    surfaceVariant = MistGray,
    onSurfaceVariant = CoolGray,
    inversePrimary = DeepTeal,
    scrim = Color.Black,
)


@Composable
fun EventoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}