package com.example.produtosdelimpeza.compose.user.order

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.utils.toBrazilianCurrency
import java.text.SimpleDateFormat
import java.util.*

// Nota: para carregar imagens reais no app, recomendo adicionar Coil (io.coil-kt:coil-compose) e substituir
// o placeholder por AsyncImage. Aqui usamos boxes como placeholders para manter o exemplo livre de dependências.

private val Success = Color(0xFF2E7D32)
private val Muted = Color(0xFF6B7280)

enum class OrderStatus(val label: String, val cor: Color, val icon: ImageVector) {
    RECEIVED("Pedido Recebido", Color(0xFFF85858), Icons.Default.Receipt),
    PREPARING("Em preparo", Color(0xFFFFB300), Icons.Default.HourglassTop),
    ON_THE_WAY("A caminho", Color(0xFF43A047), Icons.Default.LocalShipping),
    DELIVERED("Entregue",  Color(0xFF26A69A), Icons.Default.CheckCircle)
}


data class ProcessStep(
    val status: OrderStatus,
    val isConcluida: Boolean,
    val descricao: String,
)

data class OrderItem(
    val id: String,
    val name: String,
    val qty: Int,
    val unitPrice: Double,
    val imageUrl: Int? = null
) {
    val totalPrice get() = qty * unitPrice
}

data class Order(
    val id: String,
    val date: Date,
    val status: List<ProcessStep>,
    val items: List<OrderItem>,
    val customerName: String,
    val address: String,
    val deliveryMethod: String,
    val sellerName: String,
    val sellerCity: String,
    val sellerRating: Double,
    val paymentMethod: String,
    val paymentStatus: String // "Pago", "Pendente"
) {
    val total get() = items.sumOf { it.totalPrice }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    onBack: () -> Unit = {},
    onRepeat: (Order) -> Unit = {},
    onRate: (Order) -> Unit = {},
    onSupport: (Order) -> Unit = {},
) {
    val order = Order(
        id = "#1234",
        date = Date(),
        status = listOf(
            ProcessStep( OrderStatus.RECEIVED, true, "Seu pedido foi recebido."),
            ProcessStep(OrderStatus.PREPARING, true, "O vendedor está preparando seu pedido."),
            ProcessStep(OrderStatus.ON_THE_WAY, false, "Seu pedido saiu para entrega."),
            ProcessStep(OrderStatus.DELIVERED, false, "Pedido entregue ao cliente."),
        ),
        items = listOf(
            OrderItem("1", "Detergente Ultra Clean 500ml", 2, 8.5),
            OrderItem("2", "Desinfetante Limpeza Total 1L", 1, 12.9),
            OrderItem("3", "Esponja Multiuso - Pacote 3", 1, 7.5)
        ),
        customerName = "João da Silva",
        address = "Rua das Flores, 123, Apto 45 • Centro — Fortaleza, CE",
        deliveryMethod = "Entrega pelo vendedor local",
        sellerName = "Limpeza Rápida",
        sellerCity = "Fortaleza",
        sellerRating = 4.8,
        paymentMethod = "Pix",
        paymentStatus = "Pago"
    )


    val scrollBehavior = enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Text(text = "Pedido ${order.id}", fontWeight = FontWeight.SemiBold)
                        val fmt = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale("pt", "BR"))
                        Text(text = fmt.format(order.date), style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Detalhes do pedido"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val buttonHeight = 42.dp
        val bottomMargin = 18.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = bottomMargin + buttonHeight + 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { TimelineSection(current = order.status, order) }
                item { OrderItemsCard(order) }
                item {
                    ActionsSection(
                        order,
                        onRepeat,
                        onRate,
                        onSupport,
                    )
                }
            }
            // 🔹 Botão sobreposto (flutuante)
            ElevatedButton(
                onClick = { onRepeat(order) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = bottomMargin, start = 20.dp, end = 20.dp)
                    .height(buttonHeight)
                    .widthIn(min = 200.dp),
            ) {
                Icon(
                    Icons.Default.Replay,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
                Spacer(Modifier.width(18.dp))
                Text(
                    text = "Repetir pedido",
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}


@Composable
private fun TimelineSection(current: List<ProcessStep>, order: Order) {
    val secondaryColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        SellerInfoCard(order)
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Progresso da entrega",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background
            )
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                current.forEachIndexed { idx, step ->
                    // val done = idx <= currentIndex
                    val iconColor = if (step.isConcluida) step.status.cor else secondaryColor
                    val textAndCanvasLineColor = if (step.isConcluida) MaterialTheme.colorScheme.background else secondaryColor

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (step.isConcluida) iconColor.copy(alpha = 0.15f) else Color.Transparent)
                        ) {
                            Icon(
                                imageVector = step.status.icon,
                                contentDescription = step.status.label,
                                tint = iconColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                        // Descrição
                        Text(
                            text = step.status.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = textAndCanvasLineColor,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    if (idx != current.lastIndex) {
                        Canvas(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp)
                        ) {
                            drawLine(
                                color = textAndCanvasLineColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 4f
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OrderItemsCard(order: Order) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Detalhes do pedido",
                modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Column(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                order.items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // imagem placeholder
                        Image(
                            painter = painterResource(item.imageUrl ?: R.drawable.pet_icon),
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                "Qtd: ${item.qty}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("R$ ${item.unitPrice}", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "Total: R$ ${item.qty * item.unitPrice}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (index < order.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Total Geral: ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "R$ ${order.total.toBrazilianCurrency()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "sem desconto",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 3.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Método de pagamento", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                order.paymentMethod,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (order.paymentStatus.contains(
                                        "Pago",
                                        true
                                    )
                                ) Success else Muted
                            )
                            Spacer(Modifier.width(4.dp))
                            if (order.paymentStatus.contains("Pago", true)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "pago", tint = Success, modifier = Modifier.size(16.dp))
                            } else {
                                Button(onClick = { /* pagar */ }) {
                                    Text("Pagar")
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}



@Composable
private fun SellerInfoCard(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // placeholder logo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFECEFF1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Storefront,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF9E9E9E)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(order.sellerName, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.background)
                Text(
                    order.sellerCity,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "rating",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("${order.sellerRating}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.background)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                OutlinedButton(onClick = { /* ver perfil */ }) {
                    Text("Perfil")
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { /* mensagem */ }) {
                    Icon(Icons.Default.ChatBubble, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Mensagem")
                }
            }
        }
    }
}



@Composable
private fun ActionsSection(
    order: Order,
    onRepeat: (Order) -> Unit,
    onRate: (Order) -> Unit,
    onSupport: (Order) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Informações de Entrega", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(order.customerName, style = MaterialTheme.typography.bodyLarge)
                Text(order.address, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(order.deliveryMethod, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                onClick = { onRate(order) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(Icons.Default.StarRate, contentDescription = null, tint = Color(0xFFE0CE36))
                Spacer(Modifier.width(8.dp))
                Text("Avalie o pedido")
            }
            ElevatedButton (
                onClick = { onSupport(order) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ajuda / Suporte")
            }
        }
    }
}

