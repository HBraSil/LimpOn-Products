package com.example.produtosdelimpeza.store.onboarding.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.produtosdelimpeza.core.data.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("SELECT * FROM store WHERE id = :storeId LIMIT 1")
    suspend fun getStoreById(storeId: String): StoreEntity?

    // Observar uma loja (UI reativa)
    @Query("SELECT * FROM store WHERE id = :storeId LIMIT 1")
    fun observeStoreById(storeId: String): Flow<StoreEntity?>

    // Listar todas as lojas
    @Query("SELECT * FROM store")
    fun observeAllStores(): Flow<List<StoreEntity>>

    // Deletar uma loja
    @Query("DELETE FROM store WHERE id = :storeId")
    suspend fun deleteStore(storeId: String)

    // Limpar tabela (ex: logout)
    @Query("DELETE FROM store")
    suspend fun clearStores()
}
