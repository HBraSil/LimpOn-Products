package com.example.produtosdelimpeza.model

data class User(
    val uid: String,
    val name: String,
    val lastName: String,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
)