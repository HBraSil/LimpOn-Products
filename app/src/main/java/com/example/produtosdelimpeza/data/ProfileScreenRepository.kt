package com.example.produtosdelimpeza.data

import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileScreenRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    fun signOut() {
        try {
            firebaseAuth.signOut()
            Log.d("ProfileScreenRepository", "Usuário deslogado com sucesso.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ProfileScreenRepository", "Falha ao deslogar")
        }
    }
}