package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import com.example.produtosdelimpeza.core.presentation.FieldState

data class CreateCouponFormState (
    val couponCodeField: FieldState = FieldState(),
    val discountTypeField: FieldState = FieldState(),
    val discountValueField: FieldState = FieldState(),
    val durationField: FieldState = FieldState(),
    val categoryField: FieldState = FieldState(),
    val formIsValid: Boolean = false,
)