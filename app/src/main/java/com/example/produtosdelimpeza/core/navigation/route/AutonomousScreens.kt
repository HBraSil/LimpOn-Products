package com.example.produtosdelimpeza.core.navigation.route

enum class AutonomousScreen(val route: String, val showBottomBar: Boolean = false) {

    SERVICE_SETTINGS("service_settings"),


    AUTONOMOUS_DASHBOARD("autonomous_dashboard", true),
    AUTONOMOUS_CENTER_SETTINGS("autonomous_center_settings", true),
    AUTONOMOUS_PROFILE("autonomous_profile", true),
}