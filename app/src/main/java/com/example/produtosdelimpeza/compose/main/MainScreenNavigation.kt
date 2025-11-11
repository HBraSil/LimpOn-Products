package com.example.produtosdelimpeza.compose.main

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.produtosdelimpeza.R
import androidx.navigation.compose.rememberNavController
import com.example.produtosdelimpeza.compose.order.OrderListScreen
import com.example.produtosdelimpeza.compose.order.OrderDetailsScreen
import com.example.produtosdelimpeza.compose.Screen
import com.example.produtosdelimpeza.compose.about.AboutScreen
import com.example.produtosdelimpeza.compose.cart.CartScreen
import com.example.produtosdelimpeza.compose.highlights.HighlightsScreen
import com.example.produtosdelimpeza.compose.home.HomeScreen
import com.example.produtosdelimpeza.compose.notifications.NotificationScreen
import com.example.produtosdelimpeza.compose.profile.ProfileScreen
import com.example.produtosdelimpeza.compose.profile.header_profile_screen.EditUserProfileScreen
import com.example.produtosdelimpeza.compose.profile.payment_methods.PaymentMethodsScreen
import com.example.produtosdelimpeza.compose.search.SearchScreen
import com.example.produtosdelimpeza.compose.seller.SellerProductsScreen
import com.example.produtosdelimpeza.compose.seller.profile.SellerProfileScreen
import com.example.produtosdelimpeza.compose.seller.SellerRegister
import com.example.produtosdelimpeza.compose.seller.profile.PaymentMethods
import com.example.produtosdelimpeza.viewmodels.CartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenNavigation() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route,
        modifier = Modifier.background(White)
    ) {
        composable(route = Screen.HOME.route) {
            HomeScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                onCardSellerClick = { nameSeller ->
                    navController.navigate("${Screen.SELLER.route}/$nameSeller")
                },
                onSeeAllClick = {
                    navController.navigate(Screen.HIGHLIGHTS.route)
                }
            )
        }

        composable(route = Screen.HIGHLIGHTS.route) {
            HighlightsScreen()
        }

        composable(route = Screen.SEARCH.route) {
            SearchScreen(navController)
        }

        composable(route = Screen.ORDER_LIST.route) {
            OrderListScreen(
                navController,
                onNavigateToOrderDetails = { navController.navigate(Screen.ORDER_DETAIL.route) }
            )
        }

        composable(route = Screen.ORDER_DETAIL.route) {
            OrderDetailsScreen(
                onBack = { navController.navigateUp() },
            )
        }

        composable(route = Screen.PROFILE.route) {
            ProfileScreen(
                navController,
                onClickNotificationsScreen = {
                    navController.navigate(Screen.NOTIFICATION.route)
                },
                onClickEditUserProfile = {
                    navController.navigate(Screen.EDIT_USER_PROFILE.route)
                },
                onClickPaymentMethods = {
                    navController.navigate(Screen.PAYMENT_METHODS.route)
                }
            )
        }
        composable(route = Screen.EDIT_USER_PROFILE.route) {
            EditUserProfileScreen()
        }
        composable(route = Screen.PRODUCT.route) {
            //Área do vendedor(navController)
        }

        composable(route = Screen.PAYMENT_METHODS.route) {
            PaymentMethodsScreen()
        }

        composable(route = "${Screen.SELLER.route}/{nameSeller}") { navBackStackEntry ->
            val nameSeller = navBackStackEntry.arguments?.getString("nameSeller") ?: ""

            SellerProductsScreen(
                cartViewModel = cartViewModel,
                nameSeller = nameSeller,
                onBackNavigation = {
                    navController.navigateUp()
                },
                onClickCardSellerProfile = {
                    navController.navigate(Screen.SELLER_PROFILE.route)
                },
                onClickCartScreen = {
                    navController.navigate(Screen.CART.route)
                }
            )
        }

        composable(route = Screen.ABOUT.route) {
            AboutScreen(
                onNavigateUpClick = { navController.navigateUp() }
            )
        }

        composable(route = Screen.SELLER_REGISTER.route) {
            SellerRegister()
        }

        composable(route = Screen.NOTIFICATION.route) {
            NotificationScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = Screen.SELLER_PROFILE.route) {
            SellerProfileScreen()
        }

/*
        composable(route = "${Screen.PRODUCT.route}/{productId}") {
            val productId = it.arguments?.getString("productId")!!
            ProductScreen(
                productId = productId,
                onBackNavigation = {
                    navController.navigateUp()
                }
            )
        }
*/


        composable(route = Screen.CART.route) {
            CartScreen(
                onBackNavigation = { navController.navigateUp() },
                cartViewModel = cartViewModel
            )
        }
    }
}


@Composable
fun MainBottomNavigation(
    navController: NavHostController,
) {
    val navigationItems = listOf(
        NavigationItem(
            title = R.string.home,
            iconSelected = Icons.Filled.Home,
            iconUnselected = Icons.Outlined.Home,
            router = Screen.HOME
        ),
        NavigationItem(
            title = R.string.search,
            iconSelected = Icons.Filled.Search,
            iconUnselected = Icons.Outlined.Search,
            router = Screen.SEARCH
        ),
        NavigationItem(
            title = R.string.order_detail,
            iconSelected = Icons.AutoMirrored.Filled.ReceiptLong,
            iconUnselected = Icons.AutoMirrored.Outlined.ReceiptLong,
            router = Screen.ORDER_LIST
        ),
        NavigationItem(
            title = R.string.profile,
            iconSelected = Icons.Filled.Person,
            iconUnselected = Icons.Outlined.Person,
            router = Screen.PROFILE
        )
    )


    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navigationItems.forEach { item ->
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