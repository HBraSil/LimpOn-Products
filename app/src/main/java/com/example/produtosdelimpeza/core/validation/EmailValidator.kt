package com.example.produtosdelimpeza.core.validation

import android.util.Patterns

object EmailValidator {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}