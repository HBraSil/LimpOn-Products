package com.example.produtosdelimpeza.customer.cart.domain

import com.example.produtosdelimpeza.model.ProductEntity

interface CartRepository {
    suspend fun insertProduct(productEntity: ProductEntity)

    suspend fun updateProduct(productEntity: ProductEntity)

    suspend fun deleteProduct(productEntity: ProductEntity)

    suspend fun getAllProducts(): List<ProductEntity>

    suspend fun clearCart()
}