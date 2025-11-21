package com.example.produtosdelimpeza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.produtosdelimpeza.compose.ProdutosLimpezaApp
import com.example.produtosdelimpeza.ui.theme.ProdutosDeLimpezaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            ProdutosDeLimpezaTheme {
                enableEdgeToEdge()
                // Obtenha a Activity e a cor desejada (Branco para o fundo)
                val activity = LocalActivity.current as ComponentActivity
                val statusBarColor = Color.Transparent.toArgb() // Fundo da Status Bar BRANCO

                // Configuração para forçar os itens (ícones e texto) a serem pretos
                DisposableEffect(Unit) {

                    // 1. Define o estilo da Status Bar
                    val statusBarStyle = SystemBarStyle.light(
                        scrim = statusBarColor, // Define o fundo da Status Bar como BRANCO
                        darkScrim = statusBarColor
                        // Não precisamos definir darkContent, pois SystemBarStyle.light()
                        // já define que os ícones devem ser ESCUROS por padrão.
                    )

                    // 2. Aplica o novo estilo
                    activity.enableEdgeToEdge(
                        // Aplica o novo estilo à Status Bar
                        statusBarStyle = statusBarStyle,

                        // Mantém a Navigation Bar com o estilo que você já tinha (ou o padrão)
                        navigationBarStyle = SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb()
                        )
                    )

                    onDispose {
                        // 3. Ao sair, você deve restaurar o estilo definido na MainActivity (ou o padrão)
                        activity.enableEdgeToEdge(
                            // Restaurar para a configuração padrão da sua app ou da MainActivity
                            // Neste caso, vamos restaurar apenas o comportamento padrão.
                            statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
                            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
                        )
                    }
                }

                ProdutosLimpezaApp()
            }
        }
    }
}