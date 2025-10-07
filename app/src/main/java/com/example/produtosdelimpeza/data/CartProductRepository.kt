package com.example.produtosdelimpeza.data

import com.example.produtosdelimpeza.model.CartProduct
import kotlinx.coroutines.flow.Flow


class CartProductRepository(private val cartProductsDAO: CartProductsDAO) {
    suspend fun insert(product: CartProduct) {
        cartProductsDAO.insertProduct(product)
    }
    suspend fun update(product: CartProduct) {
        cartProductsDAO.updateProduct(product)
    }
    suspend fun delete(product: CartProduct) {
        cartProductsDAO.deleteProduct(product)
    }

    // ⭐️ Chamando uma função de leitura observável (Flow)
    suspend fun getAllProducts(): List<CartProduct> {
        // O Flow já executa a query em background
        return cartProductsDAO.getAllProducts()
    }

    suspend fun getProductById(productId: Int): CartProduct? {
        return cartProductsDAO.getProductById(productId)
    }

    suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }

    companion object{
        private var instance: CartProductRepository? = null

        fun getInstance(cartProductsDao: CartProductsDAO): CartProductRepository {
            return instance ?: CartProductRepository(cartProductsDao).also {
                instance = it
            }
        }
    }
}