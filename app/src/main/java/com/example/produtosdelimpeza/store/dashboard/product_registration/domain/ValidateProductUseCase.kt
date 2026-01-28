package com.example.produtosdelimpeza.store.dashboard.product_registration.domain

import com.example.produtosdelimpeza.core.domain.Product
import javax.inject.Inject

class ValidateProductUseCase @Inject constructor() {
    operator fun invoke(product: Product): Boolean {
        val formIsValid = with(product) {
            productDescription.isNotBlank() &&
            productPrice.isNotBlank() &&
            productName.isNotBlank()
            //classification.isNotBlank()
        }

        return formIsValid
    }
}