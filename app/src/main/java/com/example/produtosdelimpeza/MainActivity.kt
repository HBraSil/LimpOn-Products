package com.example.produtosdelimpeza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.example.produtosdelimpeza.compose.ProdutosLimpezaApp
import com.example.produtosdelimpeza.ui.theme.LightBluishGray
import com.example.produtosdelimpeza.ui.theme.ProdutosDeLimpezaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProdutosDeLimpezaTheme {
                enableEdgeToEdge(
                    navigationBarStyle = if (isSystemInDarkTheme()) {
                        SystemBarStyle.dark(
                            MaterialTheme.colorScheme.secondary.toArgb() // cor no dark
                        )
                    } else {
                        SystemBarStyle.light(
                            LightBluishGray.toArgb(), // cor no light
                            LightBluishGray.toArgb()  // enforced contrast
                        )
                    }
                )

                ProdutosLimpezaApp()
            }
        }
    }
}
