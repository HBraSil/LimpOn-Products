package com.example.produtosdelimpeza.customer.cart.domain

import com.example.produtosdelimpeza.core.domain.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun observeCartItems(): Flow<List<CartItem>>
    //fun getCurrentCart(): List<CartItem>

    suspend fun getProductById(id: String): Product?

    suspend fun insert(cartItem: CartItem)

    suspend fun update(cartItem: CartItem)

    suspend fun deleteProduct(product: Product)
    suspend fun deleteItem(item: CartItem)

    suspend fun fetchAllProducts(): List<Product>

    suspend fun clearCart()
}