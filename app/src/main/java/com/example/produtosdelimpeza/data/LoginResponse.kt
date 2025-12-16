package com.example.produtosdelimpeza.data

sealed class LoginResponse {
    object Success : LoginResponse()

    data class Error(val error: String) : LoginResponse()
}