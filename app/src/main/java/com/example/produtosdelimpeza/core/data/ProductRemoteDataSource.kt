package com.example.produtosdelimpeza.core.data

import android.util.Log
import com.example.produtosdelimpeza.core.domain.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ProductRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun fetchAllProductsRemote(storeId: String): Result<List<Product>> {
        return try {

            val snapshot = firestore.collection("products")
                .whereEqualTo("storeId", storeId)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(
                    id = doc.id
                )
            }

            Log.d("ProductRemoteDataSource", "storeId recebido: $storeId")
            products.forEach {
                Log.d("ProductRemoteDataSource", "Product: $it")
            }

            Result.success(products)
        } catch (e: FirebaseFirestoreException) {
            Log.e("ProductRemoteDataSource", "Erro no Firestore ao buscar produtos", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("ProductRemoteDataSource", "Erro inesperado ao buscar produtos", e)
            Result.failure(e)
        }
    }


}
