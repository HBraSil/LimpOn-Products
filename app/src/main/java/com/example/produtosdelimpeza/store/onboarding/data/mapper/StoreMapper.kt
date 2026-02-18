package com.example.produtosdelimpeza.store.onboarding.data.mapper

import com.example.produtosdelimpeza.core.data.entity.StoreEntity
import com.example.produtosdelimpeza.core.domain.model.Store

fun Store.toEntity() = StoreEntity(
    id, name, ownerId, description, category, revenue, storeOperationTime, address, phone, email
)

fun StoreEntity.toStoreDomain() = Store(
    id, name, ownerId, email, description, category, revenue, address, storeOperationTime, phone
)
