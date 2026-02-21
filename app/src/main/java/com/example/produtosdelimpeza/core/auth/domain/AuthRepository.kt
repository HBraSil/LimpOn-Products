package com.example.produtosdelimpeza.core.auth.domain

import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String): LoginResponse

    suspend fun registerUser(name: String, lastName: String, email: String, password: String): LoginResponse

    suspend fun signInWithGoogle(): Flow<LoginResponse>
}