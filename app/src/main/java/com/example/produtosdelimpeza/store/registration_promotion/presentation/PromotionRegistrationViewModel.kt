package com.example.produtosdelimpeza.store.registration_promotion.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.CategoryValidator
import com.example.produtosdelimpeza.core.validation.DiscountValidator
import com.example.produtosdelimpeza.store.registration_promotion.domain.PromotionRegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PromotionRegistrationViewModel @Inject constructor(
    private val promotionRepository: PromotionRegistrationRepository
) : ViewModel() {

    var promotionFormState by mutableStateOf(CreatePromotionFormState())

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    private val _uiState = MutableStateFlow(CreatePromotionUiState())
    val uiState = _uiState.asStateFlow()



    fun updateDiscountType(field: DiscountType) {
        promotionFormState = promotionFormState.copy(
            discountTypeField = FieldState(
                field = field.name,
                isValid = true,
                error = null
            )
        )

        updateButton()
    }


    fun updateDiscountValue(field: String) {
        val isDiscountValueValid = DiscountValidator.isValid(field)

        promotionFormState = promotionFormState.copy(
                discountValueField = FieldState(
                    field = field,
                    error = isDiscountValueValid,
                    isValid = isDiscountValueValid == null,
                )
            )

        updateButton()
    }

    fun updateCategory(field: String) {
        val isCategoryValid = CategoryValidator.isValid(field)

        promotionFormState = promotionFormState.copy(
            categoryField = FieldState(
                field = field,
                isValid = isCategoryValid == null,
                error = isCategoryValid
            )
        )

        updateButton()
    }

    fun updateDuration(field: Int) {
        promotionFormState = promotionFormState.copy(
            durationField = FieldState(
                field = field.toString(),
                isValid = true,
                error = null
            )
        )

        updateButton()
    }

    fun updateDialogView() {
        _uiState.update { it.copy(success = false) }
    }

    private fun updateButton() {
        val isPromotionValid = with(promotionFormState) {
            discountTypeField.isValid &&
            discountValueField.isValid &&
            durationField.isValid &&
            categoryField.isValid
        }

        promotionFormState = promotionFormState.copy(formIsValid = isPromotionValid)
    }


    fun createPromotion() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val promotion = Promotion(
                discountValue = promotionFormState.discountValueField.field.toInt(),
                discountType = promotionFormState.discountTypeField.field,
                expiration = promotionFormState.durationField.field.toInt(),
                category = promotionFormState.categoryField.field
            )
            when (promotionRepository.createPromotion(promotion)) {
                is FirebaseResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is FirebaseResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                is FirebaseResult.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
            }
        }
    }
}