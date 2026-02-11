package com.example.produtosdelimpeza.store.onboarding.data.mapper

import com.example.produtosdelimpeza.core.data.entity.StoreEntity
import com.example.produtosdelimpeza.core.domain.model.Store

fun Store.toEntity() = StoreEntity(
    id, name, ownerId, description, address, phone, email
)

fun StoreEntity.toStoreDomain() = Store(
    id, name, ownerId, description, address, phone, email
)
