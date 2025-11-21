package com.example.produtosdelimpeza.domain.model

sealed class UserProfile {
    data class Client(val name: String) : UserProfile()

    data class Seller(
        val storeId: String,
        val storeName: String
    ) : UserProfile()
}
