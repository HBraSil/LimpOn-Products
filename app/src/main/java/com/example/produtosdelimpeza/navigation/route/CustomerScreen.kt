package com.example.produtosdelimpeza.navigation.route

enum class CustomerScreen(val route: String){
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



    // TELAS DAS ROTAS DA USER_MAIN ROUTE
    CUSTOMER_HOME("customer_home"),
    CUSTOMER_PROFILE("customer_profile"),
    CUSTOMER_SEARCH("customer_search"),
    CUSTOMER_PRODUCTS("customer_products")
}