package com.example.produtosdelimpeza.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.notifications.NotificationsScreen
import com.example.produtosdelimpeza.navigation.route.NavGraph
import com.example.produtosdelimpeza.compose.customer.order.OrderListScreen
import com.example.produtosdelimpeza.compose.customer.order.OrderDetailsScreen
import com.example.produtosdelimpeza.compose.customer.about.AboutScreen
import com.example.produtosdelimpeza.compose.customer.cart.CartScreen
import com.example.produtosdelimpeza.compose.customer.highlights.HighlightsScreen
import com.example.produtosdelimpeza.compose.customer.home.HomeScreen
import com.example.produtosdelimpeza.compose.auth.initial.InitialScreen
import com.example.produtosdelimpeza.compose.auth.login.LoginScreen
import com.example.produtosdelimpeza.compose.customer.notifications.ManagementNotificationScreen
import com.example.produtosdelimpeza.compose.customer.profile.ProfileScreen
import com.example.produtosdelimpeza.compose.customer.profile.address.AddressesScreen
import com.example.produtosdelimpeza.compose.customer.profile.coupons.CouponsScreen
import com.example.produtosdelimpeza.compose.customer.profile.header_profile_screen.EditUserProfileScreen
import com.example.produtosdelimpeza.compose.customer.profile.help.HelpScreen
import com.example.produtosdelimpeza.compose.customer.profile.payment_methods.PaymentMethodsScreen
import com.example.produtosdelimpeza.compose.customer.search.SearchScreen
import com.example.produtosdelimpeza.compose.customer.catalog.SellerProductsScreen
import com.example.produtosdelimpeza.compose.customer.catalog.profile.StoreProfileScreen
import com.example.produtosdelimpeza.compose.auth.signup.SignupScreen
import com.example.produtosdelimpeza.compose.seller.coupon.CreateCouponScreen
import com.example.produtosdelimpeza.compose.seller.dashboard.COUPON
import com.example.produtosdelimpeza.compose.seller.dashboard.DashboardScreen
import com.example.produtosdelimpeza.compose.seller.dashboard.PRODUCT
import com.example.produtosdelimpeza.compose.seller.dashboard.PROMOTION
import com.example.produtosdelimpeza.compose.seller.dashboard.ProductRegistrationScreen
import com.example.produtosdelimpeza.compose.seller.dashboard.StoreAnalyticsScreen
import com.example.produtosdelimpeza.compose.seller.managment.StoreManagementScreen
import com.example.produtosdelimpeza.compose.seller.managment.coupon_tab.CouponDetailScreen
import com.example.produtosdelimpeza.compose.seller.managment.product_tab.ProductDetailScreen
import com.example.produtosdelimpeza.compose.seller.managment.promotion_tab.PromotionDetailsScreen
import com.example.produtosdelimpeza.compose.seller.order.StoreOrderScreen
import com.example.produtosdelimpeza.compose.seller.profile.StoreProfileScreen
import com.example.produtosdelimpeza.compose.seller.order.StoreOrderDetailsScreen
import com.example.produtosdelimpeza.compose.seller.profile.edit_profile.EditProfileScreen
import com.example.produtosdelimpeza.compose.seller.profile.logistic.OperationScreen
import com.example.produtosdelimpeza.compose.seller.promotion.TimeLimitedPromotionScreen
import com.example.produtosdelimpeza.navigation.bottom_nav.CustomerBottomNavConfig
import com.example.produtosdelimpeza.navigation.bottom_nav.StoreBottomNavConfig
import com.example.produtosdelimpeza.navigation.route.AuthScreen
import com.example.produtosdelimpeza.navigation.route.StoreScreen
import com.example.produtosdelimpeza.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel


@Composable
fun LimpOnAppNavigation(
    startDestination: String,
    cartViewModel: CartViewModel = hiltViewModel(),
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    DeepLinkObserver(
        onEmailVerified = {
            navController.navigate(NavGraph.USER_MAIN.route)
        }
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route


            val shouldShowStoreBottomBar =
                StoreScreen.entries.firstOrNull { it.route == currentRoute }?.showBottomBar == true
            if (shouldShowStoreBottomBar) {
                StoreBottomNavConfig.StoreBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }


            val shouldShowCustomerBottomBar =
                CustomerScreen.entries.firstOrNull { it.route == currentRoute }?.showBottomBar == true
            if (shouldShowCustomerBottomBar) {
                CustomerBottomNavConfig.CustomerBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            authGraph(navController)
            userMainGraph(navController, cartViewModel, navigationLastUserModeViewModel)
            storeMainGraph(navController, navigationLastUserModeViewModel)
        }
    }
}


private fun NavGraphBuilder.storeMainGraph(navController: NavHostController, navigationLastUserModeViewModel: NavigationLastUserModeViewModel) {
    navigation(
        route = NavGraph.SELLER_MAIN.route,
        startDestination = StoreScreen.DASHBOARD.route
    ) {
        composable(route = StoreScreen.DASHBOARD.route) {
            DashboardScreen(
                navigationLastUserModeViewModel,
                onNotificationsScreenClick = {
                    navController.navigate(CustomerScreen.NOTIFICATIONS.route)
                },
                onNavigateToAnalyticsScreenClick = {
                    navController.navigate(StoreScreen.ANALYTICS.route)
                },
                onNavigateToItemFab = {
                    when(it) {
                        PRODUCT -> navController.navigate(StoreScreen.CREATE_PRODUCT.route)
                        PROMOTION -> {navController.navigate(StoreScreen.CREATE_PROMOTION.route)}
                        COUPON -> {navController.navigate(StoreScreen.CREATE_COUPUN.route)}
                    }
                }
            )
        }

        composable(route = StoreScreen.STORE_ORDER.route) {
            StoreOrderScreen(
                onNavigateToStoreOrderDetailScreen = {
                    navController.navigate(StoreScreen.STORE_ORDER_DETAIL.route)
                }
            )
        }

        composable(route = StoreScreen.STORE_ORDER_DETAIL.route) {
            StoreOrderDetailsScreen()
        }

        composable(route = StoreScreen.STORE_PROFILE.route) {
            StoreProfileScreen(
                onNavigateToOtherUser = { screen ->
                    if (screen == StoreScreen.DASHBOARD.route) {
                        navController.navigate(NavGraph.SELLER_MAIN.route) {
                            popUpTo(NavGraph.USER_MAIN.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(NavGraph.USER_MAIN.route) {
                            popUpTo(NavGraph.USER_MAIN.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onItemProfileClick = {route ->
                    navController.navigate(route)
                }
            )
        }

        composable(StoreScreen.STORE_EDIT_PROFILE.route) {
            EditProfileScreen()
        }

        composable(StoreScreen.LOGISTIC.route) {
            OperationScreen()
        }

        composable(StoreScreen.ANALYTICS.route) {
            StoreAnalyticsScreen(
                onBackNavigation = {
                    navController.navigateUp()
                },
                onPromotionActionItemClick = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(StoreScreen.CREATE_COUPUN.route) {
            CreateCouponScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(StoreScreen.CREATE_PROMOTION.route) {
            TimeLimitedPromotionScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(StoreScreen.CREATE_PRODUCT.route) {
            ProductRegistrationScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(StoreScreen.STORE_MANAGEMENT.route) {
            StoreManagementScreen(
                onNavigateToTabContentDetailScreenClick = { route ->
                    navController.navigate(route)
                },
                onNewProductClick = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(StoreScreen.PROMOTION_DETAIL.route) {
            PromotionDetailsScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(StoreScreen.COUPON_DETAIL.route) {
            CouponDetailScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(StoreScreen.PRODUCT_DETAIL.route) {
            ProductDetailScreen(
                onBackNavigation = {navController.navigateUp()}
            )
        }
    }
}


fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        route = NavGraph.AUTH.route,
        startDestination = AuthScreen.INITIAL.route,
    ) {
        composable(route = AuthScreen.INITIAL.route) {
            InitialScreen(
                onStartClick = { navController.navigate(AuthScreen.LOGIN.route) },
               //appLayoutViewModel = navigationLastUserModeViewModel
            )
        }

        composable(route = AuthScreen.LOGIN.route) {
            LoginScreen(
                onSignupClick = { navController.navigate(AuthScreen.SIGNUP.route) },
                onLoginClick = {
                    navController.navigate(NavGraph.USER_MAIN.route) {
                        popUpTo(AuthScreen.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AuthScreen.SIGNUP.route) {
            SignupScreen(
                onBackNavigation = { navController.navigateUp() },
            )
        }
    }
}


fun NavGraphBuilder.userMainGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    appLayoutViewModel: NavigationLastUserModeViewModel
) {
    navigation(
        route = NavGraph.USER_MAIN.route,
        startDestination = NavGraph.HOME.route
    ) {
        sharedGraph(navController)

        homeGraph(navController, cartViewModel)
        searchGraph(navController)
        ordersGraph(navController)
        profileGraph(navController)
    }
}

private fun NavGraphBuilder.sharedGraph(controller: NavHostController) {
    navigation(
        route = NavGraph.SHRARED_GRAPH.route,
        startDestination = CustomerScreen.CART.route
    ){
        composable(route = CustomerScreen.CART.route) {
            CartScreen(
                /*onBackNavigation = { navController.navigateUp() },
                cartViewModel = cartViewModel*/
            )
        }
    }

}


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
){
    navigation(
        route = NavGraph.HOME.route,
        startDestination = CustomerScreen.CUSTOMER_HOME.route
    ) {
        composable(route = CustomerScreen.CUSTOMER_HOME.route) {
            HomeScreen(
                cartViewModel = cartViewModel,
                navController = navController,
                onCardSellerClick = { nameSeller ->
                    navController.navigate("${CustomerScreen.CUSTOMER_PRODUCTS.route}/$nameSeller")
                },
                onSeeAllClick = {
                    navController.navigate(CustomerScreen.HIGHLIGHTS.route)
                },
                onNavigateToNotifications = {
                    navController.navigate(CustomerScreen.NOTIFICATIONS.route)
                }
            )
        }

        composable(route = "${CustomerScreen.CUSTOMER_PRODUCTS.route}/{nameSeller}") { navBackStackEntry ->
            val nameSeller = navBackStackEntry.arguments?.getString("nameSeller") ?: ""

            SellerProductsScreen(
                cartViewModel = cartViewModel,
                storeName = nameSeller,
                onBackNavigation = {
                    navController.navigateUp()
                },
                onCardStoreProfileClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_STORE_PROFILE.route)
                },
                onCartScreenClick = {
                    navController.navigate(CustomerScreen.CART.route)
                }
            )
        }

        composable(route = CustomerScreen.CUSTOMER_STORE_PROFILE.route) {
            StoreProfileScreen()
        }

        composable(route = CustomerScreen.HIGHLIGHTS.route) {
            HighlightsScreen(
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = CustomerScreen.NOTIFICATIONS.route) {
            NotificationsScreen()
        }
    }

}


fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.SEARCH.route,
        startDestination = CustomerScreen.CUSTOMER_SEARCH.route
    ){
        composable(route = CustomerScreen.CUSTOMER_SEARCH.route) {
            SearchScreen()
        }
    }
}


private fun NavGraphBuilder.ordersGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.ORDERS.route,
        startDestination = CustomerScreen.CUSTOMER_ORDER_LIST.route
    ){
        composable(route = CustomerScreen.CUSTOMER_ORDER_LIST.route) {
            OrderListScreen(
                onNavigateToOrderDetails = { navController.navigate(CustomerScreen.CUSTOMER_ORDER_DETAIL.route) }
            )
        }
        composable(route = CustomerScreen.CUSTOMER_ORDER_DETAIL.route) {
            OrderDetailsScreen(
                onBackNavigation = { navController.navigateUp() },
            )
        }
    }
}


fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.PROFILE.route,
        startDestination = CustomerScreen.CUSTOMER_PROFILE.route
    ){
        composable(route = CustomerScreen.CUSTOMER_PROFILE.route) {
            ProfileScreen(
                onNotificationsScreenClick = {
                    navController.navigate(CustomerScreen.MANAGEMENT_NOTIFICATION.route)
                },
                onEditUserProfileScreenClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_EDIT_PROFILE.route)
                },
                onPaymentMethodsScreenClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_PAYMENT_METHODS.route)
                },
                onCouponsScreenClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_COUPON.route)
                },
                onMyAddressesScreenClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_ADDRESS.route)
                },
                onAboutScreenClick = {
                    navController.navigate(CustomerScreen.ABOUT.route)
                },
                onHelpScreenClick = {
                    navController.navigate(CustomerScreen.HELP.route)
                },
                onOrderScreenClick = {
                    navController.navigate(CustomerScreen.CUSTOMER_ORDER_LIST.route)
                },
                onSwitchProfileClick = { profile ->
                    if (profile == StoreScreen.DASHBOARD.route) {
                        navController.navigate(NavGraph.SELLER_MAIN.route) {
                            popUpTo(NavGraph.USER_MAIN.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(NavGraph.USER_MAIN.route) {
                            popUpTo(NavGraph.USER_MAIN.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onSignOutClick = {
                    navController.navigate(NavGraph.AUTH.route) {
                        popUpTo(0) { inclusive = true }  // limpa TODA a pilha
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = CustomerScreen.CUSTOMER_EDIT_PROFILE.route) {
            EditUserProfileScreen()
        }

        composable(route = CustomerScreen.CUSTOMER_PAYMENT_METHODS.route) {
            PaymentMethodsScreen()
        }

        composable(route = CustomerScreen.CUSTOMER_COUPON.route) {
            CouponsScreen()
        }

        composable(route = CustomerScreen.CUSTOMER_ADDRESS.route) {
            AddressesScreen()
        }
        composable(route = CustomerScreen.MANAGEMENT_NOTIFICATION.route) {
            ManagementNotificationScreen(
                onBackNavigation = { navController.navigateUp() }
            )
        }

        composable(route = CustomerScreen.HELP.route) {
            HelpScreen(
                onBackNavigation = { navController.navigateUp() }
            )
        }

        composable(route = CustomerScreen.ABOUT.route) {
            AboutScreen(
                onBackNavigation = { navController.navigateUp() }
            )
        }
    }
}


@Composable
fun DeepLinkObserver(onEmailVerified: () -> Unit = {}) {
    val deepLinkViewModel: DeepLinkViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    // 💡 IMPORTANTE: Use o LaunchedEffect para reagir ao estado
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            deepLinkViewModel.receivedLink.collect { link ->
                Log.d("DEEP_LINK", "Observer recebeu link(fun deep) = $link")
                if (link != null) {
                    // Navega para a tela de Verificação
                    Log.d("NAV_DEBUG", "Navegando para EMAIL_VERIFIED...")
                    onEmailVerified()
                    deepLinkViewModel.consume()
                }
            }
        }
    }
}