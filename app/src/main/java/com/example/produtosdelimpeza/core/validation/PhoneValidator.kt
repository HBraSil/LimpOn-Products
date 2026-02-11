package com.example.produtosdelimpeza.core.validation


object PhoneValidator {

    fun isValid(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }


        if (digits.length !in 10..11) return false


        val ddd = digits.take(2)
        val number = digits.substring(2)


        if (ddd.startsWith("0")) return false


        if (digits.length == 11 && !number.startsWith("9")) return false


        return true
    }
}
