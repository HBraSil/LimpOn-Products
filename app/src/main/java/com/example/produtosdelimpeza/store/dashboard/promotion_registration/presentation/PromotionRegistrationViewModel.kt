package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.ValidatePromotionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class PromotionRegistrationViewModel @Inject constructor(
    private val validatePromotionUseCase: ValidatePromotionUseCase
) : ViewModel() {

    private val _promotionFormState = MutableStateFlow(Promotion())
    val promotionFormState = _promotionFormState.asStateFlow()

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    fun onEvent(field: AddPromotionField) {
        when (field) {
            is AddPromotionField.CategoryField -> _promotionFormState.update { it.copy(category = field.value) }
            is AddPromotionField.DiscountTypeField -> _promotionFormState.update {it.copy(discountType = field.value) }
            is AddPromotionField.DiscountValueField -> _promotionFormState.update { it.copy(discountValue = field.value) }
            is AddPromotionField.DurationField -> _promotionFormState.update { it.copy(expirationOffer = field.value) }
        }

        _isValid.update { validatePromotionUseCase(_promotionFormState.value) }
    }
}