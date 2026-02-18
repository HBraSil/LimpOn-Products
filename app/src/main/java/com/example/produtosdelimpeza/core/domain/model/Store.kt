package com.example.produtosdelimpeza.core.domain.model


data class Store(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val revenue: String = "",
    val address: String = "",
    val storeOperationTime: Map<String, BusinessHours> = emptyMap(),
    val phone: String = "",
)
