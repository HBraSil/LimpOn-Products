package com.example.produtosdelimpeza.core.domain.model
sealed class ProfileMode {
    object LoggedOut : ProfileMode()

    sealed class LoggedIn : ProfileMode() {
        object CustomerSection : LoggedIn()
        object StoreSection : LoggedIn()
    }
}

enum class ProfileModeKey {
    LOGGED_OUT,
    CUSTOMER,
    STORE,
}
