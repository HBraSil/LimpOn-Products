package com.example.produtosdelimpeza.core.map.presentation

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.map.domain.LocationSettingsResult
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.mapLatest


private val predictionStyleSpan = StyleSpan(Typeface.BOLD)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
) : ViewModel() {
    private val _mapUiEvent = MutableSharedFlow<MapUiEvent>()
    val mapUiEvent:  SharedFlow<MapUiEvent> = _mapUiEvent.asSharedFlow()

    private val _mapState = MutableStateFlow(MapUiState())
    val mapState: StateFlow<MapUiState> = _mapState.asStateFlow()


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val predictions =
        _searchText.debounce(300)
            .mapLatest { query ->
                val location = mapState.value.userLocation

                if(query.isBlank()) emptyList()
                else mapRepository.searchPlaces(query, location?.latitude, location?.longitude)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val searchState =
        predictions.map { predictionsLits ->
            predictionsLits.map {
                PlaceSuggestion(
                    it.placeId,
                    it.getPrimaryText(predictionStyleSpan),
                    it.getSecondaryText(predictionStyleSpan)
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    init {
        checkAndRequestLocationSettings()
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            when (val result = mapRepository.fetchUserLocation()) {
                is MapResponse.Success -> {
                    _mapState.update {
                        it.copy(
                            userLocation = LatLng(result.latitude, result.longitude),
                            isLoading = false,
                            error = null
                        )
                    }
                    Log.d("ATENÇAO", "CAIU AQUI`: ${result.latitude} ${result.longitude}")
                }
                is MapResponse.Error -> {
                    Log.d("ATENÇAO", "foi pro else: ${result.errorMessage}")
                }
                is MapResponse.MissingPermission -> {}
            }
        }
    }

    fun onCenterLocationClick() {
        viewModelScope.launch {
            with(_mapState.value) {
                if (this.userLocation != null) {
                    _mapUiEvent.emit(
                        MapUiEvent.OnCenterLocationClick(
                            centerLocation = LatLng(this.userLocation.latitude, this.userLocation.longitude)
                        )
                    )
                }
            }
        }
    }

    fun onCameraMove(latLng: LatLng) {
        viewModelScope.launch {
            val userLocation = mapState.value.userLocation ?: return@launch
            if (latLng.latitude != userLocation.latitude && latLng.longitude != userLocation.longitude) {
                val place = mapRepository.getPrimaryAndSecondaryFromLatLng(latLng.latitude, latLng.longitude)
                if (place != null) {
                    Log.d("onCameraMove", "RESULTADO: ${place.primaryText} - ${place.secondaryText}")
                    _mapState.update {
                        it.copy(
                            place = PlaceSuggestion(
                                place.placeId,
                                SpannableString(place.primaryText),
                                SpannableString(place.secondaryText)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onPlaceSelected(placeSelected: PlaceSuggestion) {
        viewModelScope.launch {
            val place = mapRepository.getPlaceWithId(placeSelected.placeId)
            place.location?.let { loc ->
                _mapState.update {
                    it.copy(
                        userLocation = LatLng(loc.latitude, loc.longitude),
                        place = placeSelected
                    )
                }
                _mapUiEvent.emit(
                    MapUiEvent.OnCenterLocationClick(
                        centerLocation = LatLng(loc.latitude, loc.longitude)
                    )
                )
                Log.d("TESTE", "RESULTADO: ${loc.latitude} ${loc.longitude}")
            } ?: run {
                Log.d("ATENÇAO", "ALGO ACONTECEU")
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _searchText.value = newQuery
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

    fun savePlace(place: PlaceSuggestion) {
        viewModelScope.launch {
            val result = mapRepository.savePlace(place)
            if(result.isSuccess) {
                _mapState.update {
                    it.copy(
                        placeSavedSuccessfully = true
                    )
                }
            } else {
                _mapState.update {
                    it.copy(
                        error = "Erro ao salvar local: ${result.exceptionOrNull()}"
                    )
                }
            }
        }
    }
}

