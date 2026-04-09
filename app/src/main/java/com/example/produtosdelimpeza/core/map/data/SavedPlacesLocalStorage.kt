package com.example.produtosdelimpeza.core.map.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private const val PLACES = "saved_places"
private val Context.places by preferencesDataStore(name  = PLACES)
class SavedPlacesLocalStorage @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val gson = Gson()
    private val PLACE_KEY = stringPreferencesKey("place")
    private val listType = object : TypeToken<List<PlaceSuggestionEntity>>() {}.type


    suspend fun savePlace(place: PlaceSuggestionEntity) {
        context.places.edit { preferences ->
            // Lê a string atual
            val jsonAtual = preferences[PLACE_KEY]

            // Converte para lista ou cria uma nova se estiver vazio
            val listaAtual: MutableList<PlaceSuggestionEntity> = if (jsonAtual == null) {
                mutableListOf()
            } else {
                try {
                    gson.fromJson(jsonAtual, listType)
                } catch (e: Exception) {
                    mutableListOf() // Se der erro no parse, reseta a lista
                }
            }

            // Adiciona o novo item (evitando duplicados se quiser)
            if (!listaAtual.any { it.placeId == place.placeId }) {
                listaAtual.add(place)
            }

            // Salva a lista inteira de volta como Array JSON [ ... ]
            preferences[PLACE_KEY] = gson.toJson(listaAtual)
        }
    }


    fun getPlaces(): Flow<List<PlaceSuggestionEntity>> {
        return context.places.data.map { preferences ->
            val json = preferences[PLACE_KEY] ?: return@map emptyList()
            try {
                gson.fromJson(json, listType)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}