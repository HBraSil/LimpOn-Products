package com.example.produtosdelimpeza.core.auth.data

import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.exceptions.NoCredentialException
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

    override suspend fun registerUser(name: String, lastName: String, email: String, password: String): LoginResponse {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            val uid = firebaseUser?.uid ?: return LoginResponse.Error("Erro ao criar usuário")


            val user = User(uid = uid, name = name, email = email)
            firestore.collection("users")
                .document(uid)
                .set(user)
                .await()


            firebaseUser.sendEmailVerification().await()
            LoginResponse.Success

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResponse.Error("Erro ao fazer login: senha ou email incorretos")
        }  catch (e: FirebaseAuthUserCollisionException) {
            LoginResponse.Error("Email já existe")
        } catch (e: Exception) {
            LoginResponse.Error("Erro ao fazer login: senha ou email incorretos")
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
        try {
            val firebaseCredential = signInWithGoogleApi.firebaseAuthCredential()

            if (firebaseCredential == null) {
                emit(LoginResponse.Error("Credenciais do Google não encontradas"))
                return@flow
            }

            emit(LoginResponse.Loading)

            // 1. Tentar Autenticar no Firebase
            val signinResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val firebaseUser = signinResult.user ?: throw Exception("Usuário não encontrado após login")

            // 2. Verificar se o usuário já existe localmente ou precisa ser criado
            val existingUser = userLocalDataSource.getUserById(firebaseUser.uid)

            if (existingUser == null) {
                val userProperties = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: ""
                )

                // Salva em ambos (Se um falhar, o catch abaixo captura)
                saveUserInRoom(userProperties)
                saveUserInFirestore(userProperties, firebaseUser)
            }

            emit(LoginResponse.Success)

        } catch (e: NoCredentialException) {
            Log.e("GOOGLE_AUTH", "Erro no login: ${e.message}")
            emit(LoginResponse.Error("Nenhuma conta no dispositivo detectada"))
        } catch (e: Exception) {
            Log.e("GOOGLE_AUTH", "Erro no login: ${e.message}")
            emit(LoginResponse.Error(e.message ?: "Erro desconhecido ao fazer login"))
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