package com.example.produtosdelimpeza.compose

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.produtosdelimpeza.compose.component.EmailVerifiedScreen
import com.example.produtosdelimpeza.compose.initial.InitialScreen
import com.example.produtosdelimpeza.compose.login.LoginScreen
import com.example.produtosdelimpeza.compose.main.MainScreenNavigation
//import com.example.produtosdelimpeza.compose.product.ProductScreen
import com.example.produtosdelimpeza.compose.signup.SignupCodeScreen
import com.example.produtosdelimpeza.compose.signup.SignupScreen
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProdutosLimpezaApp() {
    val navController = rememberNavController()
    ProdutosLimpezaNavHost(navController = navController)
}

@Composable
fun ProdutosLimpezaNavHost(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser


    val startDestination = when {
        user == null -> Screen.INITIAL.route
        else -> Screen.MAIN.route
    }

    DeepLinkObserver(navController = navController)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.INITIAL.route) {
            InitialScreen(
                onChoiceClick = { navController.navigate(Screen.LOGIN.route) },
                emailVerified = { navController.navigate(Screen.EMAIL_VERIFIED.route) }
            )
        }

        composable(route = Screen.LOGIN.route) {
            LoginScreen(
                onSignupClick = { navController.navigate(Screen.SIGNUP.route) },
                onLoginClick = {
                    navController.navigate(Screen.MAIN.route) {
                        popUpTo(navController.graph.id) {

                            // 2. Remove o destino "login" da pilha de navegação.
                            // Isso garante que ele não estará lá para ser retornado ao pressionar o botão Voltar.
                            inclusive = true
                        }

                        // Opcional: Evita múltiplas cópias do destino 'home' na pilha
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.SIGNUP.route) {
            SignupScreen(
                onBackNavigation = { navController.navigateUp() },
                onToSignupClick = { navController.navigate(Screen.MAIN.route) },
                onEmailLinkValid = {
                    navController.navigate(Screen.EMAIL_VERIFIED.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(route = Screen.SIGNUP_CODE.route) {
            SignupCodeScreen(
                onToSignupClick = { navController.navigateUp() },
                onMainScreenClick = { navController.navigate(Screen.MAIN.route) }
            )
        }

        composable(route = Screen.EMAIL_VERIFIED.route) {
            EmailVerifiedScreen(navController = navController)
        }

        composable(route = Screen.MAIN.route) {
            MainScreenNavigation()
        }
    }
}

@Composable
fun DeepLinkObserver(navController: NavController) {
    val deepLinkViewModel: DeepLinkViewModel = viewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            deepLinkViewModel.receivedLink.collect { link ->
                if (link != null) {
                    navController.navigate(Screen.EMAIL_VERIFIED.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    deepLinkViewModel.consume()
                }
            }
        }
    }
}
