package com.example.produtosdelimpeza.core.validation

object PasswordValidator {
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
    fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}