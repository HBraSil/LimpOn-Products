package com.example.produtosdelimpeza.core.data


data class Product(
    val id: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val productDescription: String = "",
    val promotionalPrice: String = "",
    val classification: String = "",
    val productCategory: String = "",
    val stockCount: Int = 0,
    val inStock: Boolean = stockCount > 0
)

