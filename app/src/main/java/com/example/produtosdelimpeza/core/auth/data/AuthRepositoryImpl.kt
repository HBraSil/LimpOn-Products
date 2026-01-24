package com.example.produtosdelimpeza.core.auth.data

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.produtosdelimpeza.connection.NetworkUtils
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkUtils: NetworkUtils,
    @ApplicationContext private val context: Context
) : AuthRepository {
    data class RegistrationException(override var message: String) : Exception(message)


    override suspend fun registerUser(name: String, lastName: String, email: String, password: String) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            val uid = firebaseUser?.uid ?: throw RegistrationException("Erro")


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


    override suspend fun signInWithEmailAndPassword(email: String, password: String): LoginResponse {
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

    override suspend fun signInWithGoogle(): LoginResponse {
        return try {

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("401816894466-b142p7vdsg4v1c0chkbfec89j28g0spg.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .setNonce(generateNonce())
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = (credential as GoogleIdTokenCredential).idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential, null)

                val signinResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
                val user = signinResult.user

                if (user?.isEmailVerified != null) LoginResponse.Success
                else LoginResponse.Error("Login não foi bem sucedido")
            } else {
                LoginResponse.Error("Usuário nulo após login")
            }
        } catch (e: Exception) {
            LoginResponse.Error("Erro ao fazer login: senha ou email incorretos")
        }
    }


    fun generateNonce(
        size: Int = 32
    ): String {
        require(size in 16..500) {
            "Nonce size must be between 16 and 500 characters"
        }

        val randomBytes = ByteArray(size)
        SecureRandom().nextBytes(randomBytes)

        return Base64.encodeToString(
            randomBytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    suspend fun processEmailVerificationDeepLink(deepLinkUrl: String): Result<Boolean> {
        val uri = deepLinkUrl.toUri()
        val mode = uri.getQueryParameter("mode")
        val oobCode = uri.getQueryParameter("oobCode")

        if (mode != "verifyEmail" || oobCode.isNullOrEmpty()) {
            return Result.failure(Exception("Link inválido"))
        }

        return try {
            firebaseAuth.applyActionCode(oobCode).await()
            firebaseAuth.currentUser?.reload()?.await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}