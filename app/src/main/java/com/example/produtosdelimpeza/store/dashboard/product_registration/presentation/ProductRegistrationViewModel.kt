package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.presentation.SessionUserErrors
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ProductRegistrationRepository
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ValidateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductRegistrationViewModel @Inject constructor(
    private val validateProductUseCase: ValidateProductUseCase,
    private val repository: ProductRegistrationRepository
) : ViewModel() {

    private var productFormState = MutableStateFlow(Product())
    val _productFormState = productFormState.asStateFlow()

    private val _uiState = MutableStateFlow(SessionUserErrors())
    val uiState = _uiState.asStateFlow()

    private var isValid = MutableStateFlow(false)
    val _isValid = isValid.asStateFlow()



    fun onEvent(field: AddProductField) {
        when (field) {
            is AddProductField.NameField -> productFormState.update {it.copy(productName = field.value) }
            is AddProductField.ProductDescriptionField -> productFormState.update { it.copy(productDescription = field.value) }
            is AddProductField.PriceField -> productFormState.update { it.copy(productPrice = field.value) }
            is AddProductField.PromotionalPriceField -> productFormState.update { it.copy(promotionalPrice = field.value) }
            is AddProductField.StockField -> productFormState.update { it.copy(stockCount = field.value.toIntOrNull() ?: 0) }
            is AddProductField.CategoryField -> productFormState.update { it.copy(productCategory = field.value) }
        }

        isValid.update { validateProductUseCase(productFormState.value) }
    }


    fun registerProduct(product: Product) {
        viewModelScope.launch {
            when (repository.registerProduct(product)) {
                is AppResult.Error.SessionExpired -> _uiState.update { it.copy(showSessionExpired = true) }
                is AppResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is AppResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                else -> {}
            }
        }
    }
}

