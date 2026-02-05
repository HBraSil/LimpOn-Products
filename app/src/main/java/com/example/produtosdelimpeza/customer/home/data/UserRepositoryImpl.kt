package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override fun getUser(): Flow<User?> {
        val userId = firebaseAuth.currentUser?.uid ?: return flowOf(null)
        return userLocalDataSource.observeUser(userId)
    }

    override suspend fun syncUser() {
        val firebaseUser = firebaseAuth.currentUser ?: return
        val docRef = firestore.collection("users").document(firebaseUser.uid)
        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {
            val newUser = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
            )
            docRef.set(newUser)
        }
    }
}