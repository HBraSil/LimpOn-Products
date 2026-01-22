package com.example.produtosdelimpeza.core.auth.data

import android.content.ContentValues
import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import com.example.produtosdelimpeza.connection.NetworkUtils
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
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

    private val credentialManager = CredentialManager.create(context)
    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId("401816894466-b142p7vdsg4v1c0chkbfec89j28g0spg.apps.googleusercontent.com")
        .setAutoSelectEnabled(true)
        .setNonce(generateNonce())
        .build()

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

    suspend fun signInWithGoogle() {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        try {
              val result = credentialManager.getCredential(
                  context = context,
                  request = request
              )
            return handleSignIn(result)
        } catch (e: Exception) {

        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        val responseJson: String

        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse to your server to validate and
                // authenticate
                responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
                responseJson = "{\"username\": \"$username\", \"password\": \"$password\"}"
                Log.e(ContentValues.TAG, "Password credential: $responseJson")
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.Companion
                            .createFrom(credential.data)
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.
                        // see [validation instructions](https://developers.google.com/identity/gsi/web/guides/verify-google-id-token)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(ContentValues.TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(ContentValues.TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(ContentValues.TAG, "Unexpected type of credential")
            }
        }
    }

    fun generateNonce(
        size: Int = 32 // 32 bytes → ~43 chars Base64 (seguro e dentro do limite)
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