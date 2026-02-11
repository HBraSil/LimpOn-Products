package com.example.produtosdelimpeza.store.onboarding.domain

import com.example.produtosdelimpeza.core.presentation.FieldState

data class SignUpStoreFormState(
    val storeName: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val category: FieldState = FieldState(),
    val phone: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val formIsValid: Boolean = false
)
