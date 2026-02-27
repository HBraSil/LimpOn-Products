package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.CategoryValidator
import com.example.produtosdelimpeza.core.validation.CouponCodeValidator
import com.example.produtosdelimpeza.core.validation.DiscountValidator
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponRegistrationViewModel @Inject constructor(
    private val couponRegistrationRepository: CouponRegistrationRepository,
) : ViewModel() {

    var couponFormState by mutableStateOf(CreateCouponFormState())

    private val _uiState = MutableStateFlow(CreateCouponUiState())
    val uiState = _uiState.asStateFlow()



    fun updateCouponCode(field: String) {
        val isCouponCodeValid = CouponCodeValidator.isValid(field)

        couponFormState = couponFormState.copy(
            couponCodeField = FieldState(
                field = field,
                error = isCouponCodeValid,
                isValid = isCouponCodeValid == null,
            )
        )

        updateButton()
    }

    fun updateDiscountType(field: DiscountType) {
        couponFormState = couponFormState.copy(
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

        couponFormState = couponFormState.copy(
            discountValueField = FieldState(
                field = field,
                error = isDiscountValueValid,
                isValid = isDiscountValueValid == null,
            )
        )

        updateButton()
    }

    fun updateDuration(field: Int?) {

        couponFormState = couponFormState.copy(
            durationField = FieldState(
                field = field.toString(),
                isValid = true,
                error = null
            )
        )

        updateButton()
    }

    fun updateCategory(field: String) {
        val isCategoryValid = CategoryValidator.isValid(field)

        couponFormState = couponFormState.copy(
            categoryField = FieldState(
                field = field,
                isValid = isCategoryValid == null,
                error = isCategoryValid
            )
        )

        updateButton()
    }

    fun updateDialogView() {
        _uiState.update { it.copy(success = false) }
    }



    private fun updateButton() {
        val isCouponValid = with(couponFormState) {
            couponCodeField.isValid &&
            discountTypeField.isValid &&
            discountValueField.isValid &&
            durationField.isValid &&
            categoryField.isValid
        }

        couponFormState = couponFormState.copy(
            formIsValid = isCouponValid
        )
    }

    fun createCoupon() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val coupon = Coupon(
                couponCode = couponFormState.couponCodeField.field,
                discountValue = couponFormState.discountValueField.field.toInt(),
                discountType = couponFormState.discountTypeField.field,
                expiration = couponFormState.durationField.field.toInt(),
                category = couponFormState.categoryField.field
            )
            when (couponRegistrationRepository.createCoupon(coupon)) {
                is FirebaseResult.Error.Network -> _uiState.update { it.copy(showNoInternet = true) }
                is FirebaseResult.Error.Unknown -> _uiState.update { it.copy(unknwonError = true) }
                is FirebaseResult.Success -> _uiState.update { it.copy(isLoading = false, success = true) }
            }
        }
    }

    fun signOut() {
        couponRegistrationRepository.signOut()
    }
}