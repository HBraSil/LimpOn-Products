package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer

sealed class AddCouponField {
    data class CouponCodeField(val value: String) : AddCouponField()
    data class DiscountTypeField(val value: DiscountType) : AddCouponField()
    data class DiscountValueField(val value: String) : AddCouponField()
    data class DurationField(val value: ExpirationOffer) : AddCouponField()
    data class CategoryField(val value: String) : AddCouponField()
}