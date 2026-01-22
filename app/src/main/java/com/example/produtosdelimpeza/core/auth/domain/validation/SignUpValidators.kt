package com.example.produtosdelimpeza.core.auth.domain.validation

import android.util.Patterns

object SignUpValidators {
    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun isLastNameValid(lastName: String): Boolean {
        return lastName.isNotEmpty()
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
