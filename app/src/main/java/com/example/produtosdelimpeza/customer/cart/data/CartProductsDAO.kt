package com.example.produtosdelimpeza.customer.cart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductsDAO {
    @Query("SELECT * FROM cart_item")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(cartItem: CartItemEntity)

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: String): ProductEntity?

    @Update
    suspend fun updateProduct(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()

    @Delete
    suspend fun removeItem(item: CartItemEntity)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>
}