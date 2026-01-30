package com.example.produtosdelimpeza.core.domain.model
sealed class ProfileMode {
    object LoggedOut : ProfileMode()

    sealed class LoggedIn : ProfileMode() {
        object Customer : LoggedIn()
        object Store : LoggedIn()
    }
}

enum class ProfileModeKey {
    LOGGED_OUT,
    CUSTOMER,
    STORE,
}
