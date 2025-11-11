package com.example.produtosdelimpeza.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.produtosdelimpeza.model.Product

@Dao
interface CartProductsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): Product?

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<Product>
}