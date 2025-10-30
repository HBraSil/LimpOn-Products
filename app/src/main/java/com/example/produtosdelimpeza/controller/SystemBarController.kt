package com.example.produtosdelimpeza.controller

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge

@Composable
fun SystemBarController(
    // Defina esta variável para 'true' se o fundo for CLARO e os ícones precisarem ser ESCUROS.
    // Defina para 'false' se o fundo for ESCURO e os ícones precisarem ser CLAROS (brancos).
    useDarkIcons: Boolean
) {
    val activity = LocalActivity.current as ComponentActivity

    // Obtém a cor de fundo desejada para a Status Bar
    val statusBarColor = Color.Black.toArgb()

    DisposableEffect(useDarkIcons) {
        val style = if (useDarkIcons) {
            // Fundo da Status Bar é CLARO (surface), então use SystemBarStyle.light
            SystemBarStyle.light(
                scrim = statusBarColor,
                darkScrim = statusBarColor,
                // O padrão de light() é darkContent = true, que deixa ícones ESCUROS
            )
        } else {
            // Fundo da Status Bar é ESCURO, então use SystemBarStyle.dark
            SystemBarStyle.dark(
                scrim = statusBarColor,
                // O padrão de dark() é darkContent = false, que deixa ícones CLAROS (brancos)
            )
        }

        // 💡 Aplica o novo estilo que define tanto a cor de fundo (scrim)
        // quanto a cor dos ícones (implícito em light/dark)
        activity.enableEdgeToEdge(
            statusBarStyle = style,
            navigationBarStyle = SystemBarStyle.auto(
                Color.Black.toArgb(),
                Color.Black.toArgb()
            )
        )

        onDispose {
            // Limpeza
            activity.enableEdgeToEdge()
        }
    }
}