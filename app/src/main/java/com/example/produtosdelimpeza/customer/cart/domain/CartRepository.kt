package com.example.produtosdelimpeza.customer.cart.domain

import com.example.produtosdelimpeza.core.domain.Product

interface CartRepository {
    suspend fun insertProduct(product: Product)

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun getAllProducts(): List<Product>

    suspend fun clearCart()
}