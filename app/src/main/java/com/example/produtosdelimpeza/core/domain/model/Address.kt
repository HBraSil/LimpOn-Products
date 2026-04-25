package com.example.produtosdelimpeza.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector


data class Address(
    val id: String = "",
    val streetNumber: String = "",
    val street: String? = null,
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",
    val addressType: AddressType? = null,
    val complement: String = "",
    val formattedAddress: String? = null,
) {
    val icon: ImageVector
        get() = when (addressType) {
            AddressType.Home -> Icons.Default.Home
            AddressType.Work -> Icons.Default.Work
            else -> Icons.Default.LocationOn
        }

    fun primaryLabel(): String {
        return listOf(
            street,
            neighborhood,
            city,
            state,
        ).firstOrNull { it?.isNotBlank() == true }.orEmpty()
    }


    fun secondaryLabel(): String {
        val primary = primaryLabel()
        val parts = mutableListOf<String>()

        // se a rua existe e não é a primary, adiciona rua (+ número)
        if (street?.isNotBlank() == true && primary != street) {
            val streetAndNumber = if (streetNumber.isNotBlank()) "$street, $streetNumber" else street
            parts.add(streetAndNumber)
        }

        // bairro (se não for primary)
        if (neighborhood.isNotBlank() && primary != neighborhood) {
            parts.add(neighborhood)
        }

        // cidade e estado (sempre juntos como "Cidade - Estado" se existirem)
        val cityState = listOfNotNull(
            city.takeIf { it.isNotBlank() },
            state.takeIf { it.isNotBlank() }
        ).joinToString(" - ")

        if (cityState.isNotBlank() && primary != city) {
            parts.add(cityState)
        } else if (cityState.isNotBlank() && primary == city) {
            // se city é primary, ainda podemos mostrar estado como secundário (ex: " - Estado")
            if (state.isNotBlank()) parts.add(state)
        }

        return parts.joinToString(", ")
    }
}

sealed class AddressType {

    abstract val label: String

    data object Home : AddressType() {
        override val label = "Casa"
    }

    data object Work : AddressType() {
        override val label = "Trabalho"
    }

    data class Other(val customLabel: String) : AddressType() {
        override val label = customLabel.ifBlank { "Outro" }
    }
}


