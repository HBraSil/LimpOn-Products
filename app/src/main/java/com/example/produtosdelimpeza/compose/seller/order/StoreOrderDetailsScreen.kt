package com.example.produtosdelimpeza.compose.seller.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import com.example.produtosdelimpeza.compose.customer.order.OrderStatus


fun nextStatus(status: OrderStatus): OrderStatus? = when (status) {
    OrderStatus.RECEIVED -> OrderStatus.PREPARING
    OrderStatus.PREPARING -> OrderStatus.SHIPPED
    OrderStatus.SHIPPED -> OrderStatus.DELIVERED
    else  -> null
}

fun previousStatus(status: OrderStatus): OrderStatus? = when (status) {
    OrderStatus.PREPARING -> OrderStatus.RECEIVED
    OrderStatus.SHIPPED -> OrderStatus.PREPARING
    else -> null
}

data class OrderItem(val qty: Int, val name: String, val obs: String? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreOrderDetailsScreen() {
    // Estado do status do pedido
    var currentStatus by remember { mutableStateOf(OrderStatus.PREPARING) }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showCancelSheet by remember { mutableStateOf(false) }
    var orderStatus by remember { mutableStateOf(OrderStatus.PREPARING) }


    Scaffold(
        topBar = {
            OrderHeader(orderId = "#1234", status = currentStatus, onBack = {})
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OrderStatusUpdateSection(
                    status = orderStatus,
                    onStatusChange = { newStatus ->
                        orderStatus = newStatus
                    }
                )
            }

            item { LogisticsSection(isDelivery = true, customerName = "João Silva") }

            item { Text("Itens do Pedido", style = MaterialTheme.typography.titleMedium) }
            items(sampleItems) { item -> OrderItemRow(item) }

            item { FinancialSummary() }

            item { OrderTimeline() }
        }

        if (showStatusDialog) {
            StatusUpdateDialog(
                currentStatus = currentStatus,
                onStatusSelected = {
                    currentStatus = it
                    showStatusDialog = false
                },
                onDismiss = { showStatusDialog = false }
            )
        }

        if (showCancelSheet) {
            CancelOrderSheet(onDismiss = { showCancelSheet = false })
        }
    }
}



@Composable
fun OrderStatusUpdateSection(
    status: OrderStatus,
    onStatusChange: (OrderStatus) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var pendingStatus by remember { mutableStateOf<OrderStatus?>(null) }

    val next = nextStatus(status)
    val previous = previousStatus(status)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Status do Pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = status.color,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    status.label,
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Avançar status
            next?.let {
                Button(
                    onClick = {
                        pendingStatus = it
                        showConfirmDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Avançar para: ${it.label}")
                }
            }

            // Voltar status (ação secundária)
            previous?.let {
                OutlinedButton(
                    onClick = {
                        pendingStatus = it
                        showConfirmDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Voltar para: ${it.label}")
                }
            }
        }
    }

    if (showConfirmDialog && pendingStatus != null) {
        ConfirmStatusChangeDialog(
            current = status,
            target = pendingStatus!!,
            onConfirm = {
                onStatusChange(pendingStatus!!)
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
                pendingStatus = null
            }
        )
    }
}


@Composable
fun ConfirmStatusChangeDialog(
    current: OrderStatus,
    target: OrderStatus,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Confirmar alteração de status") },
        text = {
            Text(
                "Você está prestes a alterar o status do pedido de \"${current.label}\" para \"${target.label}\".\n\nDeseja continuar?"
            )
        }
    )
}

@Composable
fun ActionCard(
    status: OrderStatus,
    onNextStep: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Botão de Fluxo Rápido (Próximo Passo)
            status.let { actionLabel ->
                Button(
                    onClick = onNextStep,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when(status) {
                            OrderStatus.RECEIVED -> Color(0xFF4CAF50)
                            OrderStatus.PREPARING -> Color(0xFF2196F3)
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                ) {
                    Text(actionLabel.name, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
            }


            TextButton(
                onClick = onCancelClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Problemas com o pedido?", color = Color.Red, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun StatusUpdateDialog(
    currentStatus: OrderStatus,
    onStatusSelected: (OrderStatus) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Alterar Status para:", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                OrderStatus.entries.forEach { status ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onStatusSelected(status) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (status == currentStatus),
                            onClick = { onStatusSelected(status) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier
                                .size(12.dp)
                                .background(status.color, CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(status.label)
                    }
                }
            }
        }
    }
}

// --- (Componentes de Apoio Mantidos para integridade do código) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHeader(orderId: String, status: OrderStatus, onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pedido $orderId", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text("12 min decorridos", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
        actions = {
            Surface(color = status.color, shape = RoundedCornerShape(16.dp)) {
                Text(status.label, Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun LogisticsSection(isDelivery: Boolean, customerName: String) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if (isDelivery) Icons.Default.DeliveryDining else Icons.Default.Storefront, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(if (isDelivery) "Entrega Própria" else "Retirada no Local", fontWeight = FontWeight.Bold)
            }
            Text("Av. Paulista, 1000 - Bela Vista, SP", style = MaterialTheme.typography.bodyMedium)
            Divider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(customerName, fontWeight = FontWeight.SemiBold)
                Row {
                    IconButton(onClick = {}) { Icon(Icons.Default.Chat, null, tint = MaterialTheme.colorScheme.primary) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.primary) }
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItem) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp).background(Color.White, RoundedCornerShape(8.dp)).padding(12.dp)) {
        Row {
            Text("${item.qty}x", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(12.dp))
            Text(item.name, style = MaterialTheme.typography.titleMedium)
        }
        item.obs?.let {
            Surface(color = Color(0xFFFFF9C4), shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(it, Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun FinancialSummary() {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total")
                Text("R$ 52,00", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                Text("Pago pelo App - Cartão", Modifier.padding(12.dp), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun OrderTimeline() {
    Column(Modifier.padding(bottom = 24.dp)) {
        Text("Histórico", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
        TimelineItem("Realizado", "18:00")
        TimelineItem("Preparando", "18:05")
    }
}

@Composable
fun TimelineItem(text: String, time: String) {
    Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.weight(1f))
        Text(time, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelOrderSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp).fillMaxWidth()) {
            Text("Motivo do Cancelamento", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Confirmar Cancelamento")
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

private val sampleItems = listOf(
    OrderItem(2, "Hambúrguer Gourmet X-Bacon", "Sem cebola"),
    OrderItem(1, "Coca-Cola 350ml")
)


@Preview(showBackground = true)
@Composable
fun OrderDetailsModernPreview() {
    MaterialTheme {
        StoreOrderDetailsScreen()
    }
}
