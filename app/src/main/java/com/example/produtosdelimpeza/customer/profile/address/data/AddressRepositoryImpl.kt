package com.example.produtosdelimpeza.customer.profile.address.data

import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.AddressType
import com.example.produtosdelimpeza.core.map.data.AddressLocalStorage
import com.example.produtosdelimpeza.customer.profile.address.domain.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AddressRepositoryImpl @Inject constructor(
    private val addressLocalStorage: AddressLocalStorage
) : AddressRepository {
    override fun getSavedPlaces(): Flow<List<Address>> = addressLocalStorage.getAddressesList().map { entities ->
        entities.map { it }
    }

    override fun getMainAddress(): Flow<Address?> =
        addressLocalStorage.getMainAddress().map { address ->
            address
        }

    override suspend fun selectMainAddress(id: String) {
        addressLocalStorage.selectMainAddress(id)
    }

    override suspend fun updateComplement(id: String, complement: String): Result<Boolean> {
        return addressLocalStorage.updateComplement(id, complement)
    }

    override suspend fun updateAddressType(id: String, address: AddressType): Result<Boolean> {
        return addressLocalStorage.updateAddressType(id, address)
    }
}