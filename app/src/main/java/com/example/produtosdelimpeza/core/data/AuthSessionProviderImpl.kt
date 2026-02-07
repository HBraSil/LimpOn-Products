package com.example.produtosdelimpeza.core.data

import android.util.Log
import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject


class AuthSessionProviderImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthSessionProvider {

    override fun getUserId(): String? {
        Log.d("RESULT"," ${firebaseAuth.currentUser?.uid} ")
        return firebaseAuth.currentUser?.uid
    }
}
