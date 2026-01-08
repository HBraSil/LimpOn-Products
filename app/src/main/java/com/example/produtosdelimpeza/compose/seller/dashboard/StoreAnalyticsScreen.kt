package com.example.produtosdelimpeza.compose.seller.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.navigation.route.StoreScreen

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
            modifier = Modifier.padding(padding)
        ) {
            item { FaturamentoHeroCard() }
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(500)) + slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(500)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        RevenueChartSection()
                        KpiSection()
                        BehaviorAnalysisSection()
                        InsightsSection()
                    }
                }
            }
        }
    }

    if (showInfoSheet) {
        FaturamentoInfoBottomSheet(
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
fun FaturamentoHeroCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(10.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 6.dp,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A),
                            Color(0xFF020617)
                        )
                    )
                )
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Faturamento Total (7 dias)",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "R$ 12.450,80",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color(0xFF22C55E).copy(alpha = 0.15f),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "+12,5% vs semana anterior",
                        color = Color(0xFF22C55E),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun RevenueChartSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Faturamento diário",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.BarChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/* ---------- KPI SECTION ---------- */

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

/* ---------- BEHAVIOR ANALYSIS ---------- */

@Composable
private fun BehaviorAnalysisSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Comportamento de vendas",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BehaviorBox(
                title = "Dias fortes",
                description = "Sex • Sáb"
            )
            BehaviorBox(
                title = "Dias fracos",
                description = "Seg • Ter"
            )
        }
    }
}

@Composable
private fun BehaviorBox(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = description,
            fontWeight = FontWeight.SemiBold
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
                description = "Aumente vendas em horários estratégicos"
            )

            PromotionActionItem(
                icon = Icons.AutoMirrored.Outlined.TrendingDown,
                title = "Promoção para dias fracos",
                description = "Incentive pedidos em dias com menor faturamento"
            )

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
fun FaturamentoInfoBottomSheet(onDismiss: () -> Unit) {
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



@Preview
@Composable
private fun StoreAnalyticsScreenPreview() {
    MaterialTheme {
        StoreAnalyticsScreen()
    }
}
