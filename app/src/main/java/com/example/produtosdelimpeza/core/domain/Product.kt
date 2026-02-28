package com.example.produtosdelimpeza.core.domain


data class Product(
    val id: String = "",
    val storeId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val promotionalPrice: Double = 0.0,
    val description: String = "",
    val classification: String = "",
    val category: String = "",
    val stockCount: Int = 0,
)