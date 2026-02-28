package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.domain.model.Store


fun StoreDto.toDomain(): Store {
    return Store(
        id = id,
        name = name,
        ownerId = ownerId,
        email = email,
        description = description,
        category = category,
        phone = phone,
        address = address
    )
}


fun Store.toDto(): StoreDto {
    return StoreDto(
        id = id,
        name = name,
        ownerId = ownerId,
        email = email,
        description = description,
        category = category,
        phone = phone,
        address = address
    )
}