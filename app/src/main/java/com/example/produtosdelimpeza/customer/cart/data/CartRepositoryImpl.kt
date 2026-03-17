package com.example.produtosdelimpeza.customer.cart.data

import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.core.data.mapper.toDomain
import com.example.produtosdelimpeza.core.data.mapper.toProductEntity
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.customer.cart.domain.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartProductsDAO: CartProductsDAO,
    private val firestore: FirebaseFirestore
) : CartRepository {

    override fun observeCartItems(): Flow<List<CartItem>> {
        return cartProductsDAO.observeCartItems().map { cartItemEntities ->
            cartItemEntities.map { it.toCartItem() }
        }
    }

    override suspend fun getProductById(id: String): Product? {
        val snapshot = firestore.collection("products").document(id).get().await()

        return snapshot.toObject(Product::class.java)
    }



    override suspend fun insert(cartItem: CartItem) {
        val cartItem = cartItem.toCartItemEntity()
        cartProductsDAO.insertProduct(cartItem)
    }

    override suspend fun update(cartItem: CartItem) {
        val cartItem = cartItem.toCartItemEntity()
        cartProductsDAO.updateProduct(cartItem)
    }

    override suspend fun deleteProduct(product: Product) {
        val productEntity = product.toProductEntity()
        cartProductsDAO.deleteProduct(productEntity)
    }

    override suspend fun deleteItem(item: CartItem) {
        cartProductsDAO.removeItem(item.toCartItemEntity())
    }

    override suspend fun fetchAllProducts(): List<Product> {
        val productEntities = cartProductsDAO.getAllProducts()
        return productEntities.map { it.toDomain() }
    }

    override suspend fun clearCart() {
        cartProductsDAO.deleteAllProducts()
    }
}
