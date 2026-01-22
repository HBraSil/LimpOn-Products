package com.example.produtosdelimpeza.core.navigation.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.navigation.route.CustomerScreen

object CustomerBottomNavConfig {
    val bottomNavigationCustomerItems = listOf(
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


    @Composable
    fun CustomerBottomNavigation(
        navController: NavHostController,
        currentRoute: String?
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(0.5f)) {
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
}