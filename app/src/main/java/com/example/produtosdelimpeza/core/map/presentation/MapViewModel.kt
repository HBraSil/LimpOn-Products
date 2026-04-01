package com.example.produtosdelimpeza.core.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()
    private val _events = MutableSharedFlow<Boolean>()
    val events = _events.asSharedFlow()

    val uiState = MutableStateFlow(MapUiState())


    fun fetchUserLocation() {
        viewModelScope.launch {
            when(val result = mapRepository.fetchUserLocation()) {
                is MapResponse.LatiLongi -> {
                    _userLocation.value = LatLng(result.latitude, result.longitude)
                    _events.emit(true)
                }
                is MapResponse.MissingPermission -> {

                }
                else -> {

                }

            }
        }
    }
}