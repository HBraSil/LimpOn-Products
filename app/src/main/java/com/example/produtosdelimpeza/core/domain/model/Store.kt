package com.example.produtosdelimpeza.core.domain.model


data class Store(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val phone: String = "",
    val address: String = "",
)
