package com.example.produtosdelimpeza.store.dashboard.domain

import com.example.produtosdelimpeza.core.domain.model.Store
import kotlinx.coroutines.flow.StateFlow

interface StoreRepository {
    val currentStore: StateFlow<Store?>
    suspend fun loadStore()
    suspend fun updateName(id: String, name: String): Boolean
    suspend fun updateDescription(id: String, description: String): Boolean
    suspend fun updatePhone(id: String, phone: String): Boolean
}