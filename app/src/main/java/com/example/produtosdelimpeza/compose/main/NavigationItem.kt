package com.example.produtosdelimpeza.compose.main

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.produtosdelimpeza.compose.Screen

data class NavigationItem(
    val title: Int,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val router: Screen
)
