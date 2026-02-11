package com.example.produtosdelimpeza.store.onboarding.data

import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.onboarding.data.mapper.toEntity
import com.example.produtosdelimpeza.store.onboarding.data.mapper.toStoreDomain
import jakarta.inject.Inject

class StoreLocalDataSource @Inject constructor(
    private val storeDao: StoreDao
) {

    suspend fun saveStoreLocal(store: Store) {
        storeDao.insertStore(store.toEntity())
    }

    suspend fun getStoreById(storeId: String): Store? {
        return storeDao.getStoreById(storeId)?.toStoreDomain()
    }

}