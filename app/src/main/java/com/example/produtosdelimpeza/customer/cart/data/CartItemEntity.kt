package com.example.produtosdelimpeza.customer.cart.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "cart_item")
class CartItemEntity(
    @PrimaryKey
    val productId: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val promotionalPrice: Double = 0.0,
)
