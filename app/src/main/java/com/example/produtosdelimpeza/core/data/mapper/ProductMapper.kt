package com.example.produtosdelimpeza.core.data.mapper

import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import com.example.produtosdelimpeza.core.domain.Product

fun Product.toProductEntity(): ProductEntity =
    ProductEntity(
        name = name,
        price = price,
        promotionalPrice = promotionalPrice,
        description = description,
    )

fun ProductEntity.toDomain(): Product =
    Product(
        name = name,
        price = price,
        promotionalPrice = promotionalPrice,
        description = description,
    )

