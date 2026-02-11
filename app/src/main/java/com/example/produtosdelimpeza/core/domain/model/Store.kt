package com.example.produtosdelimpeza.core.domain.model

data class Store(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val description: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
)