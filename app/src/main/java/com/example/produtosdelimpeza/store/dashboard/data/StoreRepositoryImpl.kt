package com.example.produtosdelimpeza.store.dashboard.data

import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import com.example.produtosdelimpeza.core.data.StoreRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class StoreRepositoryImpl @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
    private val userSession: LastUserModeLocalStorage, // talvez essa propriedade deva estar no repositoy e n aqui
): StoreRepository {

    override val currentStore = MutableStateFlow<Store?>(null)

    override suspend fun loadStore() {
        return try {
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
        } catch (e: Exception) {
            currentStore.value = null
        }
    }

    override suspend fun updateName(id: String, name: String): Boolean {
         val result = storeRemoteDataSource.updateName(
            storeId = id,
            newName = name
        )

        return result.isSuccess
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