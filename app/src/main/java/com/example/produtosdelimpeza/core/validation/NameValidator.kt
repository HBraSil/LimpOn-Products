package com.example.produtosdelimpeza.core.validation

object NameValidator {
    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun isLastNameValid(lastName: String): Boolean {
        return lastName.isNotEmpty()
    }
}