package com.example.produtosdelimpeza.customer.catalog.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.customer.catalog.domain.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CatalogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storeRepository: CatalogRepository
) : ViewModel() {
    private val _store = MutableStateFlow<Store?>(null)
    val store = _store.asStateFlow()


    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("sellerId")?.let { sellerId ->
                Log.d("Alo", "Testando Saved: $sellerId")
                val result = storeRepository.fetchStore(sellerId)

                if (result.isSuccess) {
                    _store.value = result.getOrNull()
                }
            }
        }
    }
}