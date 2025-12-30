package com.example.produtosdelimpeza.compose.customer.order

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.ui.theme.GradientBackCardsComponents
import com.example.produtosdelimpeza.ui.theme.StarColor
import com.example.produtosdelimpeza.utils.toBrazilianCurrency
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

// --- Data model ---
data class OrderList(
    val id: Int,
    val number: String,
    val dateTime: LocalDateTime,
    val restaurantName: String,
    val rating: Double,
    val status: OrderStatusList
)

enum class OrderStatusList(val label: String) {
    PREPARING("Em preparo"),
    ON_THE_WAY("A caminho"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado")
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(onNavigateToOrderDetails: () -> Unit) {
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var sheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pedidos") },
                actions = {
                    IconButton(onClick = {
                        sheetVisible = true
                        scope.launch { sheetState.show() }
                    }) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        },
        bottomBar = {
            //MainBottomNavigation(navController = navController)
        },
    ) { contentPadding ->
        val orders = generateMockOrders(10)
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum pedido encontrado", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }


        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            items(orders.size) { index ->
                val order = orders[index]
                OrderCard(
                    order = order,
                    onClick = onNavigateToOrderDetails
                )
            }

            // optional: spacer at bottom to avoid overlaps with navigation bar / FABs
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (sheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    sheetVisible = false
                    scope.launch { sheetState.hide() }
                },
                sheetState = sheetState,
            ) {
                // drag handle custom

                FiltersBottomSheet_DoubleSliders(
                    availableStatuses = PedidoStatus.entries,
                    minPossibleValue = 0f,
                    maxPossibleValue = 200f,
                    onApply = { filters ->
                        sheetVisible = false
                        scope.launch { sheetState.hide() }
                    },
                    onDismissRequest = {
                        sheetVisible = false
                        scope.launch { sheetState.hide() }
                    }
                )
            }
        }
    }
}


// --- Helper: mock data ---
@RequiresApi(Build.VERSION_CODES.O)
fun generateMockOrders(count: Int = 8): List<OrderList> {
    val now = LocalDateTime.now()
    val names = listOf("Limpeza Rápida", "Brasa Burguer", "Cantina da Esquina", "Sushi Ya", "Café Central")
    return List(count) { i ->
        val status = when (Random.nextInt(0, 4)) {
            0 -> OrderStatusList.PREPARING
            1 -> OrderStatusList.ON_THE_WAY
            2 -> OrderStatusList.DELIVERED
            else -> OrderStatusList.CANCELLED
        }
        OrderList(
            id = 1000 + i,
            number = "#${1000 + i}",
            dateTime = now.minusHours((i * 5).toLong()).minusMinutes((i * 7).toLong()),
            restaurantName = names[i % names.size],
            rating = (4.0 + Random.nextDouble() * 1.0).let { String.format("%.1f", it).toDouble() },
            status = status
        )
    }
}

// --- UI: Order card ---
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCard(
    order: OrderList,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier
            .background(GradientBackCardsComponents)
            .padding(start = 16.dp, top = 16.dp, bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido ${order.number}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Data do pedido",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy • HH:mm")),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Status chip at the right
                StatusChip(
                    modifier = Modifier.padding(end = 16.dp),
                    order.status
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = order.restaurantName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        VerticalDivider(
                            modifier = Modifier.height(16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(3.dp))

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Avaliação",
                            modifier = Modifier.size(10.dp),
                            tint = StarColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${order.rating}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .background(
                                    brush = GradientBackCardsComponents,
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                text = "R$ ${40.0.toBrazilianCurrency()}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(6.dp),
                                color = MaterialTheme.colorScheme.background
                            )
                        }

                        Spacer(Modifier.weight(1f))
                        // subtle chevron
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Ver detalhes",
                            modifier = Modifier.size(30.dp).padding(end = 6.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(modifier: Modifier = Modifier, status: OrderStatusList) {
    val (icon, color, tonal) = when (status) {
        OrderStatusList.PREPARING -> Triple(Icons.Default.HourglassTop, MaterialTheme.colorScheme.primary, true)
        OrderStatusList.ON_THE_WAY -> Triple(Icons.Default.LocalShipping, MaterialTheme.colorScheme.secondary, true)
        OrderStatusList.DELIVERED -> Triple(Icons.Default.CheckCircle, MaterialTheme.colorScheme.tertiary, true)
        OrderStatusList.CANCELLED -> Triple(Icons.Default.Cancel, MaterialTheme.colorScheme.error, false)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = if (tonal) 2.dp else 0.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .wrapContentWidth()
            .height(34.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "${status.label} ícone",
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = status.label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}