package com.example.produtosdelimpeza.customer.catalog.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.customer.catalog.domain.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CatalogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storeCatalogRepository: CatalogRepository
) : ViewModel() {
    private val _store = MutableStateFlow(Store())
    val store = _store.asStateFlow()

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList = _productList.asStateFlow()


    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("storeId")?.let { storeId ->
                updateStore(storeId)
                updateProducts(storeId)
            }
        }
    }

    fun updateStore(storeId: String) {
        viewModelScope.launch {
            val result = storeCatalogRepository.fetchStore(storeId)

            if (result.isSuccess) {
                _store.update {
                    it.copy(
                        id = storeId,
                        ownerId = result.getOrNull()?.ownerId ?: "",
                        name = result.getOrNull()?.name ?: "",
                        address = result.getOrNull()?.address ?: "",
                        phone = result.getOrNull()?.phone ?: "",
                        email = result.getOrNull()?.email ?: "",
                        description = result.getOrNull()?.description ?: "",
                        category = result.getOrNull()?.category ?: "",
                        revenue = result.getOrNull()?.revenue ?: "",
                        storeOperationTime = result.getOrNull()?.storeOperationTime ?: emptyMap()
                    )
                }
            }
        }
    }

    fun updateProducts(storeId: String) {
        viewModelScope.launch {
            val result = storeCatalogRepository.fetchProduct(storeId)

            Log.d("CatalogViewModel", "Fetched products: ${result.isSuccess}")
            if (result.isSuccess) {
                _productList.update {
                    result.getOrNull() ?: emptyList()
                }
            }
        }
    }
}