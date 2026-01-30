package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.core.presentation.SessionUserErrors
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRepository
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.ValidateCouponUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponRegistrationViewModel @Inject constructor(
    private val couponRepository: CouponRepository,
    private val validateCouponUseCase: ValidateCouponUseCase,
) : ViewModel() {

    private val _couponFormState = MutableStateFlow(Coupon())
    val couponFormState = _couponFormState.asStateFlow()

    private val _uiState = MutableStateFlow(SessionUserErrors())
    val uiState = _uiState.asStateFlow()

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    fun onEvent(field: AddCouponField) {
        when (field) {
            is AddCouponField.CouponCodeField -> _couponFormState.update { it.copy(couponCode = field.value) }
            is AddCouponField.DiscountTypeField -> _couponFormState.update {it.copy(discountType = field.value) }
            is AddCouponField.DiscountValueField -> _couponFormState.update { it.copy(discountValue = field.value) }
            is AddCouponField.DurationField -> _couponFormState.update { it.copy(expirationOffer = field.value) }
            is AddCouponField.CategoryField -> _couponFormState.update { it.copy(category = field.value) }
        }

        _isValid.update { validateCouponUseCase(_couponFormState.value) }
    }

    fun createCoupon(coupon: Coupon) {
        viewModelScope.launch {
            when (couponRepository.createCoupon(coupon)) {
                is AppResult.Error.SessionExpired -> _uiState.update { it.copy(showSessionExpired = true) }
                is AppResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is AppResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                else -> {}
            }
        }
    }

    fun signOut() {
        couponRepository.signOut()
    }
}