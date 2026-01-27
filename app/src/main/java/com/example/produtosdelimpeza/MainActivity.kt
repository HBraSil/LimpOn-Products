package com.example.produtosdelimpeza

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.core.auth.presentation.login.LoginViewModel
import com.example.produtosdelimpeza.core.navigation.LimpOnAppNavigation
import com.example.produtosdelimpeza.core.navigation.route.NavGraph
import com.example.produtosdelimpeza.core.theme.ProdutosDeLimpezaTheme
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import dagger.hilt.android.AndroidEntryPoint
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


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
                val statusBarColor = Color.Transparent.toArgb()

                DisposableEffect(Unit) {
                    val statusBarStyle = SystemBarStyle.light(
                        scrim = statusBarColor,
                        darkScrim = statusBarColor
                    )

                    activity.enableEdgeToEdge(
                        statusBarStyle = statusBarStyle,

                        navigationBarStyle = SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb()
                        )
                    )

                    onDispose {
                        activity.enableEdgeToEdge(
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