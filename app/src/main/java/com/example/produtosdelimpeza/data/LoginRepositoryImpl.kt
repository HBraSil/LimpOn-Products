package com.example.produtosdelimpeza.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): LoginRepository {
    override fun login(email: String, password: String): Boolean {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        return@addOnCompleteListener
                    }
                }

                return@addOnCompleteListener
            } .addOnFailureListener {
                return@addOnFailureListener
            }

        return false
    }

}