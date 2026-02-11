package com.example.produtosdelimpeza.core.navigation

import SellerEntryPointScreen
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.example.produtosdelimpeza.core.navigation.route.NavGraph
import com.example.produtosdelimpeza.customer.order.OrderListScreen
import com.example.produtosdelimpeza.customer.order.OrderDetailsScreen
import com.example.produtosdelimpeza.customer.about.AboutScreen
import com.example.produtosdelimpeza.customer.highlights.HighlightsScreen
import com.example.produtosdelimpeza.customer.home.presentation.HomeScreen
import com.example.produtosdelimpeza.core.auth.presentation.login.LoginScreen
import com.example.produtosdelimpeza.customer.notifications.ManagementNotificationScreen
import com.example.produtosdelimpeza.customer.search.presentation.SearchScreen
import com.example.produtosdelimpeza.customer.catalog.SellerProductsScreen
import com.example.produtosdelimpeza.customer.catalog.profile.StoreProfileScreen
import com.example.produtosdelimpeza.core.auth.presentation.signup.SignupScreen
import com.example.produtosdelimpeza.store.onboarding.EnterInviteKeyScreen
import com.example.produtosdelimpeza.store.onboarding.SignupStoreScreen
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation.CouponRegistrationScreen
import com.example.produtosdelimpeza.store.managment.StoreManagementScreen
import com.example.produtosdelimpeza.store.managment.coupon_tab.CouponDetailScreen
import com.example.produtosdelimpeza.store.managment.product_tab.ProductDetailScreen
import com.example.produtosdelimpeza.store.managment.promotion_tab.PromotionDetailsScreen
import com.example.produtosdelimpeza.store.order.StoreOrderScreen
import com.example.produtosdelimpeza.store.profile.StoreProfileScreen
import com.example.produtosdelimpeza.store.order.StoreOrderDetailsScreen
import com.example.produtosdelimpeza.store.profile.edit_profile.EditProfileScreen
import com.example.produtosdelimpeza.store.profile.logistic.OperationScreen
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation.PromotionRegistrationScreen
import com.example.produtosdelimpeza.core.navigation.bottom_nav.CustomerBottomNavConfig
import com.example.produtosdelimpeza.core.navigation.bottom_nav.StoreBottomNavConfig
import com.example.produtosdelimpeza.core.navigation.route.AuthScreen
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen
import com.example.produtosdelimpeza.core.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.core.presentation.DeepLinkViewModel
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import com.example.produtosdelimpeza.customer.cart.presentation.CartScreen
import com.example.produtosdelimpeza.customer.cart.presentation.CartViewModel
import com.example.produtosdelimpeza.customer.profile.presentation.ProfileScreen
import com.example.produtosdelimpeza.customer.profile.presentation.address.AddressesScreen
import com.example.produtosdelimpeza.customer.profile.presentation.coupons.CouponsScreen
import com.example.produtosdelimpeza.customer.profile.presentation.header_profile_screen.EditUserProfileScreen
import com.example.produtosdelimpeza.customer.profile.presentation.help.HelpScreen
import com.example.produtosdelimpeza.customer.profile.presentation.payment_methods.PaymentMethodsScreen
import com.example.produtosdelimpeza.store.dashboard.presentation.COUPON
import com.example.produtosdelimpeza.store.dashboard.presentation.DashboardScreen
import com.example.produtosdelimpeza.store.dashboard.presentation.PRODUCT
import com.example.produtosdelimpeza.store.dashboard.presentation.PROMOTION
import com.example.produtosdelimpeza.store.dashboard.StoreAnalyticsScreen
import com.example.produtosdelimpeza.store.dashboard.product_registration.presentation.ProductRegistrationScreen
import com.example.produtosdelimpeza.store.onboarding.AutonomousRequestScreen
import com.example.produtosdelimpeza.store.onboarding.PartnerRequestEntryScreen
import com.example.produtosdelimpeza.store.onboarding.SellerType
import com.example.produtosdelimpeza.store.onboarding.StoreRequestScreen


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
            customerMainGraph(navController, cartViewModel, navigationLastUserModeViewModel)
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
                        PRODUCT -> navController.navigate(StoreScreen.PRODUCT_REGISTRATION.route)
                        PROMOTION -> {navController.navigate(StoreScreen.PROMOTION_REGISTRATION.route)}
                        COUPON -> {navController.navigate(StoreScreen.COUPUN_REGISTRATION.route)}
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

        composable(StoreScreen.COUPUN_REGISTRATION.route) {
            CouponRegistrationScreen(
                onBackNavigation = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate(NavGraph.AUTH.route)
                }
            )
        }

        composable(StoreScreen.PROMOTION_REGISTRATION.route) {
            PromotionRegistrationScreen(
                onBackNavigation = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate(NavGraph.AUTH.route)
                }
            )
        }

        composable(StoreScreen.PRODUCT_REGISTRATION.route) {
            ProductRegistrationScreen(
                onBackNavigation = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate(NavGraph.AUTH.route)
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

        composable(
            route = StoreScreen.PARTNER_REQUEST.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) }
        ) {
            PartnerRequestEntryScreen(
                onContinue = { type ->
                    navController.navigate(
                        if (type == SellerType.AUTONOMOUS) StoreScreen.AUTONOMOUS_REQUEST.route else StoreScreen.STORE_REQUEST.route
                    )
                }
            )
        }

        composable(
            route = StoreScreen.AUTONOMOUS_REQUEST.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) }
        ) {
            AutonomousRequestScreen(onSubmit = { /* enviar */ })
        }

        composable(
            route = StoreScreen.STORE_REQUEST.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) }
        ) {
            StoreRequestScreen(onSubmit = {}, onBackNavigation = { navController.navigateUp() })
        }
    }
}


fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        route = NavGraph.AUTH.route,
        startDestination = AuthScreen.LOGIN.route,
    ) {
        /*composable(route = AuthScreen.INITIAL.route) {
            InitialScreen(
                onStartClick = { navController.navigate(AuthScreen.LOGIN.route) },
               //appLayoutViewModel = navigationLastUserModeViewModel
            )
        }*/

        composable(route = AuthScreen.LOGIN.route) {
            LoginScreen(
                onSignupClick = { navController.navigate(AuthScreen.SIGNUP.route) },
                onLoginClick = {
                    navController.navigate(NavGraph.USER_MAIN.route) {
                        popUpTo(AuthScreen.LOGIN.route) { inclusive = true }
                    }
                },
            )
        }

        composable(route = AuthScreen.SIGNUP.route) {
            SignupScreen(
                onBackNavigation = { navController.navigateUp() },
            )
        }
    }
}


fun NavGraphBuilder.customerMainGraph(
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
                onSellInTheApp = {
                    navController.navigate(StoreScreen.SELLER_ENTRY_POINT.route)
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
                        popUpTo(NavGraph.AUTH.route) { inclusive = true }  // limpa TODA a pilha
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

        composable(route = StoreScreen.SELLER_ENTRY_POINT.route) {
            SellerEntryPointScreen(
                onHaveInvite = { navController.navigate(StoreScreen.ENTER_INVITE_KEY.route) },
                onRequestInvite = { navController.navigate(StoreScreen.PARTNER_REQUEST.route) },
                onBackNavigation = { navController.navigateUp() }
            )
        }

        composable(route = StoreScreen.ENTER_INVITE_KEY.route) {
            EnterInviteKeyScreen(
                onBackNavigation = { navController.navigateUp() },
                onConfirm = { navController.navigate(StoreScreen.SIGNUP_STORE.route) }
            )
        }

        composable(route = StoreScreen.SIGNUP_STORE.route) {
            SignupStoreScreen(
                onBackNavigation = { navController.navigateUp() },
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