package com.example.produtosdelimpeza.navigation.route

enum class StoreScreen(val route: String, val showBottomBar: Boolean = false) {
    DASHBOARD("dashboard", true),
    STORE_ORDER("store_order", true),
    STORE_PROFILE("store_profile", true),
    STORE_MANAGEMENT("store_management", true),



    COUPON_DETAIL("coupon_detail"),
    PROMOTION_DETAIL("promotion_detail"),
    PRODUCT_REGISTRATION("product_registration"),
    CREATE_PROMOTION("create_promotion"),
    ANALYTICS("analytics"),
    CREATE_COUPUN("create_coupon"),
    STORE_ORDER_DETAIL("store_order_detail"),
    STORE_EDIT_PROFILE("store_edit_profile"),
    LOGISTIC("logistic")
}