package com.example.produtosdelimpeza.customer.profile.address.domain

import com.example.produtosdelimpeza.core.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getSavedPlaces(): Flow<List<Address>>
    fun getMainAddress(): Flow<Address?>
    suspend fun selectMainAddress(id: String)
/*    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(address: Address)*/
}
