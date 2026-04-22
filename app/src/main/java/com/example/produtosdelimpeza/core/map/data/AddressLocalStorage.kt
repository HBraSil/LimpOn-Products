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
private val Context.datastore by preferencesDataStore(name  = PLACES)
class AddressLocalStorage @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val gson = Gson()
    private val listType = object : TypeToken<List<PlaceSuggestionEntity>>() {}.type
    private val PLACE_KEY = stringPreferencesKey("place")
    val PRIMARY_ADDRESS_ID_KEY = stringPreferencesKey("primary_address_id")


    suspend fun savePlace(place: PlaceSuggestionEntity): Result<Boolean> {
        return try {
            context.datastore.edit { preferences ->
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
                if (!listaAtual.any { it.placeId == place.placeId }) listaAtual.add(place)

                preferences[PLACE_KEY] = gson.toJson(listaAtual)
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao salvar endereço: ${e.message}"))
        }
    }

    fun getAddressList(): Flow<List<PlaceSuggestionEntity>> {
        return context.datastore.data.map { preferences ->
            val json = preferences[PLACE_KEY] ?: return@map emptyList()
            try {
                gson.fromJson(json, listType)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }


    suspend fun selectMainAddress(id: String) {
        context.datastore.edit { preferences ->
            preferences[PRIMARY_ADDRESS_ID_KEY] = id
        }
    }

    fun getMainAddress(): Flow<PlaceSuggestionEntity?> {
        return context.datastore.data.map { preferences ->
            val id = preferences[PRIMARY_ADDRESS_ID_KEY] ?: return@map null

            val json = preferences[PLACE_KEY] ?: return@map null
            try {
                val list = gson.fromJson<List<PlaceSuggestionEntity>>(json, listType)

                list.find { it.placeId == id }
            } catch (e: Exception) {
                null
            }
        }
    }
}