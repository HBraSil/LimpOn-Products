package com.example.produtosdelimpeza.compose.auth.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.navigation.route.NavGraph
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(
    sessionViewModel: NavigationLastUserModeViewModel = hiltViewModel(),
    onChoiceUserAuth: (String) -> Unit = {},
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val startProfile by sessionViewModel.lastUserMode.collectAsState()

    LaunchedEffect(startProfile) {
        when {
            user == null ->
                onChoiceUserAuth(NavGraph.AUTH.route)

            startProfile.currentMode == ProfileMode.CUSTOMER.mode ->
                onChoiceUserAuth(NavGraph.USER_MAIN.route)

            startProfile.currentMode == ProfileMode.STORE.mode ->
                onChoiceUserAuth(NavGraph.SELLER_MAIN.route)

            else -> Unit // UNKNOWN → não navega ainda
        }
    }
    // Opcional: manter a tela invisível ou com seu logo
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "OLÁ HILQUIAS"
        )
    }
}
