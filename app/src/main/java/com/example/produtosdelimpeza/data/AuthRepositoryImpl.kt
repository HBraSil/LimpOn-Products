package com.example.produtosdelimpeza.data

import android.util.Log
import com.example.produtosdelimpeza.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(name: String, lastName: String, email: String, password: String): String =
        suspendCancellableCoroutine { cont ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    println("Entrou no firebaseAuth.createUserWithEmailAndPassword no AuthRepositoryImpl")
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        val uid = firebaseUser?.uid
                        if (uid == null) {
                            println("Entrou no if do uid == null no AuthRepositoryImpl")
                            cont.resumeWithException(RuntimeException("Erro ao obter uid do usuário"))
                            return@addOnCompleteListener
                        }

                        // Criar documento no Firestore
                        val user = User(uid = uid, name = name, lastName = lastName, email = email)
                        firestore.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnCompleteListener { setTask ->
                                if (setTask.isSuccessful) {
                                    // Enviar email de verificação (não bloqueante)
                                    println("Entrou no if do setTask.isSuccessful no AuthRepositoryImpl")

                                    firebaseUser.sendEmailVerification()
                                    cont.resume(uid)
                                } else {
                                    cont.resumeWithException(setTask.exception ?: RuntimeException("Falha ao criar usuário no Firestore"))
                                }
                            }
                    }
                }
                .addOnFailureListener {
                    try {
                        throw it
                    } catch (e: Exception) {
                        println("mensagem erro: $e")
                    }
                }
            // cancel handler
            cont.invokeOnCancellation { /* nada especial */ }
        }

    override suspend fun signIn(email: String, password: String): LoginResponse {
        return try {
            val signInResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (signInResult != null) {
                LoginResponse.Success
            } else {
                LoginResponse.Error("Login não foi bem sucedido")
            }

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResponse.Error("Erro ao fazer login: senha ou email incorretos")
        }
    }
}
