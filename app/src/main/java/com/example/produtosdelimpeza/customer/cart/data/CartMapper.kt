package com.example.produtosdelimpeza.customer.cart.data

import com.example.produtosdelimpeza.customer.cart.domain.CartItem

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        productId = productId,
        name = name,
        description = description,
        quantity = quantity,
        totalPrice = price,
        totalPromotionalPrice = promotionalPrice,
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity {
    return CartItemEntity(
        productId = productId,
        name = name,
        description = description,
        quantity = quantity,
        price = totalPrice,
        promotionalPrice = totalPromotionalPrice,
    )
}