package com.example.produtosdelimpeza.core.validation


import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object PasswordValidator {
    fun isValidPassword(password: String): UiText.StringResource? {
        if (password.isBlank()) return UiText.StringResource(R.string.error_password_blank)

        if (password.length < 8) return UiText.StringResource(R.string.error_password_invalid)


        return null
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String): UiText.StringResource? {
        if (password.isBlank()) return UiText.StringResource(R.string.error_password_blank)

        if (password.length < 8) return UiText.StringResource(R.string.error_password_invalid)

        if (confirmPassword.isNotBlank() && confirmPassword != password) return UiText.StringResource(R.string.error_confirm_password_invalid)


        return null
    }
}