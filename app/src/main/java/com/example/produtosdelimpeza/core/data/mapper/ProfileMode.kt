package com.example.produtosdelimpeza.core.data.mapper

import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.commons.ProfileModeKey


fun ProfileModeKey.toProfileMode(): ProfileMode =
    when (this) {
        ProfileModeKey.LOGGED_OUT -> ProfileMode.LoggedOut
        ProfileModeKey.CUSTOMER -> ProfileMode.LoggedIn.Customer
        ProfileModeKey.STORE -> ProfileMode.LoggedIn.Store
    }

fun ProfileMode.toProfileModeKey(): ProfileModeKey =
    when (this) {
        ProfileMode.LoggedOut -> ProfileModeKey.LOGGED_OUT
        ProfileMode.LoggedIn.Customer -> ProfileModeKey.CUSTOMER
        ProfileMode.LoggedIn.Store -> ProfileModeKey.STORE
    }
