package com.example.produtosdelimpeza.customer.cart.domain

data class CartItem(
    val productId: String = "",
    val name: String = "",
    val description: String = "",
    val totalPrice: Double = 0.0,
    val totalPromotionalPrice: Double = 0.0,
    val quantity: Int = 0
)
