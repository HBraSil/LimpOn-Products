package com.example.produtosdelimpeza.model

data class LoginFormState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val rememberMe: Boolean = false,
    val isLoginFieldValid: Boolean = false,
)
