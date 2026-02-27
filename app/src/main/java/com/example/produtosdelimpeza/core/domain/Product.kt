package com.example.produtosdelimpeza.core.domain


data class Product(
    val id: String = "",
    val storeId: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val promotionalPrice: String = "",
    val productDescription: String = "",
    val classification: String = "",
    val productCategory: String = "",
    val stockCount: Int = 0,
    val inStock: Boolean = stockCount > 0
)