package com.example.produtosdelimpeza.core.map.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.AddressType
import com.google.common.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private const val PLACES = "saved_places"
private val Context.datastore by preferencesDataStore(name = PLACES)

class AddressLocalStorage @Inject constructor(@param:ApplicationContext private val context: Context) {

    private val gson = com.google.gson.GsonBuilder()
        .registerTypeAdapter(AddressType::class.java, AddressTypeAdapter())
        .create()
    private val listType = object : TypeToken<List<Address>>() {}.type
    private val PLACE_KEY = stringPreferencesKey("place")
    val PRIMARY_ADDRESS_ID_KEY = stringPreferencesKey("primary_address_id")


    suspend fun savePlace(place: Address): Result<Boolean> {
        return try {
            context.datastore.edit { preferences ->
                // Lê a string atual
                val jsonAtual = preferences[PLACE_KEY]

                // Converte para lista ou cria uma nova se estiver vazio
                val listaAtual: MutableList<Address> = if (jsonAtual == null) {
                    mutableListOf()
                } else {
                    try {
                        gson.fromJson(jsonAtual, listType)
                    } catch (e: Exception) {
                        mutableListOf() // Se der erro no parse, reseta a lista
                    }
                }

                // Adiciona o novo item (evitando duplicados se quiser)
                if (!listaAtual.any { it.id == place.id }) listaAtual.add(place)

                preferences[PLACE_KEY] = gson.toJson(listaAtual)
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao salvar endereço: ${e.message}"))
        }
    }

    fun getAddressesList(): Flow<List<Address>> {
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

    fun getMainAddress(): Flow<Address?> {
        return context.datastore.data.map { preferences ->
            val id = preferences[PRIMARY_ADDRESS_ID_KEY] ?: return@map null

            val json = preferences[PLACE_KEY] ?: return@map null
            try {
                val list = gson.fromJson<List<Address>>(json, listType)

                list.find { it.id == id }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun deleteAddress(placeId: String): Result<Boolean> {
        return try {
            context.datastore.edit { preferences ->
                val json = preferences[PLACE_KEY] ?: return@edit
                val list = gson
                    .fromJson<List<Address>>(json, listType)
                    .toMutableList()

                list.removeIf { it.id == placeId }
                preferences[PLACE_KEY] = gson.toJson(list)
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComplement(addressId: String, complement: String): Result<Boolean> {
        return try {
            context.datastore.edit { preferences ->
                val json = preferences[PLACE_KEY] ?: return@edit
                val list = gson.fromJson<List<Address>>(json, listType)

                val index = list.indexOfFirst { it.id == addressId }
                if (index != -1) {
                    val updatedList = list.toMutableList()
                    updatedList[index] = updatedList[index].copy(complement = complement)
                    preferences[PLACE_KEY] = gson.toJson(updatedList)
                }
            }

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAddressType(id: String, address: AddressType): Result<Boolean> {
        return try {
            context.datastore.edit { preferences ->
                val json = preferences[PLACE_KEY] ?: return@edit
                val list = gson.fromJson<List<Address>>(json, listType)
                val index = list.indexOfFirst { it.id == id }
                if (index != -1) {
                    val updatedList = list.toMutableList()
                    updatedList[index] = updatedList[index].copy(addressType = address)
                    preferences[PLACE_KEY] = gson.toJson(updatedList)
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
