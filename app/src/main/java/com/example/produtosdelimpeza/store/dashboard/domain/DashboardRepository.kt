package com.example.produtosdelimpeza.store.dashboard.domain

import com.example.produtosdelimpeza.core.domain.model.Store

interface DashboardRepository {
    suspend fun getDashboardData(storeId: String): Store?
}