package com.example.produtosdelimpeza.store.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val userSession: LastUserModeLocalStorage
) : ViewModel() {

    private val _dashboardUiState = MutableStateFlow(true)
    val dashboardUiState = _dashboardUiState.asStateFlow()



    private val _dashboardData = MutableStateFlow<Store?>(null)
    val dashboardData: StateFlow<Store?> = _dashboardData.asStateFlow()


    init {
        viewModelScope.launch {
            getDashboardData()
        }
    }

    fun getDashboardData() {
        viewModelScope.launch {
            userSession.storeId.collect { storeId ->
                storeId?.let { storeId ->
                    _dashboardData.value = dashboardRepository.getDashboardData(storeId)
                    _dashboardUiState.value = false
                }
            }
        }
    }
}