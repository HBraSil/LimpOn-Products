package com.example.produtosdelimpeza.core.auth.presentation.login

import com.example.produtosdelimpeza.core.presentation.FieldState

data class LoginFormState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val isLoginFieldValid: Boolean = false,
)