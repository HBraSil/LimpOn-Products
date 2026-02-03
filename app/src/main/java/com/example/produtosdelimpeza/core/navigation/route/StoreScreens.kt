package com.example.produtosdelimpeza.core.navigation.route

enum class StoreScreen(val route: String, val showBottomBar: Boolean = false) {

    SELLER_ENTRY_POINT("seller_entry_point"),
    ENTER_INVITE_KEY("enter_invite_key"),
    SIGNUP_STORE("signup_store"),
    COUPUN_REGISTRATION("coupon_registration"),
    PRODUCT_REGISTRATION("product_registration"),
    PROMOTION_REGISTRATION("promotion_registration"),
    PROMOTION_DETAIL("promotion_detail"),
    COUPON_DETAIL("coupon_detail"),
    PRODUCT_DETAIL("product_detail"),
    STORE_ORDER_DETAIL("store_order_detail"),
    ANALYTICS("analytics"),
    STORE_EDIT_PROFILE("store_edit_profile"),
    LOGISTIC("logistic"),
    AUTONOMOUS_REQUEST("autonomous_request"),
    PARTNER_REQUEST("partner_request"),
    STORE_REQUEST("store_request"),



    DASHBOARD("dashboard", true),
    STORE_ORDER("store_order", true),
    STORE_PROFILE("store_profile", true),
    STORE_MANAGEMENT("store_management", true)
}