package com.example.produtosdelimpeza.core.data.mapper

import com.example.produtosdelimpeza.core.data.entity.UserEntity
import com.example.produtosdelimpeza.core.domain.model.User

fun UserEntity.toDomain() = User(
    uid = uid,
    name = name,
    email = email,
    phone = phone,
    createdAt = createdAt
)

fun User.toEntity() = UserEntity(
    uid = uid,
    name = name,
    email = email,
    phone = phone,
    createdAt = createdAt
)
