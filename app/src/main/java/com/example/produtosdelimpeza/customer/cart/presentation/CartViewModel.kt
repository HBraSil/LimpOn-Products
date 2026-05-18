package com.example.produtosdelimpeza.customer.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.customer.cart.domain.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {
    val cartItemsList = repository.observeCartItems()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    val cartQuantity = cartItemsList.map { list ->
        list.sumOf { it.quantity }
    }

    val cartTotalPrice = cartItemsList.map { list ->
        list.sumOf { item ->
                if (item.totalPromotionalPrice > 0)
                    item.totalPromotionalPrice
                else
                    item.totalPrice
        }
    }


    fun onCartEvent(event: CartUiEvent) {
        when (event) {
            is CartUiEvent.AddProductToCart -> addProductToCart(event.product)
            is CartUiEvent.IncreaseQuantity -> increaseQuantity(event.item)
            is CartUiEvent.DecreaseQuantity -> decreaseQuantity(event.id)
            is CartUiEvent.RemoveItem -> removeItem(event.item)
            is CartUiEvent.ClearCart -> clearCart()
        }
    }


    fun decreaseQuantity(id: String) {
        viewModelScope.launch {
            val existingItem = cartItemsList.value.find { it.productId == id }!!
            val product = repository.getProductById(id) ?: return@launch



            if (existingItem.quantity > 1) {
                repository.update(
                    cartItem = existingItem.copy(
                        totalPrice = existingItem.totalPrice - product.price,
                        totalPromotionalPrice = existingItem.totalPromotionalPrice - product.promotionalPrice,
                        quantity = existingItem.quantity - 1
                    )
                )
            } else {
                removeItem(existingItem)
            }
        }
    }


    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            val existingItem = cartItemsList.value.find { it.productId == product.id }

            if (existingItem == null) {
                repository.insert(
                    CartItem(
                        productId = product.id,
                        name = product.name,
                        description = product.description,
                        totalPrice = product.price,
                        totalPromotionalPrice = product.promotionalPrice,
                        quantity = 1
                    )
                )
            } else {
                increaseQuantity(existingItem)
            }
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            val product = repository.getProductById(item.productId)

            product?.let {
                repository.update(
                    cartItem = item.copy(
                        totalPrice = item.totalPrice + product.price,
                        totalPromotionalPrice = item.totalPromotionalPrice + product.promotionalPrice,
                        quantity = item.quantity + 1
                    )
                )
            }
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}

