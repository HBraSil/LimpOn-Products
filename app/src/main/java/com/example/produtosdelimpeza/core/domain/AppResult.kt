package com.example.produtosdelimpeza.core.domain

sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()

    sealed class Error : AppResult<Nothing>() {
        object SessionExpired : Error()
        object Network : Error()
        data class Unknown(val message: String?) : Error()
    }
}
