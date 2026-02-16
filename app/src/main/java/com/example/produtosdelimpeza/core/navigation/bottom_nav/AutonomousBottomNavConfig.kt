package com.example.produtosdelimpeza.core.navigation.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
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
import com.example.produtosdelimpeza.core.navigation.route.AutonomousScreen


object AutonomousBottomNavConfig {
    val bottomNavigationAutonomousItems = listOf(
        NavigationItem(
            title = R.string.dashboard,
            iconSelected = Icons.Filled.Dashboard,
            iconUnselected = Icons.Outlined.Dashboard,
            router = AutonomousScreen.AUTONOMOUS_DASHBOARD.route
        ),
        NavigationItem(
            title = R.string.service_center,
            iconSelected = Icons.AutoMirrored.Filled.Assignment,
            iconUnselected = Icons.AutoMirrored.Outlined.Assignment,
            router = AutonomousScreen.AUTONOMOUS_CENTER_SETTINGS.route
        ),
        NavigationItem(
            title = R.string.autonomous_profile,
            iconSelected = Icons.Filled.Person,
            iconUnselected = Icons.Outlined.Person,
            router = AutonomousScreen.AUTONOMOUS_PROFILE.route
        )
    )




    @Composable
    fun AutonomousBottomNavigation(
        navController: NavHostController,
        currentRoute: String?
    ) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(0.5f)) {
            bottomNavigationAutonomousItems.forEach { item ->
                NavigationBarItem(
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