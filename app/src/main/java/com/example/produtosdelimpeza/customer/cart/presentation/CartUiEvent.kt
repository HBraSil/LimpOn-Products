package com.example.produtosdelimpeza.customer.cart.presentation

import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.customer.cart.domain.CartItem

interface CartUiEvent {
    data class AddProductToCart(val product: Product) : CartUiEvent
    data class IncreaseQuantity(val item: CartItem) : CartUiEvent
    data class DecreaseQuantity(val id: String) : CartUiEvent
    data class RemoveItem(val item: CartItem) : CartUiEvent
    object ClearCart : CartUiEvent
}