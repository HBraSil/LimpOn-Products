package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import com.example.produtosdelimpeza.core.data.StoreRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
    private val userSession: LastUserModeLocalStorage
) : StoreRepository {

    private val _currentStore = MutableStateFlow<Store?>(null)

    override val currentStore = _currentStore.asStateFlow()


    override suspend fun startObservingStore() {
        val storeid = userSession.storeId.first()


        storeid?.let {
            storeRemoteDataSource.getStoreStream(it).collect { storeDto ->
                _currentStore.value = storeDto?.toDomain()
            }
        }
    }

/*    override suspend fun loadStore() {
        userSession.storeId.collect { storeId ->
            storeId?.let { storeId ->
                val result = storeRemoteDataSource.fetchStoreRemote(storeId)

                if (result.isSuccess) {
                    val storeDto = result.getOrNull() ?: error("Erro ao obter dados da loja")
                    currentStore.value = storeDto.toDomain()
                } else {
                    currentStore.value = null
                }
            }
        }
    }*/

    override suspend fun updateName(id: String, name: String): Result<Boolean> {
        val result = storeRemoteDataSource.updateName(
            storeId = id,
            newName = name
        )

        return result
    }

    override suspend fun updateDescription(
        id: String,
        description: String,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updatePhone(id: String, phone: String): Boolean {
        TODO("Not yet implemented")
    }
}