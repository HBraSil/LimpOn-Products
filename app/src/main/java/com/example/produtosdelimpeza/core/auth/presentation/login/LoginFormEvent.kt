package com.example.produtosdelimpeza.core.auth.presentation.login

sealed interface LoginFormEvent {
    data class UpdatePassword(val password: String) : LoginFormEvent
    data class UpdateEmail(val email: String) : LoginFormEvent
    object ChangePasswordVisibility : LoginFormEvent
    object LoginWithEmailAndPassword : LoginFormEvent
    object LoginWithGoogle : LoginFormEvent
    //object LoginWithFacebook : LoginFormEvent
    object CleanErrorMessage : LoginFormEvent
}