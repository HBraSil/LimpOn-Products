package com.example.produtosdelimpeza.store.dashboard.product_registration.data


import com.example.produtosdelimpeza.store.dashboard.product_registration.data.mapper.toDto
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.Product
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ProductRegistrationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ProductRegistrationRepository {
    override suspend fun registerProduct(product: Product): Result<Unit> {


        return try {
            val productDto = product.toDto()
            firestore.collection("products")
                .document(productDto.id)
                .set(productDto)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        /*return try {
            val snapshot = firestore.collection("products")
                .document(id)
                .get()
                .await()                ISSO É getProduct PELO ID

            snapshot.toObject(ProductDto::class.java)
                ?.toDomain()

        } catch (e: Exception) {
            null
        }*/
    }
}