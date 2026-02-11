package com.example.produtosdelimpeza.core.data.mapper

import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.domain.model.ProfileModeKey


fun ProfileModeKey.toProfileMode(): ProfileMode =
    when (this) {
        ProfileModeKey.LOGGED_OUT -> ProfileMode.LoggedOut
        ProfileModeKey.CUSTOMER -> ProfileMode.LoggedIn.CustomerSection
        ProfileModeKey.STORE -> ProfileMode.LoggedIn.StoreSection
    }

fun ProfileMode.toProfileModeKey(): ProfileModeKey =
    when (this) {
        ProfileMode.LoggedOut -> ProfileModeKey.LOGGED_OUT
        ProfileMode.LoggedIn.CustomerSection -> ProfileModeKey.CUSTOMER
        ProfileMode.LoggedIn.StoreSection -> ProfileModeKey.STORE
    }
