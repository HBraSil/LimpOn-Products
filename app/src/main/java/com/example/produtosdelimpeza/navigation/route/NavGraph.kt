package com.example.produtosdelimpeza.navigation.route

enum class NavGraph(val route: String) {
    AUTH("auth"),
    USER_MAIN("user_main"),
    SELLER_MAIN("seller_main"),


    HOME("nav_graph_home"),
    SEARCH("nav_graph_search"),
    ORDERS("nav_graph_orders"),
    PROFILE("nav_graph_profile"),

    SHRARED_GRAPH("shared_graph"),
}

enum class SplashRoute(val route: String) {
    SPLASH("splash"),
    SPLASH_GRAPH("splash_login"),
}