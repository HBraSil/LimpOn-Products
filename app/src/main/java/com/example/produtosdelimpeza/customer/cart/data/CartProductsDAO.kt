package com.example.produtosdelimpeza.customer.cart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.produtosdelimpeza.core.data.entity.ProductEntity

@Dao
interface CartProductsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): ProductEntity?

    @Update
    suspend fun updateProduct(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>
}