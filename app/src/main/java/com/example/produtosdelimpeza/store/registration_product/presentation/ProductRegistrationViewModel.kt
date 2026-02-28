package com.example.produtosdelimpeza.store.registration_product.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.store.registration_product.domain.ProductRegistrationRepository
import com.example.produtosdelimpeza.store.registration_product.presentation.validation.ProductCategoryValidator
import com.example.produtosdelimpeza.store.registration_product.presentation.validation.ProductDescriptionValidator
import com.example.produtosdelimpeza.store.registration_product.presentation.validation.ProductNameValidator
import com.example.produtosdelimpeza.store.registration_product.presentation.validation.ProductPriceValidator
import com.example.produtosdelimpeza.store.registration_product.presentation.validation.ProductStockValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductRegistrationViewModel @Inject constructor(
    private val productRepository: ProductRegistrationRepository,
) : ViewModel() {

    var productFormState by mutableStateOf(CreateProductFormState())

    private val _uiState = MutableStateFlow(CreateProductUiState())
    val uiState = _uiState.asStateFlow()


    fun updateProductName(name: String) {
        val isNameValid = ProductNameValidator.isValid(name)

        productFormState = productFormState.copy(
            nameField =
                FieldState(
                    field = name,
                    error = isNameValid,
                    isValid = isNameValid == null
                )
        )

        updateButton()
    }

    fun updateProductDescription(description: String) {
        val isDescriptionValid = ProductDescriptionValidator.isValid(description)

        productFormState = productFormState.copy(
            descriptionField = FieldState(
                field = description,
                error = isDescriptionValid,
                isValid = isDescriptionValid == null
            )
        )

        updateButton()
    }

    fun updateProductPrice(price: String) {
        val isPriceValid = ProductPriceValidator.isValid(price)

        productFormState = productFormState.copy(
            priceField = FieldState(
                field = price,
                error = isPriceValid,
                isValid = isPriceValid == null
            )
        )

        updateButton()
    }

/*
    fun updateProductPromotionalPrice(price: String) {
        productFormState = productFormState.copy(
            promotionalPriceField = FieldState(
                field = price,
                error = null,
                isValid = true
            )
        )

        updateButton()
    }
*/

    fun updateStock(stock: String) {
        val isStockValid = ProductStockValidator.isValid(stock)


        productFormState = productFormState.copy(
            stockCountField = FieldState(
                field = stock,
                error = isStockValid,
                isValid = isStockValid == null
            )
        )

        updateButton()
    }

    fun updateCategory(category: String) {
        val isCategoryValid = ProductCategoryValidator.isValid(category)


        productFormState = productFormState.copy(
            categoryField = FieldState(
                field = category,
                error = isCategoryValid,
                isValid = isCategoryValid == null
            )
        )

        updateButton()
    }

    fun updateClassification(classification: String) {
        productFormState = productFormState.copy(
            classificationField = FieldState(
                field = classification,
                error = null,
                isValid = true
            )
        )

        updateButton()
    }

    fun updateButton() {
        val isFormValid = with(productFormState) {
            nameField.isValid &&
            descriptionField.isValid &&
            priceField.isValid &&
            stockCountField.isValid &&
            categoryField.isValid &&
            classificationField.isValid
        }

        productFormState = productFormState.copy(
            formIsValid = isFormValid
        )
    }

    fun registerProduct() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {


            val product = Product(
                name = productFormState.nameField.field,
                description = productFormState.descriptionField.field,
                price = productFormState.priceField.field.toDouble(),
                stockCount = productFormState.stockCountField.field.toInt(),
                category = productFormState.categoryField.field,
                classification = productFormState.classificationField.field
            )

            when (productRepository.registerProduct(product)) {
                is FirebaseResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is FirebaseResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                is FirebaseResult.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
            }
        }
    }


    fun signOut() {
        productRepository.signOut()
    }
}

