package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userUid: AuthSessionProvider,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override fun getUser(): Flow<User?> {
        val userId = userUid.getUserId() ?: return flowOf(null)
        return userLocalDataSource.observeUser(userId)
    }

    override suspend fun getStores(): List<Store> {
        return try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("stores")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Store::class.java)
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveUser(user: User) {
        userLocalDataSource.saveUser(user)
    }
}