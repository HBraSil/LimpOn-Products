package com.example.produtosdelimpeza.navigation.route

enum class StoreScreen(val route: String, val showBottomBar: Boolean = false) {
    DASHBOARD("dashboard", true),
    STORE_ORDER("store_order", true),
    STORE_PROFILE("store_profile", true),
    STORE_MANAGEMENT("store_management", true),



    CREATE_COUPUN("create_coupon"),
    CREATE_PRODUCT("create_registration"),
    CREATE_PROMOTION("create_promotion"),
    PROMOTION_DETAIL("promotion_detail"),
    COUPON_DETAIL("coupon_detail"),
    PRODUCT_DETAIL("product_detail"),
    STORE_ORDER_DETAIL("store_order_detail"),
    ANALYTICS("analytics"),
    STORE_EDIT_PROFILE("store_edit_profile"),
    LOGISTIC("logistic")
}