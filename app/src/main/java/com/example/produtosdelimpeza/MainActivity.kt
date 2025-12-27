package com.example.produtosdelimpeza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.compose.user.initial.InitialScreen
import com.example.produtosdelimpeza.navigation.LimpOnAppNavigation
import com.example.produtosdelimpeza.navigation.route.AuthScreen
import com.example.produtosdelimpeza.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.navigation.route.NavGraph
import com.example.produtosdelimpeza.navigation.route.StoreScreen
import com.example.produtosdelimpeza.ui.theme.ProdutosDeLimpezaTheme
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val deepLinkViewModel: DeepLinkViewModel by viewModels()
    private val sessionViewModel: NavigationLastUserModeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
             setKeepOnScreenCondition {
                sessionViewModel.lastUserMode.value == null
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProdutosDeLimpezaTheme {
            val startProfile by sessionViewModel.lastUserMode.collectAsState()
            startProfile?.let { profileMode ->
                val startDestination = when (profileMode) {
                    ProfileMode.LoggedIn.Store -> NavGraph.SELLER_MAIN.route
                    ProfileMode.LoggedIn.Customer -> NavGraph.USER_MAIN.route
                    ProfileMode.LoggedOut -> NavGraph.AUTH.route
                }

                LimpOnAppNavigation(startDestination)
            }
                val activity = LocalActivity.current as ComponentActivity
                val statusBarColor = Color.Transparent.toArgb() // Fundo da Status Bar BRANCO

                DisposableEffect(Unit) {

                    val statusBarStyle = SystemBarStyle.light(
                        scrim = statusBarColor, // Define o fundo da Status Bar como BRANCO
                        darkScrim = statusBarColor
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
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val link = intent.data.toString()
        if (link.isNotEmpty()) deepLinkViewModel.onDeepLinkReceived(link)
    }
}