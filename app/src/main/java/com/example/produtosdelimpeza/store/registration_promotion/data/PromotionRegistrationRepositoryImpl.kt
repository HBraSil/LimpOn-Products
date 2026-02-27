package com.example.produtosdelimpeza.store.registration_promotion.data

import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.registration_promotion.domain.PromotionRegistrationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class PromotionRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val networkChecker: NetworkChecker,
    private val userSession: LastUserModeLocalStorage
) : PromotionRegistrationRepository {
    override suspend fun createPromotion(promotion: Promotion): FirebaseResult {
        return try {
            if (!networkChecker.isInternetAvailable()) {
                return FirebaseResult.Error.Network("No internet connection")
            }

            val storeId = userSession.storeId.first() ?: return FirebaseResult.Error.Unknown("Store ID not found")
            val docRef = firestore.collection("promotions").document()
            val id = docRef.id

            val promotionWithStoreId = promotion.copy(
                id = id,
                storeId = storeId
            )

            docRef.set(promotionWithStoreId).await()

            FirebaseResult.Success(true)
        } catch (e: IOException) {
            FirebaseResult.Error.Network(e.message ?: "Network error")
        }  catch (e: Exception) {
            FirebaseResult.Error.Unknown(e.message ?: "Unknown error")
        }
    }

}