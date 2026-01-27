package com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain

import android.util.Log
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.core.domain.DiscountType
import com.example.produtosdelimpeza.core.domain.ValidityType
import javax.inject.Inject


class ValidateCouponUseCase @Inject constructor() {
    operator fun invoke(coupon: Coupon): Boolean {
        val formIsValid = with(coupon) {
            couponCode.isNotBlank() &&
            discountType != DiscountType.NONE &&
            discountValue.isNotBlank() &&
            validityType != ValidityType.NONE
        }

        Log.d("RESULT", "$formIsValid")
        return formIsValid
    }
}