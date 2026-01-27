package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

sealed class AddProductField {
    data class NameChanged(val value: String) : AddProductField()
    data class ProductDescription(val value: String) : AddProductField()
    data class PriceChanged(val value: String) : AddProductField()
    data class PromotionalPriceChanged(val value: String) : AddProductField()
    data class StockChanged(val value: String) : AddProductField()
    data class CategoryChanged(val value: String) : AddProductField()
}