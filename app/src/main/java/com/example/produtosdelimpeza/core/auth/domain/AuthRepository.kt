package com.example.produtosdelimpeza.core.auth.domain

import android.content.Context
import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import com.google.firebase.auth.FirebaseUser
import com.google.rpc.context.AttributeContext


interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun registerUser(name: String, lastName: String, email: String, password: String)

    suspend fun signInWithGoogle(): LoginResponse
}