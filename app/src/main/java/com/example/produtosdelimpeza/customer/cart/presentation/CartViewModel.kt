package com.example.produtosdelimpeza.customer.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<ProductEntity>>(emptyList())
    val cartItems: StateFlow<List<ProductEntity>> = _cartItems

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


    fun addOrUpdateProduct(productEntity: ProductEntity) {
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


    fun deleteOrRemoveProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            if (productEntity.quantity > 1) {
                val updatedProduct = productEntity.copy(quantity = productEntity.quantity - 1)

                repository.updateProduct(updatedProduct)
            } else {
                repository.deleteProduct(productEntity)
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

    fun getProductForId(productId: Int): ProductEntity? {
        return _cartItems.value.find { it.id == productId }
    }

    private fun updateTotals(productEntities: List<ProductEntity>) {
        _totalQuantity.value = productEntities.sumOf { it.quantity }
        _totalPrice.value = productEntities.sumOf { it.price * it.quantity }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCart()
        }
    }
}