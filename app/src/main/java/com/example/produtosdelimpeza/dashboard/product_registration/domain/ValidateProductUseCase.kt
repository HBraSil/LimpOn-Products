package com.example.produtosdelimpeza.dashboard.product_registration.domain

import com.example.produtosdelimpeza.core.data.Product
import javax.inject.Inject

class ValidateProductUseCase @Inject constructor() {
    operator fun invoke(product: Product): Boolean {
        val formIsValid = with(product) {
            productDescription.isNotBlank() &&
            //classification.isBlank() ||
            productPrice.isNotBlank() &&
            productName.isNotBlank()
           // classification.isBlank()
        }

        return formIsValid
    }

}