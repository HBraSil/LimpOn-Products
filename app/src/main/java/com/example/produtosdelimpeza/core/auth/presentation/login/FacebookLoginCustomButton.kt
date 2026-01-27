package com.example.produtosdelimpeza.core.auth.presentation.login

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

@Composable
fun FacebookLoginCustomButton(onClick: () -> Unit) {
    val context = LocalContext.current
    val loginManager = com.facebook.login.LoginManager.getInstance()
    val callbackManager = CallbackManager.Factory.create()

    Button(
        onClick = {
            loginManager.logInWithReadPermissions(
                context as Activity,
                listOf("public_profile", "email")
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text("Login com Facebook")
    }

    // Registrar callback (precisa estar no Activity)
    loginManager.registerCallback(callbackManager,
        callback = object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FBLogin", "Login sucesso: ${result.accessToken.token}")
            }

            override fun onCancel() {
                Log.d("FBLogin", "Login cancelado")
            }

            override fun onError(error: FacebookException) {
                Log.e("FBLogin", "Erro login: ${error.message}")
            }
        }
    )
}
