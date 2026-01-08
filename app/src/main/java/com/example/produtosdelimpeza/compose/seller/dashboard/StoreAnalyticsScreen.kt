package com.example.produtosdelimpeza.compose.seller.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val AccentGreen = Color(0xFF34D399)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreAnalyticsScreen() {
    var selectedPeriod by remember { mutableStateOf("Últimos 7 dias") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Análise de Faturamento",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = selectedPeriod,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary
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
}


@Composable
fun FaturamentoHeroCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background, Color(0xFF1E293B)))
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Faturamento Total (7 dias)", color = Color.Gray, style = MaterialTheme.typography.labelLarge)
            Text(
                "R$ 12.450,80",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Surface(
                color = AccentGreen.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, null, tint = AccentGreen, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("+12.5% vs semana anterior", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AiInsightCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Green.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, "IA", tint = Color.Green)
            Spacer(Modifier.width(12.dp))
            Text(
                "Insight: Suas vendas de 'Bebidas' cresceram 20% após às 20h. Que tal um combo?",
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
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

/* ---------- PREVIEW ---------- */

@Preview(
    showBackground = true,
    device = "id:pixel_7"
)
@Composable
private fun StoreAnalyticsScreenPreview() {
    MaterialTheme {
        StoreAnalyticsScreen()
    }
}
