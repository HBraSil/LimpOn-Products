package com.example.produtosdelimpeza.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartProduct(
    @PrimaryKey
    val id: Int = 0,
    var name: String = "",
    var price: Double = 0.0,
    var badges: List<String> = emptyList(),
    var quantity: Int = 0
)