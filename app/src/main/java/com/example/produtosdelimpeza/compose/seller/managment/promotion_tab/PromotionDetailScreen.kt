package com.example.produtosdelimpeza.compose.seller.managment.promotion_tab

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

enum class DiscountType {
    PERCENTAGE,
    FIXED_VALUE
}

data class Promotion(
    val title: String,
    val description: String,
    val discountType: DiscountType,
    val discountValue: String,
    val appliesToAllProducts: Boolean,
    val category: String?,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val ordersCount: Int,
    val totalSales: String,
    val isActive: Boolean,
    val isCanceled: Boolean
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionDetailsScreen(onBackNavigation: () -> Unit) {
    val promotion = Promotion(
        title = "Semana do Hambúrguer",
        description = "Desconto especial para impulsionar as vendas durante a semana",
        discountType = DiscountType.PERCENTAGE,
        discountValue = "20",
        appliesToAllProducts = false,
        category = "Lanches",
        startDate = "10/01/2026",
        endDate = "17/01/2026",
        createdAt = "08/01/2026",
        ordersCount = 128,
        totalSales = "4.560,00",
        isActive = true,
        isCanceled = false
    )

    val statusLabel = when {
        promotion.isCanceled -> "Encerrada manualmente"
        promotion.isActive -> "Ativa"
        else -> "Expirada"
    }

    var showEditSheet by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalhes da promoção") },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 🔹 Header
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = promotion.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = promotion.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text(statusLabel) })

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                if (promotion.appliesToAllProducts)
                                    "Todos os produtos"
                                else
                                    "Categoria: ${promotion.category}"
                            )
                        }
                    )
                }
            }


            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = when (promotion.discountType) {
                            DiscountType.PERCENTAGE ->
                                "${promotion.discountValue}% OFF"
                            DiscountType.FIXED_VALUE ->
                                "R$ ${promotion.discountValue} de desconto"
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Divider()

                    InfoRow("Início", promotion.startDate)
                    InfoRow("Término", promotion.endDate)
                }
            }

            PromotionPerformanceSection()

            PromotionInfoSection()

            if (promotion.isActive && !promotion.isCanceled) {
                Spacer(Modifier.height(20.dp))
                ElevatedButton(
                    onClick = { showEditSheet = true },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ajustar Prazo ou Limite")
                }


                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Encerrar promoção")
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }

    if (showEditSheet) {
        EditPromotionBottomSheet(
            promotion = promotion,
            onDismiss = { showEditSheet = false },
            onSave = { title, desc, endDate ->
                showEditSheet = false
                // salvar alterações
            }
        )
    }

    if (showCancelDialog) {
        CancelPromotionDialog(
            onConfirm = {
                showCancelDialog = false
                // cancelar promoção
            },
            onDismiss = { showCancelDialog = false }
        )
    }

}


@Composable
fun PromotionPerformanceSection(
    ordersCount: Int = 8,
    totalSales: String = "2.000",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Desempenho da promoção",
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            MetricCard(
                icon = Icons.Default.ReceiptLong,
                label = "Pedidos",
                value = ordersCount.toString(),
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                icon = Icons.Default.AttachMoney,
                label = "Total vendido",
                value = "R$ $totalSales",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null)

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun PromotionInfoSection(
    createdAt: String = "Ontem",
    discountType: DiscountType = DiscountType.PERCENTAGE,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Informações gerais",
            style = MaterialTheme.typography.titleSmall
        )

        InfoItem(
            icon = Icons.Default.LocalOffer,
            label = "Tipo de desconto",
            value = when (discountType) {
                DiscountType.PERCENTAGE -> "Percentual (%)"
                DiscountType.FIXED_VALUE -> "Valor fixo (R$)"
            }
        )
    }
}
@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPromotionBottomSheet(
    promotion: Promotion,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(promotion.title) }
    var description by remember { mutableStateOf(promotion.description) }
    var endDate by remember { mutableStateOf(promotion.endDate) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        //dragHandle = { ModalBottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Editar promoção",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nome da promoção") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = endDate,
                onValueChange = {},
                enabled = false,
                label = { Text("Data de término") },
                supportingText = {
                    Text("A data só pode ser antecipada")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            Text(
                text = "Algumas informações não podem ser alteradas após a promoção estar ativa.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { onSave(title, description, endDate) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar alterações")
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}


@Composable
fun CancelPromotionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Encerrar promoção?")
        },
        text = {
            Text(
                "Esta ação é definitiva. A promoção será encerrada imediatamente e não poderá ser reativada."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Encerrar promoção")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PromotionDetailsPreview_Active() {
    MaterialTheme {
        PromotionDetailsScreen(
            onBackNavigation = {},
        )
    }
}
