package com.example.produtosdelimpeza.store.dashboard.product_registration.domain

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val productName: String = "",
    val productPrice: String = "",
    val promotionalPrice: String = "",
    val productDescription: String = "",
    val classification: String = "",
    val productCategory: String = "",
    val stockCount: Int = 0,
    val inStock: Boolean = stockCount > 0
)