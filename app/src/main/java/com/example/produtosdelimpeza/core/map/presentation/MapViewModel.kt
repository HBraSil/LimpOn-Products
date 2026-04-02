package com.example.produtosdelimpeza.core.map.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.map.domain.LocationSettingsResult
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
) : ViewModel() {
    private val _mapUiEvent = MutableSharedFlow<MapUiEvent>()
    val mapUiEvent:  SharedFlow<MapUiEvent> = _mapUiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        checkAndRequestLocationSettings()
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            when (val result = mapRepository.fetchUserLocation()) {
                is MapResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            userLocation = LatLng(result.latitude, result.longitude),
                            isLoading = false,
                            error = null
                        )
                    }
                    Log.d("ATENÇAO", "CAIU AQUI`: ${result.latitude} ${result.longitude}")
                }
                else -> {}
            }
        }
    }

    fun onCenterLocationClick() {
        viewModelScope.launch {
            with(_uiState.value) {
                Log.d("ATENÇAO", "ALGO ACONTECEU: ${this.userLocation}")
                if (this.userLocation != null) {
                    _mapUiEvent.emit(
                        MapUiEvent.OnCenterLocationClick(
                            centerLocation = LatLng(this.userLocation.latitude, this.userLocation.longitude)
                        )
                    )
                } else {
                    Log.d("ATENÇAO", "ALGO ACONTECEU")
                }
            }
        }
    }

    fun checkAndRequestLocationSettings() {
        viewModelScope.launch {
            when (val result = mapRepository.checkLocationSettings()) {
                is LocationSettingsResult.ResolutionNeeded -> {
                    _mapUiEvent.emit(MapUiEvent.RequestLocationResolution(result.exception))
                }

                is LocationSettingsResult.Enabled -> {
                    fetchUserLocation()
                }

                else -> {}
            }
        }
    }
}