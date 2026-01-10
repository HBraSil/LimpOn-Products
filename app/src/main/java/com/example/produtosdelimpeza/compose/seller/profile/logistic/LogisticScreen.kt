package com.example.produtosdelimpeza.compose.seller.profile.logistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.produtosdelimpeza.compose.component.StoreTimeManagement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationScreen() {
    var deliveryEnabled by remember { mutableStateOf(true) }
    var pickupEnabled by remember { mutableStateOf(true) }
    var scheduledOrders by remember { mutableStateOf(false) }
    var showStoreTimeManagementSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Operação e Entrega", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) { Icon(Icons.AutoMirrored.Filled.ArrowBackIos, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFBFBFE)), // Fundo levemente frio/moderno
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. MODOS DE ATENDIMENTO (UX Visual com FilterChips)
            item {
                OperationCard(title = "Modos de Atendimento", icon = Icons.Default.Handshake) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ServiceChip(
                            label = "Entrega",
                            selected = deliveryEnabled,
                            onClick = { deliveryEnabled = !deliveryEnabled },
                            icon = Icons.Default.DeliveryDining
                        )
                        ServiceChip(
                            label = "Retirada",
                            selected = pickupEnabled,
                            onClick = { pickupEnabled = !pickupEnabled },
                            icon = Icons.Default.Storefront
                        )
                    }
                }
            }

            // 2. PERFORMANCE E VALORES (Campos In-place)
            item {
                OperationCard(title = "Métricas de Pedido", icon = Icons.Default.Speed, true) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Reutilizando sua lógica de edição moderna
                        EditableMetricRow(label = "Tempo de Preparo", value = "35 - 45 min", icon = Icons.Default.Timer)
                        EditableMetricRow(label = "Pedido Mínimo", value = "R$ 30,00", icon = Icons.Default.Payments)
                    }
                }
            }

            item {
                Card(
                    onClick = {showStoreTimeManagementSheet = true},
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    OperationCard(title = "Horário Semanal", icon = Icons.Default.CalendarMonth) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            DayScheduleRow("Segunda a Sexta", "18:00 - 23:00", isOpen = true)
                            DayScheduleRow("Sábado e Domingo", "11:00 - 00:00", isOpen = true)
                            DayScheduleRow("Feriados", "Fechado", isOpen = false)
                        }
                    }
                }
            }

            // 4. CONFIGURAÇÕES AVANÇADAS (Toggles Modernos)
            item {
                OperationCard(title = "Configurações Avançadas", icon = Icons.Default.SettingsSuggest) {
                    Column {
                        AdvancedToggleRow(
                            title = "Aceitar pedidos agendados",
                            subtitle = "Permite que clientes comprem para outro horário",
                            checked = scheduledOrders,
                            onCheckedChange = { scheduledOrders = it }
                        )
                        Divider(Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                        AdvancedToggleRow(
                            title = "Modo Feriado",
                            subtitle = "Pausa a loja temporariamente hoje",
                            checked = false,
                            onCheckedChange = { }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }

        if (showStoreTimeManagementSheet) {
            StoreTimeManagement(
                onDismiss = { showStoreTimeManagementSheet = false }
            )
        }
    }
}

@Composable
fun OperationCard(title: String, icon: ImageVector, showIconButton: Boolean = false, content: @Composable () -> Unit) {
    Column(Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            if (showIconButton) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        content()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceChip(label: String, selected: Boolean, onClick: () -> Unit, icon: ImageVector) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, Modifier.size(18.dp)) },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun EditableMetricRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
        Surface(Modifier.size(40.dp), shape = CircleShape, color = Color(0xFFF0F0F7)) {
            Icon(icon, null, Modifier.padding(10.dp), tint = Color.Gray)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}

@Composable
fun DayScheduleRow(days: String, hours: String, isOpen: Boolean) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(days, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(
                if (isOpen) hours else "Fechado",
                style = MaterialTheme.typography.bodySmall,
                color = if (isOpen) MaterialTheme.colorScheme.primary else Color.Red
            )
        }
        // Indicador visual de status
        Box(
            Modifier
                .size(8.dp)
                .background(if (isOpen) Color(0xFF4CAF50) else Color.Red, CircleShape)
        )
    }
}

@Composable
fun AdvancedToggleRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun EditStoreProfileModernBluePreview() {
    MaterialTheme {
        OperationScreen()
    }
}
