package com.example.produtosdelimpeza.core.data

import android.util.Log
import com.example.produtosdelimpeza.core.domain.Product
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ProductRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {


    suspend fun fetchAllProductsRemote(storeId: String): Result<List<Product>> {
        return try {
            Log.d("ProductRemoteDataSource", "cheagou aui")

            val snapshot = firestore.collection("products")
                .whereEqualTo("storeId", storeId)
                .get()
                .await()

            Log.d("ProductRemoteDataSource", "cheagou aqui 1")
            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)
            }

            Log.d("ProductRemoteDataSource", "cheagou aqui 2")

            Result.success(products)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }


}
