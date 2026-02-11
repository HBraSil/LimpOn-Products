package com.example.produtosdelimpeza.store.onboarding.data

import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import com.example.produtosdelimpeza.core.domain.model.Store
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val session: AuthSessionProvider
) {
    fun saveStoreRemote(store: Store) {
        val uid = session.getUserId()
        firestore.collection("stores").document(uid!!).set(store)
    }
}