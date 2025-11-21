package com.example.produtosdelimpeza.data

import com.google.firebase.auth.FirebaseUser

sealed class LoginResponse {
    object Success : LoginResponse()

    data class Error(val error: String) : LoginResponse()
}