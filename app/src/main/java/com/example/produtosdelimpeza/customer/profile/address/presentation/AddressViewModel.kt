package com.example.produtosdelimpeza.customer.profile.address.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.customer.profile.address.domain.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

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
}