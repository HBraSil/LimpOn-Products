package com.example.produtosdelimpeza.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.data.CartProductRepository
import com.example.produtosdelimpeza.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartProductRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartItems: StateFlow<List<CartProduct>> = _cartItems

    private val _totalQuantity = MutableStateFlow(0)
    val totalQuantity: StateFlow<Int> = _totalQuantity

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice


    init {
        viewModelScope.launch {
            loadCart()
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            val products = repository.getAllProducts()

            _cartItems.value = products
            updateTotals(products)
        }
    }


    fun addOrUpdateProduct(product: CartProduct) {
        viewModelScope.launch {
            val currentList = _cartItems.value.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.id == product.id }

            if (existingIndex >= 0) {
                val existingItem = currentList[existingIndex]
                val updated = existingItem.copy(quantity = existingItem.quantity + 1)
                repository.updateProduct(updated)
            } else {
                repository.insertProduct(product.copy(quantity = 1))
            }
            loadCart() // recarrega após salvar
        }
    }


    fun deleteOrRemoveProduct(product: CartProduct) {
        viewModelScope.launch {
            if (product.quantity > 1) {
                val updatedProduct = product.copy(quantity = product.quantity - 1)

                repository.updateProduct(updatedProduct)
            } else {
                repository.deleteProduct(product)
            }
            loadCart()
        }
    }

    fun getTotalPriceForProduct(productId: Int): Double {
        val product = _cartItems.value.find { it.id == productId }
        val quantity = product?.quantity ?: 0
        val totalPriceForThisProduct = product?.price?.times(quantity) ?: 0.0

        return totalPriceForThisProduct
    }

    fun getProductForId(productId: Int): CartProduct? {
        return _cartItems.value.find { it.id == productId }
    }

    private fun updateTotals(products: List<CartProduct>) {
        _totalQuantity.value = products.sumOf { it.quantity }
        _totalPrice.value = products.sumOf { it.price * it.quantity }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCart()
        }
    }
}
