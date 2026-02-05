package com.example.produtosdelimpeza.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val createdAt: Long
)
