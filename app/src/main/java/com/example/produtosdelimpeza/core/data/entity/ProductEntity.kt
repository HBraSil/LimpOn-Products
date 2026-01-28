package com.example.produtosdelimpeza.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val id: Int = 0,
    var name: String = "",
    var oldPrice: Double? = 0.0,
    val discountPercent: Int? = null,
    var price: Double = 0.0,
    var badges: List<String> = emptyList(),
    var quantity: Int = 0,
    val imageUrl: String? = null,
    val isTopSeller: Boolean = false
)