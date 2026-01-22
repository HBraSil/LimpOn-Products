package com.example.produtosdelimpeza.commons
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
