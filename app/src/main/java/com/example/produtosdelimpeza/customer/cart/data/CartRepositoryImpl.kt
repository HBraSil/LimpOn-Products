package com.example.produtosdelimpeza.customer.cart.data

import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.model.ProductEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartProductsDAO: CartProductsDAO
) : CartRepository {
    override suspend fun insertProduct(productEntity: ProductEntity) {
        cartProductsDAO.insertProduct(productEntity)
    }

    override suspend fun updateProduct(productEntity: ProductEntity) {
        cartProductsDAO.updateProduct(productEntity)
    }

    override suspend fun deleteProduct(productEntity: ProductEntity) {
        cartProductsDAO.deleteProduct(productEntity)
    }

    override suspend fun getAllProducts(): List<ProductEntity> {
        return cartProductsDAO.getAllProducts()
    }

    override suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }
}