package com.example.produtosdelimpeza.compose.seller.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen


private data class ProductRank(
    val name: String,
    val quantity: Int
)

private val Emerald = Color(0xFF10B981)
private val Slate = Color(0xFF3D3030).copy(0.7f)
private val Indigo = Color(0xFF4F46E5)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreAnalyticsScreen(
    onBackNavigation: () -> Unit = {},
    onPromotionActionItemClick: (String) -> Unit = {}
) {
    var showInfoSheet by remember { mutableStateOf(false) }
    var showCreatePromoSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackNavigation) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = stringResource(R.string.icon_navigate_back)
                            )
                        }
                        Text(
                            text = "Análise de Faturamento",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoSheet = true }) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreatePromoSheet = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(Icons.Outlined.LocalOffer, contentDescription = null)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item {
                RevenueHero()
                Spacer(Modifier.height(12.dp))
                SecondaryMetrics()
            }
            item { DailyRevenueSection() }
            item { KpiSection() }
            item { BehaviorAnalysisSection() }
            item { TopSellingProducts() }
            item { InsightsSection() }
        }
    }

    if (showInfoSheet) {
        RevenueInfoBottomSheet(
            onDismiss = { showInfoSheet = false }
        )
    }
    if (showCreatePromoSheet) {
        CreatePromotionBottomSheet(
            onDismiss = { showCreatePromoSheet = false },
            onPromotionActionItemClick = { tela ->
                onPromotionActionItemClick(tela)
            }
        )
    }
}



@Composable
private fun RevenueHero() {
    Column {
        Text(
            text = "Receita Bruta",
            fontSize = 14.sp,
            color = Slate
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "R$ 18.450,90",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "▲ 12,4% em relação ao período anterior",
            fontSize = 14.sp,
            color = Emerald,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
private fun SecondaryMetrics() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricSmall("Pedidos", "284")
        MetricSmall("Ticket Médio", "R$ 64,90")
        MetricSmall("Cancelados", "2")
    }
}

@Composable
private fun MetricSmall(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Slate)
        Text(text = value, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun DailyRevenueSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Faturamento diário",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Hoje vs. Última Terça",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header do Card com Valor Atual e Badge de Performance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "R$ 1.240,00",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Atualizado às 14:30",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Badge de Performance (Moderno)
                    Surface(
                        color = Color(0xFFECFDF5),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "15% acima",
                                color = Color(0xFF065F46),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Visualização de Mini-Gráfico de Área (Moderno e Minimalista)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Linha de Meta (Tracejada ou Opaca)
                    Divider(
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                        color = Color(0xFFF1F5F9),
                        thickness = 2.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Simulação de barras de faturamento por hora (Estilo Moderno)
                        val hourlyData = listOf(0.2f, 0.3f, 0.45f, 0.4f, 0.6f, 0.8f, 0.9f, 0.7f, 0.4f, 0.2f)
                        hourlyData.forEachIndexed { index, value ->
                            val isCurrentHour = index == 6
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(value)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(
                                        if (isCurrentHour) Color(0xFF4F46E5)
                                        else Color(0xFF4F46E5).copy(alpha = 0.2f)
                                    )
                            )
                        }
                    }
                }

                // Legenda de Horários
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("08h", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("12h", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("16h", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("20h", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("00h", fontSize = 10.sp, color = Color(0xFF94A3B8))
                }
            }
        }
    }
}


@Composable
private fun KpiSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        KpiItem(
            title = "Total",
            value = "R$ 8.420",
            highlight = true
        )
        KpiItem(
            title = "Média/dia",
            value = "R$ 1.203"
        )
        KpiItem(
            title = "Melhor dia",
            value = "Sábado"
        )
    }
}

@Composable
private fun KpiItem(
    title: String,
    value: String,
    highlight: Boolean = false
) {
    Column(
        modifier = Modifier.width(110.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = if (highlight) 20.sp else 16.sp,
            color = if (highlight) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun BehaviorAnalysisSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Comportamento de vendas",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                BehaviorLine(
                    label = "Dias fracos",
                    days = "Seg • Ter",
                    accentColor = Color(0xFFF59E0B) // amber
                )

                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                BehaviorLine(
                    label = "Dias fortes",
                    days = "Sex • Sáb",
                    accentColor = Color(0xFF10B981) // emerald
                )
            }
        }
    }
}


@Composable
private fun BehaviorLine(
    label: String,
    days: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Label à esquerda
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Dias à direita (destaque)
        Text(
            text = days,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = accentColor
        )
    }
}


/* ---------- INSIGHTS SECTION ---------- */

@Composable
private fun InsightsSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Insights",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )

        InsightItem("Sábados concentram 35% do faturamento")
        InsightItem("Quintas mostram crescimento constante")
        InsightItem("Segundas possuem menor volume de pedidos")
    }
}

@Composable
private fun InsightItem(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePromotionBottomSheet(
    onDismiss: () -> Unit,
    onPromotionActionItemClick: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Criar promoção",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Baseada no desempenho da sua loja",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔥 Sugestão inteligente
            PromotionInsightCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "O que você quer criar?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            PromotionActionItem(
                icon = Icons.Outlined.ConfirmationNumber,
                title = "Cupom de desconto",
                description = "Ofereça um cupom para atrair mais pedidos"
            ){
                onPromotionActionItemClick(StoreScreen.CREATE_COUPUN.route)
                onDismiss()
            }

            PromotionActionItem(
                icon = Icons.Outlined.Schedule,
                title = "Promoção por tempo limitado",
                description = "Aumente vendas em horários estratégicos",
            ) {
                onPromotionActionItemClick(StoreScreen.CREATE_PROMOTION.route)
                onDismiss()
            }

            /*PromotionActionItem(
                icon = Icons.AutoMirrored.Outlined.TrendingDown,
                title = "Promoção para dias fracos",
                description = "Incentive pedidos em dias com menor faturamento"
            )*/

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun PromotionInsightCard() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF22C55E).copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = Color(0xFF22C55E),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Sugestão",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF166534)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Segundas-feiras têm o menor faturamento da semana. Que tal criar uma promoção exclusiva para esse dia?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PromotionActionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueInfoBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Como calculamos seu faturamento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoItem(
                title = "O que está incluído",
                description = "Pedidos pagos e concluídos dentro do período selecionado."
            )

            InfoItem(
                title = "O que não entra no cálculo",
                description = "Pedidos cancelados, estornados ou ainda em andamento."
            )

            InfoItem(
                title = "Atualização dos dados",
                description = "Os valores são atualizados automaticamente no fim do expediente."
            )

            InfoItem(
                title = "Possíveis variações",
                description = "O faturamento pode mudar caso ocorram cancelamentos após a finalização."
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    description: String
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun TopSellingProducts() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Top 5 produtos mais vendidos",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.background,
           // tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val products = listOf(
                    ProductRank("Hambúrguer Artesanal", 420),
                    ProductRank("Batata Especial", 310),
                    ProductRank("Pizza Média", 260),
                    ProductRank("Refrigerante Lata", 190),
                    ProductRank("Milkshake", 140)
                )

                products.forEachIndexed { index, product ->
                    ProductRankRow(
                        position = index + 1,
                        name = product.name,
                        quantity = product.quantity
                    )
                }
            }
        }
    }
}


@Composable
private fun ProductRankRow(
    position: Int,
    name: String,
    quantity: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Posição
        Text(
            text = position.toString(),
            modifier = Modifier.width(24.dp),
            fontWeight = FontWeight.Bold,
            color = if (position == 1) Indigo else Slate
        )

        // Nome do produto
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = if (position == 1) FontWeight.Medium else FontWeight.Normal,
            color = if (position == 1) Indigo else Slate
        )

        // Quantidade vendida
        Text(
            text = "$quantity vendas",
            fontSize = 12.sp,
            color = if (position == 1) Indigo else Slate
        )
    }
}



@Preview
@Composable
private fun StoreAnalyticsScreenPreview() {
    MaterialTheme {
        StoreAnalyticsScreen()
    }
}
