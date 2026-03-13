package com.example.produtosdelimpeza.customer.cart.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.core.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    private val _quantities = mutableStateMapOf<String, Int>()
    val quantities: Map<String, Int> get() = _quantities

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

/*
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


    fun addOrUpdateProduct(productEntity: Product) {
        viewModelScope.launch {
            val currentList = _cartItems.value.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.id == productEntity.id }

            if (existingIndex >= 0) {
                val existingItem = currentList[existingIndex]
                val updated = existingItem.copy(quantity = existingItem.quantity + 1)
                repository.updateProduct(updated)
            } else {
                repository.insertProduct(productEntity.copy(quantity = 1))
            }
            loadCart() // recarrega após salvar
        }
    }


    fun deleteOrRemoveProduct(product: ProductEntity) {
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
*/

/*
    fun getTotalPriceForProduct(productId: Int): Double {
        val product = _cartItems.value.find { it.id == productId }
        val quantity = product?.quantity ?: 0
        val totalPriceForThisProduct = product?.price?.times(quantity) ?: 0.0

        return totalPriceForThisProduct
    }

    fun getProductForId(productId: Int): Product? {
        return _cartItems.value.find { it.id == productId }
    }

    private fun updateTotals(listOfProducts: List<Product>) {
        _totalQuantity.value = listOfProducts.sumOf { it.quantity }
        _totalPrice.value = listOfProducts.sumOf { it.price * it.quantity }
    }
*/


    fun decreaseQuantity(productId: String) {
        _quantities[productId]?.let {
            if ( it > 0) {
                _quantities[productId] = it - 1
            }
        }
    }

    fun increaseQuantity(productId: String, price: Double) {
        val qtd = _quantities[productId] ?: 0
        qtd.let {
            _quantities[productId] = it + 1
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}