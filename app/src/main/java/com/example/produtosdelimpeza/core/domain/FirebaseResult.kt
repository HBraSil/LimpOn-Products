package com.example.produtosdelimpeza.core.domain

sealed class FirebaseResult {
    data class Success(var data: Boolean) : FirebaseResult()

    sealed class Error : FirebaseResult() {
        data class Network(val message: String) : Error()
        data class Unknown(val message: String) : Error()
    }
}
