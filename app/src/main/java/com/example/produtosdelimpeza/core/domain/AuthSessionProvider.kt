package com.example.produtosdelimpeza.core.domain

interface AuthSessionProvider {
    fun getUserId(): String?
}