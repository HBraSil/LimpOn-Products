package com.example.produtosdelimpeza.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.produtosdelimpeza.App
import com.example.produtosdelimpeza.data.CartProductRepository
import com.example.produtosdelimpeza.model.CartProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
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
            Log.d("CartViewModel", "Cart items: ${_cartItems.value}")
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
                repository.update(updated)
            } else {
                repository.insert(product.copy(quantity = 1))
            }
            loadCart() // recarrega após salvar
        }
    }


    private fun updateTotals(products: List<CartProduct>) {
        _totalQuantity.value = products.sumOf { it.quantity }
        _totalPrice.value = products.sumOf { it.price * it.quantity }
    }


    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY]
                val dao = (application as App).db.cartProductsDao()
                val repository = CartProductRepository.getInstance(dao)

                return CartViewModel(repository) as T
            }
        }
    }
}
