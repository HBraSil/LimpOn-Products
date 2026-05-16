package com.example.produtosdelimpeza.customer.cart.presentation

import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.customer.cart.domain.CartItem

sealed interface CartScreenEvent {
    data class AddProductToCart(val product: Product) : CartScreenEvent
    data class IncreaseQuantity(val item: CartItem) : CartScreenEvent
    data class DecreaseQuantity(val id: String) : CartScreenEvent
    data class RemoveItem(val item: CartItem) : CartScreenEvent
    object ClearCart : CartScreenEvent
}