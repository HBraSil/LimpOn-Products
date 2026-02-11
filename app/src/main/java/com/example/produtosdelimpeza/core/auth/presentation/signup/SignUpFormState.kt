package com.example.produtosdelimpeza.core.auth.presentation.signup

import com.example.produtosdelimpeza.core.presentation.FieldState

data class UserFormState(
    val name: FieldState = FieldState(),
    val lastName: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
    val formIsValid: Boolean = false
)
