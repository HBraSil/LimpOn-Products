package com.example.produtosdelimpeza.core.auth.data

sealed class LoginResponse {
    object Success : LoginResponse()

    data class Error(val error: String) : LoginResponse()
}