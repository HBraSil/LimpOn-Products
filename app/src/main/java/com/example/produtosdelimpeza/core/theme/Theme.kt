package com.example.produtosdelimpeza.core.theme

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
    primary = Color(0xFF3E556B),
    onPrimary = Color(0xFF002233),
    secondary = Color(0xFF1C3856),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF002233),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1C3856),
    onSurface = Color(0xFFC2D7DA)
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xE6A4BED0),
    onPrimary = Color(0x9CAFADAD),
    secondary = Color(0xFF0E4A94),
    onSecondary = Color(0xFF04129F),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xDA0F0C52),
    surface = LightBluishGray,
    onSurface = Color(0xFF00000A),
    surfaceVariant = Color(0xFFB4248B)
)

@Composable
fun ProdutosDeLimpezaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}