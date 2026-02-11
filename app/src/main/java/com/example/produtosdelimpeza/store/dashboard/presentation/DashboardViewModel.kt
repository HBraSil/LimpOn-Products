package com.example.produtosdelimpeza.store.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _dashboardData = MutableStateFlow<Store?>(null)
    val dashboardData: StateFlow<Store?> = _dashboardData.asStateFlow()


    init {
        viewModelScope.launch {
            val store = dashboardRepository.getDashboardData()
            _dashboardData.value = store
        }
    }

    fun getDashboardData() {
        viewModelScope.launch {
            dashboardRepository.getDashboardData()
        }
    }
}