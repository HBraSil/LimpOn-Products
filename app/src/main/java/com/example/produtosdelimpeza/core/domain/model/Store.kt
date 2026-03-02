package com.example.produtosdelimpeza.core.domain.model

import java.util.UUID


data class Store(
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String = "",
    val name: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val revenue: String = "",
    val address: String = "",
    val storeOperationTime: Map<String, BusinessHours> = emptyMap(),
    val phone: String = "",
)
