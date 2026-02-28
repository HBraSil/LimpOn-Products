package com.example.produtosdelimpeza.store.dashboard.data

// Localização: data/remote/dto/StoreDto.kt (ou data/model/StoreDto.kt)

data class StoreDto(
    val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val email: String = "",
    val description: String = "",
    val category: String = "",
    val phone: String = "",
    val address: String = ""
)