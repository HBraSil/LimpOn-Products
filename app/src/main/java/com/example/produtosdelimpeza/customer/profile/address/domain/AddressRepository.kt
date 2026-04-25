package com.example.produtosdelimpeza.customer.profile.address.domain

import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.AddressType
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getSavedPlaces(): Flow<List<Address>>
    fun getMainAddress(): Flow<Address?>
    suspend fun selectMainAddress(id: String)

    suspend fun updateComplement(id: String,complement: String)

    suspend fun updateAddressType(id: String, address: AddressType)
/*    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(address: Address)*/
}
