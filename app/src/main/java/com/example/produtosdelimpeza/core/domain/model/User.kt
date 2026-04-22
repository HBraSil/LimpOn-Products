package com.example.produtosdelimpeza.core.domain.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: Address? = null,
    val createdAt: Long = System.currentTimeMillis()
)