package com.example.produtosdelimpeza.store.onboarding.data

import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.data.StoreDto
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class StoreRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val session: AuthSessionProvider
) {
    suspend fun saveStoreRemote(store: StoreDto): Result<Boolean> {
        return try {
            val uid = session.getUserId() ?: error("Usuário não autenticado")

            firestore.collection("stores").document(uid).set(store).await()

            Result.success(true)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStoreRemote(): Result<StoreDto> {
        return runCatching {
            val uid = session.getUserId()!!

            val snapshot = firestore
                .collection("stores")
                .document(uid)
                .get()
                .await()

            if (!snapshot.exists()) {
                error("Loja não encontrada")
            }

            val dto = snapshot.toObject(StoreDto::class.java)
                ?: error("Erro ao converter dados da loja")

            dto
        }
    }
}