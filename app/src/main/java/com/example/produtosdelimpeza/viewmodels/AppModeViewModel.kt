package com.example.produtosdelimpeza.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppModeViewModel @Inject constructor() : ViewModel() {

    var activeProfile: UserProfile by mutableStateOf(UserProfile.Client("Hilquias"))
        private set

    private val sellerProfiles = mutableStateListOf<UserProfile.Seller>()

    fun addSellerProfile(storeId: String, storeName: String) {
        sellerProfiles += UserProfile.Seller(storeId, storeName)
    }

    fun getSellerProfiles(): List<UserProfile.Seller> = sellerProfiles

    fun switchToClientMode() {
        activeProfile = UserProfile.Client("Hilquias")
    }

    fun switchToSellerProfile(seller: UserProfile.Seller) {
        activeProfile = seller
    }

    fun isSellerMode(): Boolean = activeProfile is UserProfile.Seller
}

