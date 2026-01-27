package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

sealed class AddProductEvent {
    data class NameChanged(val value: String) : AddProductEvent()
    data class ProductDescription(val value: String) : AddProductEvent()
    data class PriceChanged(val value: String) : AddProductEvent()
    data class PromotionalPriceChanged(val value: String) : AddProductEvent()
    data class StockChanged(val value: String) : AddProductEvent()
    data class CategoryChanged(val value: String) : AddProductEvent()
}