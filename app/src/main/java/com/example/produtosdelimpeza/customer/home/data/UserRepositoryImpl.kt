package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebase: FirebaseAuth,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override fun getUser(): Flow<User?> {
        val userUid = firebase.currentUser?.uid
        val userId = userUid ?: return flowOf(null)
        return userLocalDataSource.observeUser(userId)
    }

    override suspend fun saveUser(user: User) {
        userLocalDataSource.saveUser(user)
    }
}