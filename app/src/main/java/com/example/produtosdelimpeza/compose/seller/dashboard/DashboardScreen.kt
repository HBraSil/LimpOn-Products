package com.example.produtosdelimpeza.compose.seller.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.data.NavigationLastUserModeRepository
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


private const val todayRevenueMock = 1250.75  // R$ faturamento do dia
private const val activeOrdersMock = 7        // pedidos ativos
private val weeklySparkMock = listOf(6, 8, 5, 12, 10, 14, 11) // mock sparkline


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onCreateProduct: () -> Unit = {},
    onOpenOrders: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel()
) {
    // nav selection state for bottom bar
    var selectedTab by remember { mutableStateOf(DashboardTab.Home) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        navigationLastUserModeViewModel.saveLastUserMode(profileMode = ProfileMode.STORE.mode)
    }

    // scaffold with top app bar (visual icon instead of text)
    val listState = rememberLazyListState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { PremiumTopBar() },
        floatingActionButton = {
            // simple prominent FAB for primary action (create product)
            ExtendedFloatingActionButton(
                onClick = onCreateProduct,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Criar produto") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        bottomBar = {
            PremiumBottomNavigation(selected = selectedTab, onSelect = { newTab ->
                selectedTab = newTab
            }, onOpenOrders = onOpenOrders, onOpenProfile = onOpenProfile)
        }
    ) { innerPadding ->
        // Main content - switch by selectedTab (Home/Orders/Profile). Keep Home rich.
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                DashboardTab.Home -> PremiumHomeContent(listState = listState)
                DashboardTab.Orders -> OrdersPlaceholder(onOpenOrders)
                DashboardTab.Profile -> ProfilePlaceholder(onNavigateToCustomer)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTopBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            Color.White
        ),
        title = {
            // Visual title: brand icon + tiny sparkline
            Row(verticalAlignment = Alignment.CenterVertically) {
                // brand circular icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocalCafe, contentDescription = "Brand", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(10.dp))
                // subtle contextual chip (today)
                AssistChip(onClick = { /* calendar or date picker */ }, label = { Text("Hoje") })
            }
        },
        actions = {
            IconButton(onClick = { /* search */ }) { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            BadgedBox(badge = { Badge { Text("3") } }) {
                IconButton(onClick = { /* notifications */ }) { Icon(Icons.Default.Notifications, contentDescription = "Notificações") }
            }
        }
    )
}


@Composable
fun PremiumHomeContent(listState: LazyListState) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp)
    ) {
        item {
            StoreProfileCardAdvanced(
                storeName = "Pastelaria do Zé",
                avatarRes = null, // could be painterResource
                isOnline = true,
                itemsActive = 24,
                avgResponseTime = "8m",
                recentFeedbackCount = 7,
                conversionRate = 12.6f,
                onClick = {  }
            )
        }

        item {
            // Top KPI row - always visible
            KPIHeroRow(
                revenue = todayRevenueMock,
                activeOrders = activeOrdersMock,
                spark = weeklySparkMock
            )
        }

        item {
            // Secondary actions / quick insights
        }

        item {
            Spacer(Modifier.height(16.dp))
            SectionHeader(title = "Pedidos Recentes", actionText = "Ver tudo") { /* go to orders */ }
        }

        // Mock list of recent orders (light)
        items(5) { idx ->
            OrderCompactCard(idx + 1)
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StoreProfileCardAdvanced(
    storeName: String,
    avatarRes: Int? = null,
    isOnline: Boolean,
    itemsActive: Int,
    avgResponseTime: String,
    recentFeedbackCount: Int,
    conversionRate: Float,
    onClick: () -> Unit
) {
    // controls the expanded details area
    var expanded by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(targetValue = if (expanded) 10.dp else 4.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // avatar or placeholder
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarRes != null) {
                        Image(painter = painterResource(id = avatarRes), contentDescription = null, contentScale = ContentScale.Crop)
                    } else {
                        Icon(
                            Icons.Default.Store,
                            contentDescription = "Loja",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(storeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        // status chip
                        AssistChip(
                            onClick = { /* toggle online? */ },
                            label = { Text(if (isOnline) "Online" else "Offline", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Circle, contentDescription = null, modifier = Modifier.size(12.dp))
                            },
                            colors = AssistChipDefaults.assistChipColors(containerColor = if (isOnline) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // micro metrics inline
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TinyMetric(label = "Itens", value = "$itemsActive")
                        TinyMetric(label = "Resposta", value = avgResponseTime)
                    }
                }

                // chevron to indicate expand/collapse (animated)
                IconButton(onClick = { expanded = !expanded }) {
                    AnimatedContent(targetState = expanded) { expandedState ->
                        if (expandedState) {
                            Icon(Icons.Default.ExpandLess, contentDescription = "Fechar")
                        } else {
                            Icon(Icons.Default.ExpandMore, contentDescription = "Abrir")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // sparkline mock + conversion pill
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                // mini sparkline placeholder (replace by chart lib later)
                SparklineMock(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text("${"%.1f".format(conversionRate)}% conversão", style = MaterialTheme.typography.labelSmall)
                    Text("+${recentFeedbackCount} feedbacks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // expanded details (AnimatedVisibility)
            AnimatedVisibility(visible = expanded, enter = expandVertically(animationSpec = tween(220)) + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Controls quick actions
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SmallActionButton(icon = Icons.Default.Edit, label = "Editar Loja", onClick = { /* edit */ })
                        SmallActionButton(icon = Icons.Default.Place, label = "Raio Entrega", onClick = { /* edit radius */ })
                        SmallActionButton(icon = Icons.Default.Schedule, label = "Horário", onClick = { /* edit hours */ })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // quick toggles
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = true, onClick = {}, label = { Text("Aceita Retirada") })
                        FilterChip(selected = false, onClick = {}, label = { Text("Delivery 24h") })
                    }
                }
            }
        }
    }
}


@Composable
fun SparklineMock(modifier: Modifier = Modifier) {
    // Very simple visual placeholder for a sparkline (use chart library later)
    Box(modifier = modifier.height(36.dp)) {
        // just an abstract representation
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(6, 10, 8, 18, 12, 16, 9).forEach { h ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((h).dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                )
            }
        }
    }
}

@Composable
fun SmallActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier
        .height(44.dp)
        .clickable(onClick = onClick)
        .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun TinyMetric(label: String, value: String) {
    Column {
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// -----------------------------
// KPI Hero Row: Revenue + Active orders emphasized
// -----------------------------
@Composable
fun KPIHeroRow(revenue: Double, activeOrders: Int, spark: List<Int>) {
    // responsive: side-by-side on wide screens, stacked on narrow
    BoxWithConstraints(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        val wide = this.maxWidth > 600.dp
        val spacing = 12.dp

        if (wide) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                RevenueCard(revenue = revenue, spark = spark, modifier = Modifier.weight(1f))
                OrdersCard(activeOrders = activeOrders, modifier = Modifier.weight(1f))
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                RevenueCard(revenue = revenue, spark = spark, modifier = Modifier.fillMaxWidth())
                OrdersCard(activeOrders = activeOrders, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


@Composable
fun OrderFilterChips() {
    val chips = listOf("Todos", "Aguardando", "Preparação", "Entrega")
    var selected by remember { mutableIntStateOf(0) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEachIndexed { idx, label ->
            FilterChip(selected = selected == idx, onClick = { selected = idx }, label = { Text(label) })
        }
    }
}


@Composable
fun RevenueCard(revenue: Double, spark: List<Int>, modifier: Modifier = Modifier) {

    Column {
        SalesSummaryCardInteractive(
            itemsSoldToday = 34,
            revenueToday = 1250.75f,
            onClick = { /* navigate to detail */ }
        )
    }
}


// OrdersCard (corrigida)
@Composable
fun OrdersCard(activeOrders: Int, modifier: Modifier = Modifier) {

    Column(modifier = Modifier
        .padding(16.dp)
        .animateContentSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Pedidos Ativos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))

                // AnimatedContent sem transitionSpec
                AnimatedContent(targetState = activeOrders) { count ->
                    Text(
                        "$count",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // action button inside card
            ElevatedButton(onClick = { /* abrir pedidos */ }) {
                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gerenciar")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Responda rápido para manter altas avaliações.*",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }

}

// -----------------------------
// Sparkline mock (simple columns) - replace with chart lib if desired
// -----------------------------
@Composable
fun Sparkline(spark: List<Int>, modifier: Modifier = Modifier) {
    val max = (spark.maxOrNull() ?: 1).toFloat()
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        spark.forEach { v ->
            val h = (v / max) * 40f
            Box(modifier = Modifier
                .weight(1f)
                .height(with(LocalDensity.current) { h.dp })
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
            )
        }
    }
}

// -----------------------------
// Quick insights row (smaller cards)
// -----------------------------
@Composable
fun QuickInsightsRow() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MiniInsightCard(title = "Conversão", value = "12.6%")
        MiniInsightCard(title = "Tempo médio", value = "14m")
        MiniInsightCard(title = "Itens ativos", value = "42")
    }
}

@Composable
fun MiniInsightCard(title: String, value: String) {
    Card(modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(96.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TextButton(onClick = { /* quick action */ }) { Text("Ver") }
        }
    }
}

// -----------------------------
// Orders compact card (mock)
// -----------------------------
@Composable
fun OrderCompactCard(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Text("C$index", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("#${1000 + index}", fontWeight = FontWeight.SemiBold)
                Text("Cliente Nome", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("R$ ${(20 + index * 5)}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                AssistChip(onClick = { /* status filter */ }, label = { Text("Aguardando") })
            }
        }
    }
}

// -----------------------------
// Section header helper
// -----------------------------
@Composable
fun SectionHeader(title: String, actionText: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = onAction) { Text(actionText) }
        }

        OrderFilterChips()
    }
}

// -----------------------------
// Bottom navigation
// -----------------------------
enum class DashboardTab { Home, Orders, Profile }

@Composable
fun PremiumBottomNavigation(selected: DashboardTab, onSelect: (DashboardTab) -> Unit, onOpenOrders: () -> Unit, onOpenProfile: () -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == DashboardTab.Home,
            onClick = { onSelect(DashboardTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") }
        )
        NavigationBarItem(
            selected = selected == DashboardTab.Orders,
            onClick = {
                onSelect(DashboardTab.Orders)
                onOpenOrders()
            },
            icon = { BadgedBox(badge = { Badge { Text("7") } }) { Icon(Icons.Default.List, contentDescription = "Pedidos") } },
            label = { Text("Pedidos") }
        )
        NavigationBarItem(
            selected = selected == DashboardTab.Profile,
            onClick = {
                onSelect(DashboardTab.Profile)
                onOpenProfile()
            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

// -----------------------------
// Simple placeholders for tabs
// -----------------------------
@Composable
fun OrdersPlaceholder(onOpenOrders: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(8.dp))
            Text("Tela de pedidos (placeholder)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onOpenOrders) { Text("Abrir pedidos completos") }
        }
    }
}

@Composable
fun ProfilePlaceholder(onOpenProfile: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(8.dp))
            Text("Perfil da loja (placeholder)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onOpenProfile) { Text("Mudar para usuário", color = MaterialTheme.colorScheme.onSurface) }
        }
    }
}

data class DaySales(
    val dayLabel: String,   // ex: "SEG", "TER"
    val itemsSold: Int,
    val revenue: Float      // in currency units, e.g. BRL
)

@Composable
fun SalesSummaryCardInteractive(
    itemsSoldToday: Int,
    revenueToday: Float,
    modifier: Modifier = Modifier,
    currencyLocale: Locale = Locale("pt", "BR"),
    onClick: () -> Unit = {}
) {
    val currencyFormatter = remember(currencyLocale) { NumberFormat.getCurrencyInstance(currencyLocale) }

    val animatedItems by animateIntAsState(targetValue = itemsSoldToday, animationSpec = tween(600))
    val animatedRevenue by animateFloatAsState(
        targetValue = revenueToday,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role = Role.Button,
                onClick = onClick
            )
            .semantics {
                this.contentDescription =
                    "Resumo de vendas: $animatedItems itens vendidos, faturamento ${
                        currencyFormatter.format(
                            animatedRevenue.toDouble()
                        )
                    }. Toque para ver detalhes."
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.weight(0.45f)) {
            Row(
                Modifier.padding(10.dp)
            ) {
                Text(
                    "Vendas hoje",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$animatedItems itens",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Faturamento",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currencyFormatter.format(animatedRevenue.toDouble()),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider()
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .padding(horizontal = 8.dp),
            ) {
                val mock = listOf(
                    DaySales("SEG", 12, 300f),
                    DaySales("TER", 18, 450f),
                    DaySales("QUA", 15, 500f),
                    DaySales("QUI", 22, 800f),
                    DaySales("SEX", 20, 700f),
                    DaySales("SAB", 25, 900f),
                    DaySales("DOM", 19, 650f)
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    // TÍTULO ISOLADO E FIXO NO TOPO
                    Text(
                        "Vendas - Últimos 7 dias",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Toque no gráfico para ver valores por dia",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(30.dp)) // Espaçamento entre título e gráfico
                    // GRÁFICO OTIMIZADO PARA USAR O RESTANTE DO ESPAÇO
                    BarChart7Days(
                        days = mock,
                        // weight(1f) garante que o gráfico use TODO o espaço vertical restante
                        modifier = Modifier
                            .fillMaxWidth(),
                        barSpacingDp = 4f // Redução do espaçamento das barras para maximizar o rótulo
                    )
                }
            }
        }
    }
}


@Composable
fun BarChart7Days(
    modifier: Modifier = Modifier,
    days: List<DaySales>,
    barSpacingDp: Float = 4f,
    barCornerRadiusDp: Float = 4f,
    barGradientTop: Color = MaterialTheme.colorScheme.primary,
    barGradientBottom: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
    tooltipBackground: Color = MaterialTheme.colorScheme.surface,
    tooltipTextColor: Color = MaterialTheme.colorScheme.onSurface,
    onDaySelected: (DaySales) -> Unit = {}
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { barSpacingDp.dp.toPx() }
    val cornerRadiusPx = with(density) { barCornerRadiusDp.dp.toPx() }

    if (days.isEmpty()) return

    // estados do gráfico
    var barRects by remember { mutableStateOf<List<Rect>>(emptyList()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var tooltipLocalAnchor by remember { mutableStateOf(Offset.Zero) } // local ao Canvas
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    val maxRevenue = days.maxOfOrNull { it.revenue }.let { if (it == 0f) 1f else it } ?: 1f

    // animações das barras
    val animList = remember(days) { days.map { Animatable(0f) } }
    LaunchedEffect(days, maxRevenue) {
        animList.forEachIndexed { i, anim ->
            val target = (days.getOrNull(i)?.revenue ?: 0f) / maxRevenue
            launch {
                delay(i * 40L)
                anim.animateTo(target.coerceIn(0f, 1f), animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing))
            }
        }
    }

    // dimensões estáveis do tooltip (usadas para decidir cima/baixo sem toggle)
    val tooltipWidthDp = 110.dp
    val tooltipHeightDp = 56.dp
    val pointerHeightDp = 6.dp
    val tooltipWidthPxStable = with(density) { tooltipWidthDp.toPx() }
    val tooltipHeightPxStable = with(density) { tooltipHeightDp.toPx() }
    val pointerHeightPx = with(density) { pointerHeightDp.toPx() }
    val safetyMarginPx = with(density) { 6.dp.toPx() }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopStart
        ) {
            // Canvas principal
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(days) {
                        detectTapGestures { offset ->
                            val idx = barRects.indexOfFirst { it.contains(offset) }
                            if (idx >= 0 && idx < days.size) {
                                selectedIndex = idx
                                val barTop = barRects[idx].top
                                val barCenterX = barRects[idx].center.x
                                tooltipLocalAnchor = Offset(barCenterX, barTop)
                                onDaySelected(days[idx])
                            } else {
                                selectedIndex = null
                            }
                        }
                    }
                    .onGloballyPositioned { coords ->
                        canvasSize = coords.size.toSize()
                    }
            ) {
                val w = size.width
                val h = size.height
                val count = days.size
                val totalSpacing = spacingPx * (count - 1)
                val availableW = (w - totalSpacing).coerceAtLeast(1f)
                val barWidth = availableW / count
                val gradientBrush = Brush.verticalGradient(colors = listOf(barGradientTop, barGradientBottom))
                val newRects = mutableListOf<Rect>()

                for (i in 0 until count) {
                    val left = i * (barWidth + spacingPx)
                    val right = left + barWidth
                    val valueRatio = animList.getOrNull(i)?.value ?: 0f
                    val top = h * (1f - valueRatio)
                    val barRect = Rect(left, top, right, h)

                    drawRoundRect(
                        brush = gradientBrush,
                        topLeft = Offset(barRect.left, barRect.top),
                        size = Size(barRect.width, barRect.height),
                        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                    )

                    // margem para hit test
                    newRects.add(Rect(barRect.left, barRect.top - 6f, barRect.right, barRect.bottom + 6f))

                    if (selectedIndex == i) {
                        drawRoundRect(
                            color = barGradientTop.copy(alpha = 0.18f),
                            topLeft = Offset(barRect.left - 4f, barRect.top - 4f),
                            size = Size(barRect.width + 8f, barRect.height + 8f),
                            cornerRadius = CornerRadius(cornerRadiusPx + 4f, cornerRadiusPx + 4f)
                        )
                    }
                }
                barRects = newRects
            }

            // overlay tooltip (interno ao Box, usa offset em Dp)
            if (selectedIndex != null && selectedIndex!! in days.indices && canvasSize != Size.Zero) {
                val sel = days[selectedIndex!!]
                val tooltipText1 = "${sel.itemsSold} itens"
                val tooltipText2 = currencyFormatter.format(sel.revenue.toDouble())

                // anchor local no Canvas (px)
                val anchorX = tooltipLocalAnchor.x
                val anchorY = tooltipLocalAnchor.y

                // calcular xPx centralizado e clamped dentro do canvas
                val sideMarginPx = with(density) { 8.dp.toPx() }
                var xPx = anchorX - tooltipWidthPxStable / 2f
                xPx = xPx.coerceIn(sideMarginPx, canvasSize.width - tooltipWidthPxStable - sideMarginPx)

                // decide se cabe acima (usando valor estável)
                // distância opcional entre a barra e o tooltip quando fica acima
                // espaço entre o tooltip e a parte superior da barra quando tooltip aparece acima
                val tooltipMarginAbovePx = with(density) { 3.dp.toPx() }

// decide se cabe acima com a margem adicional
                val preferredYPxAbove =
                    anchorY - (tooltipHeightPxStable + pointerHeightPx + safetyMarginPx + tooltipMarginAbovePx)


                val fitsAbove = preferredYPxAbove >= 0f

                // yPx local final (px)
                val yPx = if (fitsAbove) {
                    preferredYPxAbove.coerceAtLeast(0f)
                } else {
                    (anchorY + pointerHeightPx + safetyMarginPx).coerceAtMost((canvasSize.height - tooltipHeightPxStable))
                }

                // pointerX dentro do tooltip (px)
                val pointerXInTooltip = (anchorX - xPx).coerceIn(pointerHeightPx, tooltipWidthPxStable - pointerHeightPx)

                // converte para Dp (offset do tooltip dentro do Box/Canvas)
                val offsetXDp = with(density) { xPx.toDp() }
                val offsetYDp = with(density) { yPx.toDp() }

                // AnimatedVisibility qualificada para evitar conflitos de receiver
                androidx.compose.animation.AnimatedVisibility(
                    visible = true,
                    modifier = Modifier
                        .offset(x = offsetXDp, y = offsetYDp)
                        .width(tooltipWidthDp)
                        .zIndex(1f),
                    enter = fadeIn(animationSpec = tween(200))
                            + scaleIn(animationSpec = tween(200, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(120))
                            + scaleOut(animationSpec = tween(120))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (!fitsAbove) {
                            // pointer ACIMA do tooltip (apontando para baixo)
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(pointerHeightDp)
                            ) {
                                val triW = 20f
                                val triH = 12f
                                val px = pointerXInTooltip

                                val path = Path().apply {
                                    moveTo(px, 0f)                     // topo do canvas (ponta para baixo)
                                    lineTo(px - triW / 2f, triH)       // base esquerda
                                    lineTo(px + triW / 2f, triH)       // base direita
                                    close()
                                }

                                drawPath(path = path, color = tooltipBackground)
                            }

                            // agora o tooltip aparece DEPOIS do triângulo
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = tooltipBackground,
                                tonalElevation = 6.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = sel.dayLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = tooltipText1,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = tooltipTextColor
                                    )
                                    Text(
                                        text = tooltipText2,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tooltipTextColor
                                    )
                                }
                            }
                        }
                        else {
                            // tooltip acima (surface acima, pointer abaixo)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = pointerHeightDp),
                                shape = RoundedCornerShape(8.dp),
                                color = tooltipBackground,
                                tonalElevation = 6.dp
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = sel.dayLabel, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.height(4.dp))
                                    Text(text = tooltipText1, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = tooltipTextColor)
                                    Text(text = tooltipText2, style = MaterialTheme.typography.bodySmall, color = tooltipTextColor)
                                }
                            }
                            // pointer abaixo (apontando pra baixo)
                            Canvas(modifier = Modifier
                                .fillMaxWidth()
                                .height(pointerHeightDp)) {
                                val triW = 20f
                                val triH = 12f
                                val px = pointerXInTooltip
                                val path = Path().apply {
                                    moveTo(px, triH)
                                    lineTo(px - triW / 2f, 0f)
                                    lineTo(px + triW / 2f, 0f)
                                    close()
                                }
                                drawPath(path = path, color = tooltipBackground)
                            }
                        }
                    }
                } // AnimatedVisibility end
            } // overlay if end
        } // Box end

        Spacer(modifier = Modifier.height(8.dp))

        // rótulos X
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val count = days.size
            for (i in 0 until count) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = days[i].dayLabel, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                }
            }
        }
    }
}


@Preview
@Composable
fun VendorDashboardModernScreenPreview() {
    DashboardScreen()
}
