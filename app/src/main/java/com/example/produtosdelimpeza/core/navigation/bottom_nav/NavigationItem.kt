package com.example.produtosdelimpeza.core.navigation.bottom_nav

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: Int,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val router: String
)