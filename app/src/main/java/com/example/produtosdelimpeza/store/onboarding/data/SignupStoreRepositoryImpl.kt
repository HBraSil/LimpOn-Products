package com.example.produtosdelimpeza.store.onboarding.data

import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.data.toDto
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import jakarta.inject.Inject


class SignupStoreRepositoryImpl @Inject constructor(
    private val remoteSignUpStore: StoreRemoteDataSource,
    //private val local: StoreLocalDataSource,
) : SignupStoreRepository {
    override suspend fun createStore(store: Store): Result<Boolean> {
        //local.saveStoreLocal(store)

        val storeDto = store.toDto()
        val remoteResult = remoteSignUpStore.saveStoreRemote(storeDto)

        return if (remoteResult.isSuccess) Result.success(true) else Result.success(false)
    }

}