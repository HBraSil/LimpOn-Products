package com.example.produtosdelimpeza.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector


data class Address(
    val id: String,
    val street: String,
    val city: String,
    val state: String,
    val label: String,
    val complement: String? = null,
    val zip: String,
    val distance: String? = null,
) {
    val cityStateZip: String
        get() = "$city - $state, $zip"

    val icon: ImageVector
        get() = when (label) {
            "Casa" -> Icons.Default.Home
            "Trabalho" -> Icons.Default.Work
            else -> Icons.Default.LocationOn
        }
}
