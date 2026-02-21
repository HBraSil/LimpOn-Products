package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.customer.home.domain.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await


class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HomeRepository {

    override suspend fun getStores(): List<Store> {
        return try {
            val snapshot = firestore
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
}
