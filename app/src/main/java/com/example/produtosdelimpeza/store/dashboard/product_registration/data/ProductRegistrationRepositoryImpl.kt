package com.example.produtosdelimpeza.store.dashboard.product_registration.data

import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ProductRegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class ProductRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val networkChecker: NetworkChecker
): ProductRegistrationRepository {
    override suspend fun registerProduct(product: Product): AppResult<Boolean> {
        return try {
            if (!networkChecker.isInternetAvailable()) {
                return AppResult.Error.Network
            }
            val userUid = firebaseAuth.currentUser?.uid ?: return AppResult.Error.SessionExpired

            firestore
                .collection("users")
                .document(userUid)
                .collection("products")
                .document(product.id)
                .set(product)
                .await()

            AppResult.Success(true)
        } catch (e: IOException) {
            AppResult.Error.Network
        }  catch (e: Exception) {
            AppResult.Error.Unknown(e.message)
        }
    }
}