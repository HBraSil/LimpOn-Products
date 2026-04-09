package com.example.produtosdelimpeza.core.map.data

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.content.ContextCompat
import com.example.produtosdelimpeza.core.map.domain.LocationSettingsResult
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Singleton
class MapRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @param:ApplicationContext private val context: Context
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

                 val currentLocation = fusedLocationProviderClient
                     .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                     .await()

                 if (currentLocation != null) {
                     MapResponse.Success(currentLocation.latitude, currentLocation.longitude)
                 } else {
                     MapResponse.Error("Localização indisponível no momento.")
                 }
             } else {
                 MapResponse.MissingPermission
             }
         } catch (e: Exception) {
             MapResponse.Error("Something went wrong: ${e.message}")
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

    override suspend fun searchPlaces(query: String, userLatLng: LatLng?): List<AutocompletePrediction> {
        val builder = FindAutocompletePredictionsRequest.builder()
        userLatLng?.let { latLng ->
            try {
                val bounds = createBounds(latLng, 1000.0)
                builder.setLocationBias(bounds)
            } catch (e: Exception) {
                Log.e("MapRepositoryImpl", "Erro ao criar bounds: ${e.message}")
            }
        }

        val request = builder
            .setCountries("BR")
            .setQuery(query)
            .build()

        return try {
            val response = placesClient.findAutocompletePredictions(request).await()
            response.autocompletePredictions
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emptyList()
        }
    }

    override suspend fun getPlaceLatLng(placeId: String): Place {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.LOCATION,
        )

        return withContext(Dispatchers.Main) {
            placesIdsWithAddresses.getOrElse(placeId) {
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
    }
}