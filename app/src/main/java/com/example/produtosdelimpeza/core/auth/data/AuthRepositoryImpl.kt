package com.example.produtosdelimpeza.core.auth.data

import android.util.Log
import androidx.core.net.toUri
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.data.SigninWithGoogleApi
import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkChecker: NetworkChecker,
    private val signInWithGoogleApi: SigninWithGoogleApi,
    private val userLocalDataSource: UserLocalDataSource
) : AuthRepository {
    data class RegistrationException(override var message: String) : Exception(message)

    override suspend fun registerUser(name: String, lastName: String, email: String, password: String) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            val uid = firebaseUser?.uid ?: throw RegistrationException("Erro")


            val user = User(uid = uid, name = name, email = email)
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
            val isInternetAvailable = networkChecker.isInternetAvailable()
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


    override suspend fun signInWithGoogle(): Flow<LoginResponse> = flow {
        val firebaseCredential = signInWithGoogleApi.firebaseAuthCredential()


        if (firebaseCredential != null) {
            emit(LoginResponse.Loading)
            val signinResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val userUid = signinResult.user?.uid ?: return@flow

            if (signinResult.user?.isEmailVerified != null) {
                val existingUser = userLocalDataSource.getUserById(userUid)
                if (existingUser == null) {
                    FirebaseAuth.getInstance().currentUser?.let {
                        val userProperties = with(it) {
                            User(
                                uid = uid,
                                name = displayName ?: "",
                                email = email ?: ""
                            )
                        }
                        saveUserInRoom(userProperties)
                        saveUserInFirestore(userProperties, it)
                    }
                }
                emit(LoginResponse.Success)
            }
            else emit(LoginResponse.Error("Login não foi bem sucedido"))
        } else {
            emit(LoginResponse.Error("Login não foi bem sucedido"))
        }
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

    suspend fun saveUserInFirestore(userProperties: User, currUser: FirebaseUser) {
        firestore.collection("users")
            .document(currUser.uid)
            .set(userProperties)
            .await()
    }

    suspend fun saveUserInRoom(userProperties: User) {
        userLocalDataSource.saveUser(userProperties)
    }
}