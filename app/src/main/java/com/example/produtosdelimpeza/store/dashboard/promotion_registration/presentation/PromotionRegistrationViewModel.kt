package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.presentation.SessionUserErrors
import com.example.produtosdelimpeza.core.validation.CategoryValidator
import com.example.produtosdelimpeza.core.validation.DiscountValidator
import com.example.produtosdelimpeza.core.validation.DurationValidator
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class PromotionRegistrationViewModel @Inject constructor(
    private val promotionRepository: PromotionRegistrationRepository
) : ViewModel() {

    var promotionFormState by mutableStateOf(AddPromotionFormState())

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    private val _uiState = MutableStateFlow(SessionUserErrors())
    val uiState = _uiState.asStateFlow()



    fun updateDiscountValue(field: String) {
        val isDiscountValueValid = DiscountValidator.isValid(field)

        promotionFormState = promotionFormState.copy(
                discountValueField = FieldState(
                    field = field,
                    error = isDiscountValueValid,
                    isValid = isDiscountValueValid == null,
                )
            )
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
    }

    fun updateDuration(field: Int?) {
        val isDurationValid = DurationValidator.isValid(field)

        promotionFormState = promotionFormState.copy(
            durationField = FieldState(
                field = field.toString(),
                isValid = isDurationValid == null,
                error = isDurationValid
            )
        )
    }


//    fun createPromotion() {
//        viewModelScope.launch {
//            when (promotionRepository.createPromotion(promotionFormState)) {
//                is AppResult.Error.SessionExpired -> _uiState.update { it.copy(showSessionExpired = true) }
//                is AppResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
//                is AppResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
//                else -> {}
//            }
//        }
//    }
}