package com.example.produtosdelimpeza.core.auth.data

import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.data.SigninWithGoogleApi
import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.model.User
import com.google.firebase.auth.FacebookAuthProvider
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

    override suspend fun createUserWithEmailAndPassword(name: String, lastName: String, email: String, password: String): LoginResponse {
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


            val signinResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
            val user = signinResult.user ?: throw Exception("Usuário não encontrado após login")
            val existingUser = userLocalDataSource.getUserById(user.uid)

            if (existingUser == null) {
                val userProperties = User(
                    uid = user.uid,
                    name = user.displayName ?: "",
                    email = user.email ?: ""
                )

                saveUserInRoom(userProperties)
                saveUserInFirestore(userProperties, user)
            }

            emit(LoginResponse.Success)
        } catch (e: NoCredentialException) {
            emit(LoginResponse.Error("Nenhuma conta no dispositivo detectada"))
        } catch (e: GetCredentialCancellationException) {}
        catch (e: Exception) {
            emit(LoginResponse.Error(e.message ?: "Erro desconhecido ao fazer login"))
        }
    }

    override suspend fun facebookLogin(token: String): Flow<LoginResponse> = flow {
        try {
            val credential = FacebookAuthProvider.getCredential(token)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user ?: throw Exception("Usuário não encontrado após login")

            val existingUser = userLocalDataSource.getUserById(user.uid) // Assuming this is a suspend function
            Log.d("FACEBOOK_FIREBASE", firebaseAuth.currentUser?.uid ?: "NO USER")
            if (existingUser == null) {
                val userProperties = User(
                    uid = user.uid,
                    name = user.displayName ?: "",
                    email = user.email ?: ""
                )

                saveUserInRoom(userProperties)
                saveUserInFirestore(userProperties, user)
            }

            emit(LoginResponse.Success)
        } catch (e: NoCredentialException) {
            emit(LoginResponse.Error("Nenhuma conta no dispositivo detectada"))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(LoginResponse.Error("Email já existe. Utilize outra forma de login"))
        } catch (e: Exception) {
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