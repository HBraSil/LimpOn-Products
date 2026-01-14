package com.example.produtosdelimpeza.navigation.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Storefront
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
import com.example.produtosdelimpeza.navigation.route.StoreScreen

object StoreBottomNavConfig {
    val bottomNavigationStoreItems = listOf(
        NavigationItem(
            title = R.string.dashboard,
            iconSelected = Icons.Filled.Dashboard,
            iconUnselected = Icons.Outlined.Dashboard,
            router = StoreScreen.DASHBOARD.route
        ),
        NavigationItem(
            title = R.string.order_detail_screen,
            iconSelected = Icons.AutoMirrored.Filled.List,
            iconUnselected = Icons.AutoMirrored.Outlined.List,
            router = StoreScreen.STORE_ORDER.route
        ),
        NavigationItem(
            title = R.string.store_managment,
            iconSelected = Icons.Default.Storefront,
            iconUnselected = Icons.Outlined.Storefront,
            router = StoreScreen.STORE_MANAGEMENT.route
        ),
        NavigationItem(
            title = R.string.profile_store,
            iconSelected = Icons.Filled.Person,
            iconUnselected = Icons.Outlined.Person,
            router = StoreScreen.STORE_PROFILE.route
        )
    )




    @Composable
    fun StoreBottomNavigation(
        navController: NavHostController,
        currentRoute: String?
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(0.5f)) {
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
}