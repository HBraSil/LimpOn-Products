package com.example.produtosdelimpeza.store.dashboard.product_registration.data

data class ProductDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val promotionalPrice: String = "",
    val stock: Int = 0,
    val category: String = "",
    val isAvailable: Boolean = true
)

