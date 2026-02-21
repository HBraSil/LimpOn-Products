package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.domain.model.Store


fun StoreDto.toDomain(): Store {
    return Store(
        id = id.orEmpty(),
        name = name.orEmpty(),
        ownerId = ownerId.orEmpty(),
        email = email.orEmpty(),
        description = description.orEmpty(),
        category = category.orEmpty(),
        phone = phone.orEmpty(),
        address = address.orEmpty()
    )
}


fun Store.toDto(ownerId: String): StoreDto {
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