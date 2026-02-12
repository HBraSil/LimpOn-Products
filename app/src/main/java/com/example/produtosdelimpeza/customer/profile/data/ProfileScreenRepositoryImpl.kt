package com.example.produtosdelimpeza.customer.profile.data

import android.provider.ContactsContract
import android.util.Log
import com.example.produtosdelimpeza.core.data.SigninWithGoogleApi
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.customer.profile.domain.ProfileScreenRepository
import com.example.produtosdelimpeza.store.dashboard.data.toDomain
import com.example.produtosdelimpeza.store.onboarding.data.StoreRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileScreenRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val remoteProfileDataSource: StoreRemoteDataSource,
    private val signInWithGoogleApi: SigninWithGoogleApi
): ProfileScreenRepository {

    override suspend fun getStores(): List<Store> {
        val result = remoteProfileDataSource.getAllStoresNames()
        return result.map { it.toDomain() }
    }


    override fun signOut() {
        try {
            firebaseAuth.signOut()
            signInWithGoogleApi.singOut()

            Log.d("ProfileScreenRepositoryImpl", "Usuário deslogado com sucesso.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ProfileScreenRepositoryImpl", "Falha ao deslogar")
        }
    }
}