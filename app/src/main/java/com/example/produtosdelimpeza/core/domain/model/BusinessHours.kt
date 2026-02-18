package com.example.produtosdelimpeza.core.domain.model


enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

data class BusinessHours(
    var openTime: String = "",
    var closeTime: String = "",
)