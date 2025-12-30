package com.example.produtosdelimpeza.compose.seller.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.compose.customer.order.OrderStatus


data class Order(
    val id: String,
    val customerName: String,
    val totalValue: String,
    val status: OrderStatus,
    val time: String,
    val waitTime: String,
    val itemsSummary: String
)

// --- Componentes Principal ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreOrderScreen(
    onNavigateToStoreOrderDetailScreen: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(OrderStatus.RECEIVED) }
    val focusManager = LocalFocusManager.current

    // Mock de dados
    val orders = remember {
        listOf(
            Order("#1234", "João Silva", "R$ 85,90", OrderStatus.RECEIVED, "19:00", "15 min", "2x Pizza Calabresa, 1x Coca-Cola"),
            Order("#1235", "Maria Oliveira", "R$ 42,00", OrderStatus.PREPARING, "18:45", "30 min", "1x Hamburguer Gourmet"),
            Order("#1236", "Carlos Souza", "R$ 120,50", OrderStatus.DELIVERED, "18:20", "Ontem", "3x Sushi Combo"),
        )
    }

    Scaffold(
        topBar = {
           Row(
               modifier = Modifier.fillMaxWidth().statusBarsPadding(),
               horizontalArrangement = Arrangement.Center
           ) {
               Text(
                   text = "Pedidos",
                   modifier = Modifier.padding(16.dp),
                   style = MaterialTheme.typography.titleLarge
               )
           }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    SearchBarAnimated(
                        text = searchText,
                        onTextChange = { searchText = it },
                        isFocused = isSearchFocused,
                        onFocusChanged = { isSearchFocused = it },
                        onCancel = {
                            isSearchFocused = false
                            searchText = ""
                            focusManager.clearFocus()
                        }
                    )
                    FilterRow(selectedFilter) { selectedFilter = it }
                }
            }
            items(orders) { order ->
                OrderCard(order = order, onNavigateToStoreOrderDetailScreen = onNavigateToStoreOrderDetailScreen)
            }
        }
    }
}

@Composable
fun SearchBarAnimated(
    text: String,
    onTextChange: (String) -> Unit,
    isFocused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it.isFocused) },
            placeholder = { Text("Buscar pedido ou cliente") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.background
            )
        )

        //ANIMAÇÃO QUE ENCURTA O A BARRA DE PESQUISAR PARA APARECER O BOTÃO *CANCELAR*
        /*AnimatedVisibility(
            visible = isFocused,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Cancelar")
            }
        }*/
    }
}


@Composable
fun FilterRow(selectedFilter: OrderStatus, onFilterSelected: (OrderStatus) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        OrderStatus.entries.forEach { status ->
            FilterChip(
                selected = selectedFilter == status,
                onClick = { onFilterSelected(status) },
                label = { Text(status.label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}


@Composable
fun OrderCard(order: Order, onNavigateToStoreOrderDetailScreen: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToStoreOrderDetailScreen() },
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = order.customerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Pedido ${order.id}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Text(
                    text = order.totalValue,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status Tag
            Surface(
                color = order.status.color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = order.status.label.uppercase(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = order.status.color,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = order.itemsSummary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Hoje, ${order.time}", style = MaterialTheme.typography.bodySmall)
                Text(text = " • ", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Aguardando há ${order.waitTime}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = if (order.status == OrderStatus.RECEIVED) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersListScreenPreview() {
    StoreOrderScreen()
}
