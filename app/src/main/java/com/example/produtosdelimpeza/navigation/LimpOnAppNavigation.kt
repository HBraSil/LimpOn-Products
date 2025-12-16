package com.example.produtosdelimpeza.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.produtosdelimpeza.navigation.route.AuthScreen
import com.example.produtosdelimpeza.navigation.route.SellerScreen
import com.example.produtosdelimpeza.navigation.route.UserScreen
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel


private val bottomNavigationItems = listOf(
    NavigationItem(
        title = R.string.home,
        iconSelected = Icons.Filled.Home,
        iconUnselected = Icons.Outlined.Home,
        router = UserScreen.HOME
    ),
    NavigationItem(
        title = R.string.search,
        iconSelected = Icons.Filled.Search,
        iconUnselected = Icons.Outlined.Search,
        router = UserScreen.SEARCH
    ),
    NavigationItem(
        title = R.string.order_detail,
        iconSelected = Icons.AutoMirrored.Filled.ReceiptLong,
        iconUnselected = Icons.AutoMirrored.Outlined.ReceiptLong,
        router = UserScreen.ORDER_LIST
    ),
    NavigationItem(
        title = R.string.profile,
        iconSelected = Icons.Filled.Person,
        iconUnselected = Icons.Outlined.Person,
        router = UserScreen.PROFILE
    )
)

@Composable
fun LimpOnAppNavigation(cartViewModel: CartViewModel = hiltViewModel(), navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    DeepLinkObserver(
        onEmailVerified = {
            navController.navigate(NavGraph.USER_MAIN.route)
        }
    )

    Scaffold(
        bottomBar = {
            val showBottomBar = bottomNavigationItems.any { it.router.route == currentRoute }
            if (showBottomBar) {
                MainBottomNavigation(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavGraph.SPLASH_GRAPH.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            splashGraph(navController)
            authGraph(navController)
            userMainGraph(navController, cartViewModel)
            sellerMainGraph(navController, cartViewModel)
        }
    }

}


fun NavGraphBuilder.splashGraph(navController: NavController) {
    navigation(
        route = NavGraph.SPLASH_GRAPH.route,
        startDestination = UserScreen.SPLASH.route
    ) {
        composable(route = UserScreen.SPLASH.route) {
            SplashScreen(
                onChoiceUserAuth = { route ->
                    navController.navigate( route )
                }
            )
        }
    }
}


private fun NavGraphBuilder.sellerMainGraph(navController: NavHostController, model: CartViewModel) {
    navigation(
        route = NavGraph.SELLER_MAIN.route,
        startDestination = SellerScreen.DASHBOARD.route
    ) {
        composable(route = SellerScreen.DASHBOARD.route) {
            DashboardScreen(
                onNavigateToCustomer = {
                    navController.navigate(NavGraph.USER_MAIN.route)
                }
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


fun NavGraphBuilder.userMainGraph(navController: NavHostController, cartViewModel: CartViewModel) {
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
        startDestination = UserScreen.CART.route
    ){
        composable(route = UserScreen.CART.route) {
            CartScreen(
                /*onBackNavigation = { navController.navigateUp() },
                cartViewModel = cartViewModel*/
            )
        }
    }

}


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel
){

    navigation(
        route = NavGraph.HOME.route,
        startDestination = UserScreen.HOME.route
    ) {
        composable(route = UserScreen.HOME.route) {
            HomeScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                onCardSellerClick = { nameSeller ->
                    navController.navigate("${UserScreen.SELLER.route}/$nameSeller")
                },
                onSeeAllClick = {
                    navController.navigate(UserScreen.HIGHLIGHTS.route)
                }
            )
        }

        composable(route = "${UserScreen.SELLER.route}/{nameSeller}") { navBackStackEntry ->
            val nameSeller = navBackStackEntry.arguments?.getString("nameSeller") ?: ""

            SellerProductsScreen(
                cartViewModel = cartViewModel,
                nameSeller = nameSeller,
                onBackNavigation = {
                    navController.navigateUp()
                },
                onClickCardSellerProfile = {
                    navController.navigate(UserScreen.SELLER_PROFILE.route)
                },
                onClickCartScreen = {
                    navController.navigate(UserScreen.CART.route)
                },

                )
        }

        composable(route = UserScreen.SELLER_PROFILE.route) {
            SellerProfileScreen()
        }

        composable(route = UserScreen.HIGHLIGHTS.route) {
            HighlightsScreen()
        }
    }

}


fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.SEARCH.route,
        startDestination = UserScreen.SEARCH.route
    ){
        composable(route = UserScreen.SEARCH.route) {
            SearchScreen(navController)
        }
    }
}


private fun NavGraphBuilder.ordersGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.ORDERS.route,
        startDestination = UserScreen.ORDER_LIST.route
    ){
        composable(route = UserScreen.ORDER_LIST.route) {
            OrderListScreen(
                navController,
                onNavigateToOrderDetails = { navController.navigate(UserScreen.ORDER_DETAIL.route) }
            )
        }
        composable(route = UserScreen.ORDER_DETAIL.route) {
            OrderDetailsScreen(
                onBack = { navController.navigateUp() },
            )
        }
    }
}


fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation(
        route = NavGraph.PROFILE.route,
        startDestination = UserScreen.PROFILE.route
    ){
        composable(route = UserScreen.PROFILE.route) {
            ProfileScreen(
                onClickNotificationsScreen = {
                    navController.navigate(UserScreen.NOTIFICATION.route)
                },
                onClickEditUserProfileScreen = {
                    navController.navigate(UserScreen.EDIT_USER_PROFILE.route)
                },
                onClickPaymentMethodsScreen = {
                    navController.navigate(UserScreen.PAYMENT_METHODS.route)
                },
                onClickCouponsScreen = {
                    navController.navigate(UserScreen.COUPON.route)
                },
                onClickMyAddressesScreen = {
                    navController.navigate(UserScreen.ADDRESS.route)
                },
                onClickAboutScreen = {
                    navController.navigate(UserScreen.ABOUT.route)
                },
                onClickHelpScreen = {
                    navController.navigate(UserScreen.HELP.route)
                },
                onCLickOrderScreen = {
                    navController.navigate(UserScreen.ORDER_LIST.route)
                },
                onSwitchProfileClick = { profile ->
                    if (profile == ProfileMode.STORE) {
                        navController.navigate(NavGraph.SELLER_MAIN.route)
                    } else {
                        navController.navigate(NavGraph.USER_MAIN.route)
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

        composable(route = UserScreen.EDIT_USER_PROFILE.route) {
            EditUserProfileScreen()
        }

        composable(route = UserScreen.PAYMENT_METHODS.route) {
            PaymentMethodsScreen()
        }

        composable(route = UserScreen.COUPON.route) {
            CouponsScreen()
        }

        composable(route = UserScreen.ADDRESS.route) {
            AddressesScreen()
        }
        composable(route = UserScreen.NOTIFICATION.route) {
            NotificationScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = UserScreen.HELP.route) {
            HelpScreen(
                onBack = { navController.navigateUp() }
            )
        }

        composable(route = UserScreen.ABOUT.route) {
            AboutScreen(
                onNavigateUpClick = { navController.navigateUp() }
            )
        }
    }
}




@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomNavigationItems.forEach { item ->
            NavigationBarItem(
                //modifier = Modifier.background(if (currentRoute == item.router.route) DarkGray else White),
                selected = currentRoute == item.router.route,
                onClick = {
                    if (currentRoute != item.router.route) {
                        navController.navigate(item.router.route) {
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
                        imageVector = if (currentRoute == item.router.route) item.iconSelected else item.iconUnselected,
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