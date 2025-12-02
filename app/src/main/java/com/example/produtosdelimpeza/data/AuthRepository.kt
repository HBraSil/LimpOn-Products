package com.example.produtosdelimpeza.data

import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    /**
     * Registra um usuário no Firebase Auth e cria o documento em Firestore.
     * Retorna o uid do usuário registrado.
     * Lança Exception em caso de erro.
     */
    suspend fun registerUser(name: String, lastName: String, email: String, password: String)

    suspend fun signIn(email: String, password: String): LoginResponse
}
