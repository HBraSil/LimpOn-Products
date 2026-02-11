package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.DashboardRepository
import com.example.produtosdelimpeza.store.onboarding.data.StoreRemoteDataSource
import javax.inject.Inject


class DashboardRepositoryImpl @Inject constructor(
    private val dashboardRemoteDataSource: StoreRemoteDataSource
): DashboardRepository {
    override suspend fun getDashboardData(): Store? {
        val result = dashboardRemoteDataSource.getStoreRemote()
        return if (result.isSuccess) {
            val storeDto = result.getOrNull() ?: error("Erro ao obter dados da loja")
            storeDto.toDomain()
        } else {
            null
        }
    }
}