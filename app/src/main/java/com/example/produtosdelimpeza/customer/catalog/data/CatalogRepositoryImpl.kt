package com.example.produtosdelimpeza.customer.catalog.data

import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.customer.catalog.domain.CatalogRepository
import com.example.produtosdelimpeza.store.dashboard.data.toDomain
import com.example.produtosdelimpeza.store.onboarding.data.StoreRemoteDataSource
import jakarta.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val remoteDataSource: StoreRemoteDataSource,
) : CatalogRepository {

    override suspend fun fetchStore(storeId: String): Result<Store> {
        return remoteDataSource
            .getStoreRemote(storeId)
            .map { storeDto ->
                storeDto.toDomain()
            }
    }

    override suspend fun fetchProduct(sellerId: String): List<Product> {
        TODO("Not yet implemented")
    }
}