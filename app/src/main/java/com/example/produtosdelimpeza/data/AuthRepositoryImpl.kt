package com.example.produtosdelimpeza.data

import com.example.produtosdelimpeza.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
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
}
