package com.example.produtosdelimpeza.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.produtosdelimpeza.model.CartProduct

@Dao
interface CartProductsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: CartProduct)

    @Query("SELECT * FROM cartproduct WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): CartProduct?

    @Update
    suspend fun updateProduct(product: CartProduct)

    @Delete
    suspend fun deleteProduct(product: CartProduct)

    @Query("DELETE FROM cartproduct")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM cartproduct")
    suspend fun getAllProducts(): List<CartProduct>
}