package com.example.produtosdelimpeza.store.onboarding.domain

import com.example.produtosdelimpeza.core.domain.model.DayOfWeek
import com.example.produtosdelimpeza.core.presentation.FieldState

data class SignUpStoreFormState(
    val storeName: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val category: FieldState = FieldState(),
    val phone: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val openHours: Map<DayOfWeek, FieldState> = emptyMap(),
    val closeHours: Map<DayOfWeek, FieldState> = emptyMap(),
    val formIsValid: Boolean = false
)
