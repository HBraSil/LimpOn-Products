package com.example.produtosdelimpeza.customer.profile.address.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.AddressType
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.customer.profile.address.domain.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
    private val mapRepository: MapRepository,
) : ViewModel() {

    var addressState = MutableStateFlow(AddressUiState())
        private set


    val getSavedPlaces = addressRepository.getSavedPlaces().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val getSelectedAddress = addressRepository.getMainAddress().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun selectMainAddress(id: String) {
        viewModelScope.launch {
            Log.d("AddressViewModel", "selected address: $id, current address: ${getSelectedAddress.value?.id}")
            addressRepository.selectMainAddress(id)
        }
    }

    fun addAddress(id: String) {
        viewModelScope.launch {
            Log.d("TESTE", "IDAddress: $id")
            val result = mapRepository.savePlace(id)
            if(result.isSuccess) {
                addressState.value = addressState.value.copy(
                    addressSavedSuccessfully = true
                )
            } else {
                Log.d("AddressViewModel", "Error adding address: ${result.exceptionOrNull()}")
            }
        }
    }

    fun deleteAddress(placeId: String) {
        viewModelScope.launch {
            mapRepository.deleteAddress(placeId)
            Log.d("deleteAddress", "Address deleted: $placeId")
        }
    }

    fun updateAddressType(id: String, addressType: AddressType) {
        addressState.update { addressState ->
            addressState.copy(
                address = addressState.address?.copy(id = id, addressType = addressType),
                isAddressTypeFilled = validateAddress(addressType)
            )
        }
    }

    fun updateComplement(id: String, complement: String) {
        addressState.update { addressState ->
            addressState.copy(
                address = addressState.address?.copy(id = id, complement = complement),
                isAddressComponentFilled = validateComplementField(complement)
            )
        }
    }

    fun addressToEdit(address: Address?) {
        addressState.update {
            it.copy(
                address = address
            )
        }
    }

    private fun validateAddress(address: AddressType?): Boolean {
        if (address == null) return false

        return when (address) {
            AddressType.Home -> true
            AddressType.Work -> true
            is AddressType.Other -> address.customLabel.isNotBlank()
        }
    }

    fun validateComplementField(address: String): Boolean {
        return address.isNotBlank()
    }

    fun saveEditButton() {
        viewModelScope.launch {
            val state = addressState.value
            val address = state.address ?: return@launch

            if (state.isAddressTypeFilled) {
                address.addressType?.let {
                    addressRepository.updateAddressType(address.id, it)
                }
            }

            if (state.isAddressComponentFilled) {
                addressRepository.updateComplement(address.id, address.complement)
            }
        }
    }
}