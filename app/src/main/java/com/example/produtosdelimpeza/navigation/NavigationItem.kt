package com.example.produtosdelimpeza.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.produtosdelimpeza.navigation.route.UserScreen

data class NavigationItem(
    val title: Int,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val router: UserScreen
)