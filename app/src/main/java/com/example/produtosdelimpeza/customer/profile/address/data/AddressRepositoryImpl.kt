package com.example.produtosdelimpeza.customer.profile.address.data

import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.map.data.AddressLocalStorage
import com.example.produtosdelimpeza.core.map.data.PlaceSuggestionMapper.toAddress
import com.example.produtosdelimpeza.customer.profile.address.domain.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AddressRepositoryImpl @Inject constructor(
    private val addressLocalStorage: AddressLocalStorage
) : AddressRepository {
    override fun getSavedPlaces(): Flow<List<Address>> = addressLocalStorage.getAddressList().map { entities ->
        entities.map { it.toAddress() }
    }

    override fun getMainAddress(): Flow<Address?> =
        addressLocalStorage.getMainAddress().map { entity ->
            entity?.toAddress()
        }

    override suspend fun selectMainAddress(id: String) {
        addressLocalStorage.selectMainAddress(id)
    }
}