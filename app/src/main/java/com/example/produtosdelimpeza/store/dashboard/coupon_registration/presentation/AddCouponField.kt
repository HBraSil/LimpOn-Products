package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import com.example.produtosdelimpeza.core.domain.DiscountType
import com.example.produtosdelimpeza.core.domain.ValidityType

sealed class AddCouponField {
    data class CouponCodeField(val value: String) : AddCouponField()
    data class DiscountTypeField(val value: DiscountType) : AddCouponField()
    data class DiscountValueField(val value: String) : AddCouponField()
    data class ValidityField(val value: ValidityType) : AddCouponField()
}