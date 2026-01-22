package com.example.produtosdelimpeza.core.auth.presentation.signup

data class FieldState(
    val field: String = "",
    val error: String? = null,
    val isValid: Boolean = false
)

data class UserFormState(
    val name: FieldState = FieldState(),
    val lastName: FieldState = FieldState(),
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
    val formIsValid: Boolean = false
)
