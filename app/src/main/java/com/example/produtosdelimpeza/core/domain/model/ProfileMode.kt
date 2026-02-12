package com.example.produtosdelimpeza.core.domain.model
sealed class ProfileMode {
    object LoggedOut : ProfileMode()

    sealed class LoggedIn : ProfileMode() {
        object CustomerSection : LoggedIn()
        data class StoreSection(
            val storeId: String
        ) : LoggedIn()
    }
}

enum class ProfileModeKey {
    LOGGED_OUT,
    CUSTOMER,
    STORE,
}
