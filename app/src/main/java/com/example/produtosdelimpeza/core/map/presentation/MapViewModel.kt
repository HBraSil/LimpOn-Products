package com.example.produtosdelimpeza.core.map.presentation

import android.graphics.Typeface
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

    val predictions = _searchText
        .debounce(300)
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
                Log.d("onPlaceSelected", "ID: ${it.placeId}")
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
                if (userLocation != null) {
                    _mapUiEvent.emit(
                        MapUiEvent.OnCenterLocationClick(
                            centerLocation = LatLng(userLocation.latitude, userLocation.longitude)
                        )
                    )
                }
            }
        }
    }

    fun onCameraMove(latLng: LatLng) {
        viewModelScope.launch {
            val userLocation = mapState.value.userLocation ?: return@launch

            Log.d("onCameraMove", "camera moving: $latLng")
            if (
                latLng.latitude != userLocation.latitude &&
                latLng.longitude != userLocation.longitude
            ) {
                val predictions = mapRepository.getPrimaryAndSecondaryFromLatLng(
                    latLng.latitude,
                    latLng.longitude
                )

                val prediction = predictions?.firstOrNull()

                if (prediction != null) {
                    _mapState.update {
                        it.copy(
                            place = PlaceSuggestion(
                                placeId = prediction.placeId,
                                primaryText = prediction.getPrimaryText(null),
                                secondaryText = prediction.getSecondaryText(null)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onPlaceSelected(placeSelected: PlaceSuggestion) {
        viewModelScope.launch {
            Log.d("onPlaceSelected", "onPLaceSelected: ${placeSelected.placeId}")
            val place = mapRepository.getPlaceWithId(placeSelected.placeId)
            place.location?.let { loc ->
                _mapState.update {
                    it.copy(
                        place = placeSelected,
                        userLocation = LatLng(loc.latitude, loc.longitude)
                    )
                }
                _mapUiEvent.emit(
                    MapUiEvent.OnCenterLocationClick(
                        centerLocation = LatLng(loc.latitude, loc.longitude)
                    )
                )
                Log.d("onPlaceSelected", "RESULTADO: ${loc.latitude} ${loc.longitude}")
                Log.d("onPlaceSelected", "id: ${mapState.value.place?.placeId} ")
            } ?: run {
                Log.d("onPlaceSelected", "ALGO ACONTECEU")
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

    fun savePlace(id: String) {
        viewModelScope.launch {
            Log.d("TESTE", "IDMap: $id")
            val result = mapRepository.savePlace(id)
            if(result.isSuccess) {
                _mapState.update {
                    it.copy(placeSavedSuccessfully = true)
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

    fun closeCard() {
        _mapState.update {
            it.copy(place = null)
        }
    }
}

