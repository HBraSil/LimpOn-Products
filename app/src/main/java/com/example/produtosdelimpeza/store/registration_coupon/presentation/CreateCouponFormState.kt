package com.example.produtosdelimpeza.store.registration_coupon.presentation

import com.example.produtosdelimpeza.core.presentation.FieldState

data class CreateCouponFormState (
    val codeField: FieldState = FieldState(),
    val discountTypeField: FieldState = FieldState(),
    val discountValueField: FieldState = FieldState(),
    val durationField: FieldState = FieldState(),
    val categoryField: FieldState = FieldState(),
    val formIsValid: Boolean = false,
)