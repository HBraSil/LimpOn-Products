package com.example.produtosdelimpeza.customer.home.domain

import com.example.produtosdelimpeza.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User?>

//    suspend fun syncUser()
}