package com.example.produtosdelimpeza.core.data.mapper

import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.domain.model.ProfileModeKey


fun ProfileModeKey.toProfileMode(storeId: String?): ProfileMode =
    when (this) {
        ProfileModeKey.LOGGED_OUT -> ProfileMode.LoggedOut
        ProfileModeKey.CUSTOMER -> ProfileMode.LoggedIn.CustomerSection
        ProfileModeKey.STORE -> {
            if (storeId != null) {
                ProfileMode.LoggedIn.StoreSection(storeId)
            } else {
                ProfileMode.LoggedOut
            }
        }
    }

fun ProfileMode.toProfileModeKey(): ProfileModeKey =
    when (this) {
        ProfileMode.LoggedOut -> ProfileModeKey.LOGGED_OUT
        ProfileMode.LoggedIn.CustomerSection -> ProfileModeKey.CUSTOMER
        is ProfileMode.LoggedIn.StoreSection -> ProfileModeKey.STORE
    }
