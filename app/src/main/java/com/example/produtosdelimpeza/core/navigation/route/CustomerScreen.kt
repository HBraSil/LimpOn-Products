package com.example.produtosdelimpeza.core.navigation.route

enum class CustomerScreen(val route: String, val showBottomBar: Boolean = false){
    ABOUT("about"),
    MANAGEMENT_NOTIFICATION("management_notification"),
    NOTIFICATIONS("notifications"),
    CUSTOMER_STORE_PROFILE("customer_store_profile"),
    CUSTOMER_PROFESSIONAL_PROFILE("customer_professional_profile"),
    CART("cart"),
    CUSTOMER_ORDER_DETAIL("customer_order_detail"),
    CUSTOMER_ORDER_LIST("customer_order_list"),
    HIGHLIGHTS("highlights"),
    CUSTOMER_EDIT_PROFILE("edit_customer_profile"),
    CUSTOMER_PAYMENT_METHODS("payment_methods"),
    CUSTOMER_COUPON("coupon"),
    CUSTOMER_ADDRESS("customer_address"),
    CUSTOMER_ADD_NEW_ADDRESS("customer_add_new_address"),
    CUSTOMER_SET_LOCATION("customer_set_location"),
    HELP("help"),



    // TELAS DAS ROTAS
    CUSTOMER_HOME("customer_home",true),
    CUSTOMER_PROFILE("customer_profile",true),
    CUSTOMER_SEARCH("customer_search",true),
    CUSTOMER_PRODUCTS("customer_products",true)
}