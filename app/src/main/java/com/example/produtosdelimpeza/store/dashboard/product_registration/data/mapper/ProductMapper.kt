package com.example.produtosdelimpeza.store.dashboard.product_registration.data.mapper

import com.example.produtosdelimpeza.store.dashboard.product_registration.data.ProductDto
import com.example.produtosdelimpeza.core.domain.Product
import com.google.type.Money

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        productName = name,
        productDescription = description,
        productPrice = price,
        promotionalPrice = promotionalPrice,
        stockCount = stock,
        productCategory = category,
        inStock = isAvailable
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        id = id,
        name = productName,
        description = productDescription,
        price = productPrice,
        promotionalPrice = promotionalPrice,
        stock = stockCount,
        category = productCategory,
        isAvailable = inStock
    )
}
