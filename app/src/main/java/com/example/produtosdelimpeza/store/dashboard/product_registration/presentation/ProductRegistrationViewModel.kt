package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.Product
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

    private var isValid = MutableStateFlow(false)
    val _isValid = isValid.asStateFlow()



    fun onEvent(field: AddProductField) {
        when (field) {
            is AddProductField.NameChanged -> productFormState.update {it.copy(productName = field.value) }
            is AddProductField.ProductDescription -> productFormState.update { it.copy(productDescription = field.value) }
            is AddProductField.PriceChanged -> productFormState.update { it.copy(productPrice = field.value) }
            is AddProductField.PromotionalPriceChanged -> productFormState.update { it.copy(promotionalPrice = field.value) }
            is AddProductField.StockChanged -> productFormState.update { it.copy(stockCount = field.value.toIntOrNull() ?: 0) }
            is AddProductField.CategoryChanged -> productFormState.update { it.copy(productCategory = field.value) }
        }

        isValid.update { validateProductUseCase(productFormState.value) }
    }


    fun saveProduct(product: Product) {
        viewModelScope.launch {
            val result = repository.registerProduct(product)

            result.fold(
                onSuccess = {
                    println("Produto salvo com sucesso!")
                },
                onFailure = { error ->
                    println("Erro ao salvar: ${error.message}")
                }
            )
        }
    }
}

