package com.example.produtosdelimpeza.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartProduct(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    var price: Double,
    var quantity: Int
)