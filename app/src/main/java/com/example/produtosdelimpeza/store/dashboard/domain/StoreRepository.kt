package com.example.produtosdelimpeza.store.dashboard.domain

import com.example.produtosdelimpeza.core.domain.model.Store
import kotlinx.coroutines.flow.StateFlow

interface StoreRepository {

    val currentStore: StateFlow<Store?>
    suspend fun loadStore()
}