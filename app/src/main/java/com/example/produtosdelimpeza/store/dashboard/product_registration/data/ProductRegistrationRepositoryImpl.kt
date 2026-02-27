package com.example.produtosdelimpeza.store.dashboard.product_registration.data

import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ProductRegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class ProductRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val networkChecker: NetworkChecker,
    private val userSession: LastUserModeLocalStorage
): ProductRegistrationRepository {
    override suspend fun registerProduct(product: Product): FirebaseResult {
        return try {
            if (!networkChecker.isInternetAvailable()) {
                return FirebaseResult.Error.Network("No internet connection")
            }
            val storeId = userSession.storeId.first() ?: return FirebaseResult.Error.Unknown("Store ID not found")

            val docRef = firestore.collection("products").document()
            val id = docRef.id

            val productWithStoreId = product.copy(
                id = id,
                storeId = storeId
            )

            docRef.set(productWithStoreId).await()

            FirebaseResult.Success(true)
        } catch (e: IOException) {
            FirebaseResult.Error.Network(e.message ?: "Network error")
        }  catch (e: Exception) {
            FirebaseResult.Error.Unknown(e.message ?: "Unknown error")
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }


}