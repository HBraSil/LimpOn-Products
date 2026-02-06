package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.data.UserLocalDataSource
import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
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

    /*    override suspend fun syncUser() {
        val firebaseUser = userUid.getUserId() ?: return
        val docRef = firestore.collection("users").document(firebaseUser)
        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {
            val newUser = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
            )
            docRef.set(newUser)
        }
    }*/
}