package com.example.produtosdelimpeza.customer.profile.address.presentation

import com.example.produtosdelimpeza.core.domain.model.Address

data class AddressUiState(
    val address: Address? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isAddressTypeFilled: Boolean = false,
    val isAddressComponentFilled: Boolean = false,
    val addressSavedSuccessfully: Boolean = false,
    val addressDeletedSuccessfully: Boolean = false
)
