package com.example.produtosdelimpeza.navigation.route

enum class CustomerScreen(val route: String, val showBottomBar: Boolean = false){
    ABOUT("about"),
    MANAGEMENT_NOTIFICATION("management_notification"),
    NOTIFICATIONS("notifications"),
    CUSTOMER_STORE_PROFILE("customer_store_profile"),
    CART("cart"),
    CUSTOMER_ORDER_DETAIL("customer_order_detail"),
    CUSTOMER_ORDER_LIST("customer_order_list"),
    HIGHLIGHTS("highlights"),
    CUSTOMER_EDIT_PROFILE("edit_customer_profile"),
    CUSTOMER_PAYMENT_METHODS("payment_methods"),
    CUSTOMER_COUPON("coupon"),
    CUSTOMER_ADDRESS("customer_address"),
    HELP("help"),
    SELLER_ENTRY_POINT("seller_entry_point"),
    ENTER_INVITE_KEY("enter_invite_key"),
    SIGNUP_STORE("signup_store"),



    // TELAS DAS ROTAS
    CUSTOMER_HOME("customer_home",true),
    CUSTOMER_PROFILE("customer_profile",true),
    CUSTOMER_SEARCH("customer_search",true),
    CUSTOMER_PRODUCTS("customer_products",true)
}