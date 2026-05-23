package com.example.produtosdelimpeza.core.data

import android.util.Log
import com.example.produtosdelimpeza.store.dashboard.data.StoreDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class StoreRemoteDataSource @Inject constructor(
    private val firebase: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {
    suspend fun saveStoreRemote(store: StoreDto): Result<Boolean> {
        return try {

            val userUid = firebase.currentUser?.uid
            val uid = userUid ?: error("Usuário não autenticado")

            Log.d("StoreRemoteDataSource", "Store: $uid")
            val docRef = firestore
                .collection("stores")
                .document()

            val storeWithOwner = store.copy(
                id = docRef.id,
                ownerId = uid
            )

            docRef.set(storeWithOwner).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchStoreRemote(storeId: String): Result<StoreDto> {
        return try {
            val snapshot = firestore
                .collection("stores")
                .document(storeId)
                .get()
                .await()

            if (!snapshot.exists()) {
                error("Loja não encontrada")
            }

            val dto = snapshot.toObject(StoreDto::class.java)
                ?: error("Erro ao converter dados da loja")

            Result.success(dto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllStoresNames(): List<StoreDto> {
        return try {
            val userUid = firebase.currentUser?.uid
            val uid = userUid ?: error("Usuário não logado")

            val snapshot = firestore.collection("stores")
                .whereEqualTo("ownerId", uid)
                .get()
                .await()

//            snapshot.documents.mapNotNull { it.getString("name") }
            snapshot.documents.mapNotNull { document ->
                val name = document.getString("name")
                val id = document.id

                if (name != null) {
                    StoreDto(
                        id = id,
                        name = name
                    )
                } else {
                    null
                }
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateName(storeId: String, newName: String): Result<Boolean> {
        return try {
            val storeRef = firestore.collection("stores").document(storeId)

            storeRef.update("name", newName).await()
                ?: Log.d("StoreRemoteDataSource", "Falha ao atualizar")

            Log.d("StoreRemoteDataSource", "Nome da loja atualizado com sucesso")
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}