package com.example.produtosdelimpeza.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: Int = 0,
    val storeId: String = "",
    var name: String = "",
    var oldPrice: Double? = 0.0,
    val discountPercent: Int? = null,
    var price: Double = 0.0,
    val promotionalPrice: Double = 0.0,
    var description: String = "",
    var classification: String = "",
    var category: String = "",
    var stockCount: Int = 0,
)