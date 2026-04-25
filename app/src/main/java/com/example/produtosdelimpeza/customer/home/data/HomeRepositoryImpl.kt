package com.example.produtosdelimpeza.customer.home.data

import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.map.data.AddressLocalStorage
import com.example.produtosdelimpeza.customer.home.domain.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val addressLocalStorage: AddressLocalStorage
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

    override fun getMainAddress(): Flow<Address?> =
        addressLocalStorage.getMainAddress().map { address ->
            address
        }

}
