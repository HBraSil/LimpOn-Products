package com.example.produtosdelimpeza.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.produtosdelimpeza.navigation.route.CustomerScreen

data class NavigationItem(
    val title: Int,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val router: String
)