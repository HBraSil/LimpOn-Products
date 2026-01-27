package com.example.produtosdelimpeza.dashboard


import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.compose.component.StoreTimeManagement


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopStatusComponent(){
    val isOnline = true
    var showStoreTimeManagementSheet by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { showStoreTimeManagementSheet = true },
        label = { Text(if (isOnline) "Aberto" else "Fechado", fontSize = 12.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.size(12.dp)
            )
        },
        border = null,
        elevation = AssistChipDefaults.assistChipElevation(
            elevation = 4.dp,
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showStoreTimeManagementSheet) {
        StoreTimeManagement(
            onDismiss = { showStoreTimeManagementSheet = false }
        )
    }
}