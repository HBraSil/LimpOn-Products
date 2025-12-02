package com.example.produtosdelimpeza.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileScreenRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    fun signOut() {
        firebaseAuth.signOut()
    }
}