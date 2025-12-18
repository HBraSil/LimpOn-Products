package com.example.produtosdelimpeza.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.produtosdelimpeza.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import com.example.produtosdelimpeza.connection.NetworkUtils

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkUtils: NetworkUtils
) {
    data class RegistrationException(override var message: String) : Exception(message)

    suspend fun registerUser(name: String, lastName: String, email: String, password: String) {
        try {
            // 1. Cria o usuário e aguarda o resultado. Erros vão para o catch.
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            val uid = firebaseUser?.uid ?: throw RegistrationException("Erro")


            // 2. Cria o objeto e salva no Firestore, aguardando a conclusão.
            val user = User(uid = uid, name = name, lastName = lastName, email = email)
            firestore.collection("users")
                .document(uid)
                .set(user)
                .await()

            try {
                firebaseUser.sendEmailVerification().await()
                Log.e("EMAIL", "Email enviado com sucesso!")
            } catch (e: Exception) {
                Log.e("EMAIL", "Falha ao enviar email: ${e.message}")
            }

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw RegistrationException("O e-mail fornecido é inválido ou a senha é muito fraca. Por favor, verifique.")
        } catch (e: FirebaseAuthUserCollisionException) {
            throw RegistrationException("Este e-mail já está cadastrado. Tente fazer login ou use outro e-mail.")
        } catch (e: Exception) {
            throw RegistrationException("Falha ao registrar. Verifique sua conexão e tente novamente.")
        }
    }


    suspend fun signIn(email: String, password: String): LoginResponse {
        return try {
            val isInternetAvailable = networkUtils.isInternetAvailable()
            Log.d("INTERNET", "status internet: $isInternetAvailable")
            if (isInternetAvailable) {
                val signInResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = signInResult.user
                val isEmailVerified = user?.isEmailVerified

                if (signInResult != null && isEmailVerified == true) LoginResponse.Success
                else LoginResponse.Error("Login não foi bem sucedido")

            } else {
                LoginResponse.Error("Sem internet")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResponse.Error("Erro ao fazer login: senha ou email incorretos")
        }
    }

    suspend fun processEmailVerificationDeepLink(deepLinkUrl: String): Result<Boolean> {
        val uri = deepLinkUrl.toUri()
        val mode = uri.getQueryParameter("mode") // ex: verifyEmail
        val oobCode = uri.getQueryParameter("oobCode")

        if (mode != "verifyEmail" || oobCode.isNullOrEmpty()) {
            return Result.failure(Exception("Link inválido"))
        }

        return try {
            // Aplica o action code (marca o email como verificado no servidor)
            firebaseAuth.applyActionCode(oobCode).await()

            // Recarrega o usuário atual pra atualizar isEmailVerified
            firebaseAuth.currentUser?.reload()?.await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
