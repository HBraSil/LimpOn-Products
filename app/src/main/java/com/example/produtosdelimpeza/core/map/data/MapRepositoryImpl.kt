package com.example.produtosdelimpeza.core.map.data

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.content.ContextCompat
import com.example.produtosdelimpeza.core.map.domain.LocationSettingsResult
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume


@Singleton
class MapRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val savedMapPlacesLocalStorage: AddressLocalStorage,
    @param:ApplicationContext private val context: Context,
) : MapRepository {

    private val placesIdsWithAddresses = mutableStateMapOf<String, Place>()


    override suspend fun fetchUserLocation(): MapResponse {
        return try {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val lastLocation = fusedLocationProviderClient.lastLocation.await()
                if (lastLocation != null) {
                    return MapResponse.Success(
                        lastLocation.latitude,
                        lastLocation.longitude
                    )
                }

                val cts = CancellationTokenSource()
                val currentLocation = fusedLocationProviderClient
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                    .await()


                return if (currentLocation != null) {
                    MapResponse.Success(currentLocation.latitude, currentLocation.longitude)
                } else {
                    fetchLocationViaCallback()
                }
            } else {
                MapResponse.MissingPermission
            }
        } catch (e: Exception) {
            MapResponse.Error("Something went wrong: ${e.message}")
        }
    }

    private suspend fun fetchLocationViaCallback(): MapResponse =
        suspendCancellableCoroutine { cont ->
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000L
            ).setMaxUpdates(1).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    val loc = result.lastLocation
                    if (loc != null) {
                        cont.resume(MapResponse.Success(loc.latitude, loc.longitude))
                    } else {
                        cont.resume(MapResponse.Error("Localização indisponível."))
                    }
                }
            }

            try {
                if (ContextCompat.checkSelfPermission(
                        context, ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        request, callback, Looper.getMainLooper()
                    )
                } else {
                    cont.resume(MapResponse.MissingPermission)
                }
            } catch (e: SecurityException) {
                cont.resume(MapResponse.MissingPermission)
            }

            cont.invokeOnCancellation {
                fusedLocationProviderClient.removeLocationUpdates(callback)
            }
        }

    override suspend fun checkLocationSettings(): LocationSettingsResult {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        val client = LocationServices.getSettingsClient(context)

        return try {
            client.checkLocationSettings(builder).await()
            LocationSettingsResult.Enabled
        } catch (exception: Exception) {
            if (exception is ResolvableApiException) {
                LocationSettingsResult.ResolutionNeeded(exception)
            } else {
                LocationSettingsResult.Error(exception)
            }
        }
    }

    fun createBounds(userLatLng: LatLng, radiusKm: Double): RectangularBounds {
        val lat = userLatLng.latitude
        val lng = userLatLng.longitude

        val latDelta = radiusKm / 111.0

        val lngDelta = radiusKm / (111.0 * kotlin.math.cos(Math.toRadians(lat)))

        val southwest = LatLng(
            lat - latDelta,
            lng - lngDelta
        )

        val northeast = LatLng(
            lat + latDelta,
            lng + lngDelta
        )

        return RectangularBounds.newInstance(southwest, northeast)
    }

    override suspend fun searchPlaces(
        query: String,
        latitude: Double?,
        longitude: Double?,
    ): List<AutocompletePrediction> {
        val builder = FindAutocompletePredictionsRequest.builder()

        val userLatLng = if (latitude != null && longitude != null) {
            LatLng(latitude, longitude)
        } else {
            null
        }
        userLatLng?.let { latLng ->
            Log.d("MapRepositoryImpl", "userLatLng: $latLng")
            try {
                val bounds = createBounds(latLng, 50.0)
                builder.setLocationBias(bounds)
            } catch (e: Exception) {
                Log.d("MapRepositoryImpl", "Erro ao criar bounds: ${e.message}")
            }
        }

        val request = builder
            .setCountries("BR")
            .setQuery(query)
            .build()

        return try {
            val response = placesClient.findAutocompletePredictions(request).await()

            Log.e("MapRepositoryImpl", "userLatLng: $response")
            response.autocompletePredictions
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emptyList()
        }
    }

    override suspend fun getPlaceWithId(placeId: String): Place {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LOCATION,
        )

        return placesIdsWithAddresses.getOrElse(placeId) {
            withContext(Dispatchers.IO) {
                Log.d("MapRepositoryImpl", "Fetching placeId: $placeId")
                placesClient.awaitFetchPlace(
                    placeId = placeId,
                    placeFields = placeFields
                ).place.also { place ->
                    withContext(Dispatchers.Main) {
                        Log.d("MapRepositoryImpl", "Fetched place location: $place")
                        placesIdsWithAddresses[placeId] = place
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun getPrimaryAndSecondaryFromLatLng(
        latitude: Double,
        longitude: Double,
    ): List<AutocompletePrediction>? = suspendCancellableCoroutine { continuation ->
        val geocoder = Geocoder(context, Locale.getDefault())

        geocoder.getFromLocation(
            latitude,
            longitude,
            1,
            object : Geocoder.GeocodeListener {
                @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses.firstOrNull()

                        if (address == null) {
                            continuation.resume(null)
                            return
                        }

                        val query = address.getAddressLine(0)

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val predictions = searchPlaces(
                                    query = query,
                                    latitude = latitude,
                                    longitude = longitude
                                )

                                continuation.resume(predictions)
                            } catch (e: Exception) {
                                continuation.resume(null)
                            }
                        }
                    } else {
                        continuation.resume(null)
                    }
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume(null)
                }
            }
        )
    }


    override suspend fun savePlace(id: String): Result<Boolean> {
        val place = placesClient.awaitFetchPlace(
            id,
            listOf(Place.Field.ID, Place.Field.ADDRESS_COMPONENTS)
        ).place
        Log.d("MapRepositoryImpl", "Address: $place")


        val id = place.id ?: return Result.failure(Exception("ID não encontrado"))
        val streetNumber = place.getComponent("street_number")
        val street = place.getComponent("route")
        val neighborhood = place.getComponent("sublocality_level_1")
        val city = place.getComponent("administrative_area_level_2")
        val state = place.getComponent("administrative_area_level_1")

        val address = com.example.produtosdelimpeza.core.domain.model.Address(
            id = id,
            streetNumber = streetNumber ?: "",
            street = street ?: "",
            neighborhood = neighborhood ?: "",
            city = city ?: "",
            state = state ?: ""
        )
        Log.d("MapRepositoryImpl", "Address: $address")


        val result = savedMapPlacesLocalStorage.savePlace(address)
        return if (result.isSuccess) {
            Result.success(true)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Erro desconhecido"))
        }
    }
    private fun Place.getComponent(type: String): String? {
        return addressComponents
            ?.asList()
            ?.firstOrNull { type in it.types }
            ?.name
    }


    suspend fun getPlaceIdFromLatLng(
        latitude: Double,
        longitude: Double
    ): String? = withContext(Dispatchers.IO) {

        val placeFields = listOf(Place.Field.ID)
        val request = FindCurrentPlaceRequest.newInstance(placeFields) // ainda deprecado, mas sem alternativa
        try {
            val response = placesClient.findCurrentPlace(request).await()
            response.placeLikelihoods.firstOrNull()?.place?.id
        } catch (e: SecurityException) {
            null // ou trate conforme necessário
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun deleteAddress(placeId: String): Result<Boolean> {
        return savedMapPlacesLocalStorage.deleteAddress(placeId)
    }
}