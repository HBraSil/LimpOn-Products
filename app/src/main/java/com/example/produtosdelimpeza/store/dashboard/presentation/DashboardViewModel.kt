package com.example.produtosdelimpeza.store.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    val uiState = storeRepository.currentStore
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            storeRepository.loadStore()
        }
    }
}
