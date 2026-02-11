package com.example.produtosdelimpeza.store.onboarding.data

import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.example.produtosdelimpeza.core.domain.model.Store
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class StoreRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val session: AuthSessionProvider
) {
    fun saveStoreRemote(store: Store): Result<Boolean> {
        return try {
            val uid = session.getUserId()
            val storeSaved = firestore.collection("stores").document(uid!!).set(store)

            if (storeSaved.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Erro ao salvar a loja"))
            }
        } catch (e: Exception) {
            throw e
        }
    }
}