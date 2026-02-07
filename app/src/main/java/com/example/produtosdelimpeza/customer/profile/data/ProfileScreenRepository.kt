package com.example.produtosdelimpeza.customer.profile.data

import android.util.Log
import com.example.produtosdelimpeza.core.data.SigninWithGoogleApi
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileScreenRepository @Inject constructor(private val firebaseAuth: FirebaseAuth, private val signInWithGoogleApi: SigninWithGoogleApi) {
    fun signOut() {
        try {
            firebaseAuth.signOut()
            signInWithGoogleApi.singOut()

            Log.d("ProfileScreenRepository", "Usuário deslogado com sucesso.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ProfileScreenRepository", "Falha ao deslogar")
        }
    }
}