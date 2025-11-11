package com.example.produtosdelimpeza.data

import com.example.produtosdelimpeza.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartProductRepository @Inject constructor(private val cartProductsDAO: CartProductsDAO) {
    suspend fun insertProduct(product: Product) {
        cartProductsDAO.insertProduct(product)
    }
    suspend fun updateProduct(product: Product) {
        cartProductsDAO.updateProduct(product)
    }
    suspend fun deleteProduct(product: Product) {
        cartProductsDAO.deleteProduct(product)
    }
    // ⭐️ Chamando uma função de leitura observável (Flow)
    suspend fun getAllProducts(): List<Product> {
        // O Flow já executa a query em background
        return cartProductsDAO.getAllProducts()
    }

    suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }
}