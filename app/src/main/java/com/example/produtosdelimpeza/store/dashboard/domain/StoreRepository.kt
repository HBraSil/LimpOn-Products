package com.example.produtosdelimpeza.store.dashboard.domain

import com.example.produtosdelimpeza.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    val currentStore: Flow<Store?>
    suspend fun startObservingStore()
//    suspend fun loadStore()
    suspend fun updateName(id: String, name: String): Result<Boolean>
    suspend fun updateDescription(id: String, description: String): Boolean
    suspend fun updatePhone(id: String, phone: String): Boolean
}