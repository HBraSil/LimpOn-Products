package com.example.produtosdelimpeza.dashboard.product_registration.presentation

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.data.Product
import com.example.produtosdelimpeza.dashboard.product_registration.domain.ValidateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ProductRegistrationViewModel @Inject constructor(
    private val validateProductUseCase: ValidateProductUseCase,
) : ViewModel() {

    private var productFormState = MutableStateFlow(Product())
    val _productFormState = productFormState.asStateFlow()

    private var isValid = MutableStateFlow(false)
    val _isValid = isValid.asStateFlow()



    fun onEvent(field: AddProductEvent) {
        when (field) {
            is AddProductEvent.NameChanged -> productFormState.update {it.copy(productName = field.value) }
            is AddProductEvent.ProductDescription -> productFormState.update { it.copy(productDescription = field.value) }
            is AddProductEvent.PriceChanged -> productFormState.update { it.copy(productPrice = field.value) }
            is AddProductEvent.PromotionalPriceChanged -> productFormState.update { it.copy(promotionalPrice = field.value) }
            is AddProductEvent.StockChanged -> productFormState.update { it.copy(stockCount = field.value.toIntOrNull() ?: 0) }
            is AddProductEvent.CategoryChanged -> productFormState.update { it.copy(productCategory = field.value) }
        }

        isValid.update { validateProductUseCase(productFormState.value) }
    }


    fun saveProduct(product: Product) {
        // Lógica para salvar o produto
    }
}

