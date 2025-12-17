package com.example.produtosdelimpeza.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
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
import com.example.produtosdelimpeza.R
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.navigation.route.NavGraph
import com.example.produtosdelimpeza.compose.user.order.OrderListScreen
import com.example.produtosdelimpeza.compose.user.order.OrderDetailsScreen
import com.example.produtosdelimpeza.compose.user.about.AboutScreen
import com.example.produtosdelimpeza.compose.user.cart.CartScreen
import com.example.produtosdelimpeza.compose.user.highlights.HighlightsScreen
import com.example.produtosdelimpeza.compose.user.home.HomeScreen
import com.example.produtosdelimpeza.compose.user.initial.InitialScreen
import com.example.produtosdelimpeza.compose.auth.login.LoginScreen
import com.example.produtosdelimpeza.compose.user.notifications.NotificationScreen
import com.example.produtosdelimpeza.compose.user.profile.ProfileScreen
import com.example.produtosdelimpeza.compose.user.profile.address.AddressesScreen
import com.example.produtosdelimpeza.compose.user.profile.coupons.CouponsScreen
import com.example.produtosdelimpeza.compose.user.profile.header_profile_screen.EditUserProfileScreen
import com.example.produtosdelimpeza.compose.user.profile.help.HelpScreen
import com.example.produtosdelimpeza.compose.user.profile.payment_methods.PaymentMethodsScreen
import com.example.produtosdelimpeza.compose.user.search.SearchScreen
import com.example.produtosdelimpeza.compose.user.catalog.SellerProductsScreen
import com.example.produtosdelimpeza.compose.user.catalog.profile.SellerProfileScreen
import com.example.produtosdelimpeza.compose.auth.signup.SignupScreen
import com.example.produtosdelimpeza.compose.auth.splash.SplashScreen
import com.example.produtosdelimpeza.compose.seller.dashboard.DashboardScreen
import com.example.produtosdelimpeza.compose.seller.order.StoreOrderScreen
import com.example.produtosdelimpeza.compose.seller.profile.StoreProfileScreen
import com.example.produtosdelimpeza.navigation.route.AuthScreen
import com.example.produtosdelimpeza.navigation.route.StoreScreen
import com.example.produtosdelimpeza.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.navigation.route.SplashRoute
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel


private val bottomNavigationCustomerItems = listOf(
    NavigationItem(
        title = R.string.home,
        iconSelected = Icons.Filled.Home,
        iconUnselected = Icons.Outlined.Home,
        router = CustomerScreen.CUSTOMER_HOME.route
    ),
    NavigationItem(
        title = R.string.search,
        iconSelected = Icons.Filled.Search,
        iconUnselected = Icons.Outlined.Search,
        router = CustomerScreen.CUSTOMER_SEARCH.route
    ),
    NavigationItem(
        title = R.string.order_detail,
        iconSelected = Icons.AutoMirrored.Filled.ReceiptLong,
        iconUnselected = Icons.AutoMirrored.Outlined.ReceiptLong,
        router = CustomerScreen.CUSTOMER_ORDER_LIST.route
    ),
    NavigationItem(
        title = R.string.profile,
        iconSelected = Icons.Filled.Person,
        iconUnselected = Icons.Outlined.Person,
        router = CustomerScreen.CUSTOMER_PROFILE.route
    )
)

private val bottomNavigationStoreItems = listOf(
    NavigationItem(
        title = R.string.dashboard,
        iconSelected = Icons.Filled.Dashboard,
        iconUnselected = Icons.Outlined.Dashboard,
        router = StoreScreen.DASHBOARD.route
    ),
    NavigationItem(
        title = R.string.customer_order_detail,
        iconSelected = Icons.AutoMirrored.Filled.List,
        iconUnselected = Icons.AutoMirrored.Outlined.List,
        router = StoreScreen.STORE_ORDER.route
    ),
    NavigationItem(
        title = R.string.customer_profile,
        iconSelected = Icons.Filled.Person,
        iconUnselected = Icons.Outlined.Person,
        router = StoreScreen.STORE_PROFILE.route
    )
)

@Composable
fun LimpOnAppNavigation(
    cartViewModel: CartViewModel = hiltViewModel(),
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val layout by navigationLastUserModeViewModel.layout.collectAsState()
    Log.d("Limp Layout", "LimpOnAppNavigation layout: $layout")

    DeepLinkObserver(
        onEmailVerified = {
            navController.navigate(NavGraph.USER_MAIN.route)
        }
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (layout == ProfileMode.CUSTOMER) {
                CustomerBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
            if (layout == ProfileMode.STORE) {
                StoreBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute.SPLASH_GRAPH.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            splashGraph(navController)
            authGraph(navController)
            userMainGraph(navController, cartViewModel, navigationLastUserModeViewModel)
            storeMainGraph(navController, navigationLastUserModeViewModel)
        }
    }

}


fun NavGraphBuilder.splashGraph(navController: NavController) {
    navigation(
        route = SplashRoute.SPLASH_GRAPH.route,
        startDestination = SplashRoute.SPLASH.route
    ) {
        composable(route = SplashRoute.SPLASH.route) {
            SplashScreen(
                onChoiceUserAuth = { route ->
                    navController.navigate( route )
                }
            )
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
                onNavigateToCustomer = {
                    navController.navigate(NavGraph.USER_MAIN.route)
                }
            )
        }
        composable(route = StoreScreen.STORE_PROFILE.route) {
            StoreProfileScreen(
                onOpenProfile = {
                    navController.navigate(NavGraph.USER_MAIN.route) {
                        popUpTo(NavGraph.SELLER_MAIN.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = StoreScreen.STORE_ORDER.route) {
            StoreOrderScreen()
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
                onChoiceClick = { navController.navigate(AuthScreen.LOGIN.route) },
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

        homeGraph(navController, cartViewModel, appLayoutViewModel)
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
    appLayoutViewModel: NavigationLastUserModeViewModel
){
    navigation(
        route = NavGraph.HOME.route,
        startDestination = CustomerScreen.CUSTOMER_HOME.route
    ) {
        composable(route = CustomerScreen.CUSTOMER_HOME.route) {
            HomeScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                appLayoutViewModel = appLayoutViewModel,
                onCardSellerClick = { nameSeller ->
                    navController.navigate("${CustomerScreen.CUSTOMER_PRODUCTS.route}/$nameSeller")
                },
                onSeeAllClick = {
                    navController.navigate(CustomerScreen.HIGHLIGHTS.route)
                }
            )
        }

        composable(route = "${CustomerScreen.CUSTOMER_PRODUCTS.route}/{nameSeller}") { navBackStackEntry ->
            val nameSeller = navBackStackEntry.arguments?.getString("nameSeller") ?: ""

            SellerProductsScreen(
                cartViewModel = cartViewModel,
                nameSeller = nameSeller,
                onBackNavigation = {
                    navController.navigateUp()
                },
                onClickCardSellerProfile = {
                    navController.navigate(CustomerScreen.CUSTOMER_STORE_PROFILE.route)
                },
                onClickCartScreen = {
                    navController.navigate(CustomerScreen.CART.route)
                },

                )
        }

        composable(route = CustomerScreen.CUSTOMER_STORE_PROFILE.route) {
            SellerProfileScreen()
        }

        composable(route = CustomerScreen.HIGHLIGHTS.route) {
            HighlightsScreen()
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
                onBack = { navController.navigateUp() },
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
                onClickNotificationsScreen = {
                    navController.navigate(CustomerScreen.NOTIFICATION.route)
                },
                onClickEditUserProfileScreen = {
                    navController.navigate(CustomerScreen.CUSTOMER_EDIT_PROFILE.route)
                },
                onClickPaymentMethodsScreen = {
                    navController.navigate(CustomerScreen.CUSTOMER_PAYMENT_METHODS.route)
                },
                onClickCouponsScreen = {
                    navController.navigate(CustomerScreen.CUSTOMER_COUPON.route)
                },
                onClickMyAddressesScreen = {
                    navController.navigate(CustomerScreen.CUSTOMER_ADDRESS.route)
                },
                onClickAboutScreen = {
                    navController.navigate(CustomerScreen.ABOUT.route)
                },
                onClickHelpScreen = {
                    navController.navigate(CustomerScreen.HELP.route)
                },
                onCLickOrderScreen = {
                    navController.navigate(CustomerScreen.CUSTOMER_ORDER_LIST.route)
                },
                onSwitchProfileClick = { profile ->
                    if (profile == ProfileMode.STORE) {
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
        composable(route = CustomerScreen.NOTIFICATION.route) {
            NotificationScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = CustomerScreen.HELP.route) {
            HelpScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable(route = CustomerScreen.ABOUT.route) {
            AboutScreen(
                onNavigateUpClick = { navController.navigateUp() }
            )
        }
    }
}




@Composable
fun CustomerBottomNavigation(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomNavigationCustomerItems.forEach { item ->
            NavigationBarItem(
                //modifier = Modifier.background(if (currentRoute == item.router.route) DarkGray else White),
                selected = currentRoute == item.router,
                onClick = {
                    if (currentRoute != item.router) {
                        navController.navigate(item.router) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.router) item.iconSelected else item.iconUnselected,
                        contentDescription = item.title.toString(),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = White)
            )
        }
    }

}


@Composable
fun StoreBottomNavigation(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        bottomNavigationStoreItems.forEach { item ->
            NavigationBarItem(
                //modifier = Modifier.background(if (currentRoute == item.router.route) DarkGray else White),
                selected = currentRoute == item.router,
                onClick = {
                    if (currentRoute != item.router) {
                        navController.navigate(item.router) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.router) item.iconSelected else item.iconUnselected,
                        contentDescription = item.title.toString(),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = White)
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