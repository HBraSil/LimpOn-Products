package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.core.presentation.SessionUserErrors
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.ValidatePromotionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PromotionRegistrationViewModel @Inject constructor(
    private val validatePromotionUseCase: ValidatePromotionUseCase,
    private val promotionRepository: PromotionRegistrationRepository
) : ViewModel() {

    private val _promotionFormState = MutableStateFlow(Promotion())
    val promotionFormState = _promotionFormState.asStateFlow()

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    private val _uiState = MutableStateFlow(SessionUserErrors())
    val uiState = _uiState.asStateFlow()


    fun onEvent(field: AddPromotionField) {
        when (field) {
            is AddPromotionField.CategoryField -> _promotionFormState.update { it.copy(category = field.value) }
            is AddPromotionField.DiscountTypeField -> _promotionFormState.update {it.copy(discountType = field.value) }
            is AddPromotionField.DiscountValueField -> _promotionFormState.update { it.copy(discountValue = field.value) }
            is AddPromotionField.DurationField -> _promotionFormState.update { it.copy(expirationOffer = field.value) }
        }

        _isValid.update { validatePromotionUseCase(_promotionFormState.value) }
    }

    fun createPromotion(promotion: Promotion) {
        viewModelScope.launch {
            when (promotionRepository.createPromotion(promotion)) {
                is AppResult.Error.SessionExpired -> _uiState.update { it.copy(showSessionExpired = true) }
                is AppResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is AppResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                else -> {}
            }
        }
    }
}