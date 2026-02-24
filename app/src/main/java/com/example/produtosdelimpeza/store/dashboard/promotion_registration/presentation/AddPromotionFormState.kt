package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import com.example.produtosdelimpeza.core.presentation.FieldState


data class AddPromotionFormState (
    val discountTypeField: FieldState = FieldState(),
    val discountValueField: FieldState = FieldState(),
    val durationField: FieldState = FieldState(),
    val categoryField: FieldState = FieldState()
)