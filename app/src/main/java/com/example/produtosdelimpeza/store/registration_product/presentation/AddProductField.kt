package com.example.produtosdelimpeza.store.registration_product.presentation

sealed class AddProductField {
    data class NameField(val value: String) : AddProductField()
    data class ProductDescriptionField(val value: String) : AddProductField()
    data class PriceField(val value: String) : AddProductField()
    data class PromotionalPriceField(val value: String) : AddProductField()
    data class StockField(val value: String) : AddProductField()
    data class CategoryField(val value: String) : AddProductField()
}