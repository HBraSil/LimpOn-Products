package com.example.produtosdelimpeza.customer.cart.data

import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.core.data.mapper.toDomain
import com.example.produtosdelimpeza.core.data.mapper.toProductEntity
import com.example.produtosdelimpeza.core.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartProductsDAO: CartProductsDAO
) : CartRepository {
    override suspend fun insertProduct(product: Product) {

        val productEntity = product.toProductEntity()
        cartProductsDAO.insertProduct(productEntity)
    }

    override suspend fun updateProduct(product: Product) {
        val productEntity = product.toProductEntity()
        cartProductsDAO.updateProduct(productEntity)
    }

    override suspend fun deleteProduct(product: Product) {
        val productEntity = product.toProductEntity()
        cartProductsDAO.deleteProduct(productEntity)
    }

    override suspend fun getAllProducts(): List<Product> {
        val productEntities = cartProductsDAO.getAllProducts()
        return productEntities.map { it.toDomain() }
    }

    override suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }
}