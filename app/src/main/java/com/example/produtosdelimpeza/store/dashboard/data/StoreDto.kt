package com.example.produtosdelimpeza.store.dashboard.data

// Localização: data/remote/dto/StoreDto.kt (ou data/model/StoreDto.kt)

data class StoreDto(
    val id: String? = null,
    val name: String? = null,
    val ownerId: String? = null,
    val email: String? = null,
    val description: String? = null,
    val category: String? = null,
    val phone: String? = null,
    val address: String? = null
)