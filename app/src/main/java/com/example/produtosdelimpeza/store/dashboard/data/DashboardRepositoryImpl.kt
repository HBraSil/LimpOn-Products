package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.DashboardRepository
import com.example.produtosdelimpeza.core.data.StoreRemoteDataSource
import javax.inject.Inject


class DashboardRepositoryImpl @Inject constructor(
    private val dashboardRemoteDataSource: StoreRemoteDataSource
): DashboardRepository {
    override suspend fun getDashboardData(storeId: String): Store? {
        val result = dashboardRemoteDataSource.fetchStoreRemote(storeId)
        return if (result.isSuccess) {
            val storeDto = result.getOrNull() ?: error("Erro ao obter dados da loja")
            storeDto.toDomain()
        } else {
            null
        }
    }
}