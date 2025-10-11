package com.example.produtosdelimpeza.data

import com.example.produtosdelimpeza.model.CartProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartProductRepository @Inject constructor(private val cartProductsDAO: CartProductsDAO) {
    suspend fun insertProduct(product: CartProduct) {
        cartProductsDAO.insertProduct(product)
    }
    suspend fun updateProduct(product: CartProduct) {
        cartProductsDAO.updateProduct(product)
    }
    suspend fun deleteProduct(product: CartProduct) {
        cartProductsDAO.deleteProduct(product)
    }
    // ⭐️ Chamando uma função de leitura observável (Flow)
    suspend fun getAllProducts(): List<CartProduct> {
        // O Flow já executa a query em background
        return cartProductsDAO.getAllProducts()
    }

    suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }
}