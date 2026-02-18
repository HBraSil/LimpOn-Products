package com.example.produtosdelimpeza.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.produtosdelimpeza.core.domain.model.BusinessHours

@Entity(tableName = "store")
data class StoreEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ownerId: String,
    val description: String,
    val category: String,
    val revenue: String,
    val storeOperationTime: Map<String, BusinessHours>,
    val address: String,
    val phone: String,
    val email: String,
)
