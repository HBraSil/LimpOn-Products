package com.example.produtosdelimpeza.store.registration_product.presentation

import com.example.produtosdelimpeza.core.presentation.FieldState

data class CreateProductFormState(
    val nameField: FieldState = FieldState(),
    val descriptionField: FieldState = FieldState(),
    val priceField: FieldState = FieldState(),
    //val promotionalPriceField: FieldState = FieldState(),
    val classificationField: FieldState = FieldState(),
    val categoryField: FieldState = FieldState(),
    val stockCountField: FieldState = FieldState(),
    val formIsValid: Boolean = false
)
