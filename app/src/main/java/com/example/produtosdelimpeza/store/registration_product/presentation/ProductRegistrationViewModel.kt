package com.example.produtosdelimpeza.store.registration_product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.store.registration_product.domain.ProductRegistrationRepository
import com.example.produtosdelimpeza.store.registration_product.domain.ValidateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductRegistrationViewModel @Inject constructor(
    private val validateProductUseCase: ValidateProductUseCase,
    private val productRepository: ProductRegistrationRepository
) : ViewModel() {

    private var _productFormState = MutableStateFlow(Product())
    val productFormState = _productFormState.asStateFlow()

    private val _uiState = MutableStateFlow(CreateProductUiState())
    val uiState = _uiState.asStateFlow()

    private var _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()



    fun onEvent(field: AddProductField) {
        when (field) {
            is AddProductField.NameField -> _productFormState.update {it.copy(productName = field.value) }
            is AddProductField.ProductDescriptionField -> _productFormState.update { it.copy(productDescription = field.value) }
            is AddProductField.PriceField -> _productFormState.update { it.copy(productPrice = field.value) }
            is AddProductField.PromotionalPriceField -> _productFormState.update { it.copy(promotionalPrice = field.value) }
            is AddProductField.StockField -> _productFormState.update { it.copy(stockCount = field.value.toIntOrNull() ?: 0) }
            is AddProductField.CategoryField -> _productFormState.update { it.copy(productCategory = field.value) }
        }

        _isValid.update { validateProductUseCase(_productFormState.value) }
    }


    fun registerProduct(product: Product) {
        viewModelScope.launch {
            when (productRepository.registerProduct(product)) {
                is FirebaseResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is FirebaseResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                else -> {}
            }
        }
    }


    fun signOut() {
        productRepository.signOut()
    }
}

