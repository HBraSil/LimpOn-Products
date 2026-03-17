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


/*
    fun changeToCartItemAndUpdate(product: Product) {
        increaseQuantity(
            CartItem(
                productId = product.id,
                name = product.name,
                description = product.description,
                price = product.price,
                promotionalPrice = product.promotionalPrice,
                quantity = 1
            )
        )
    }
*/


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