package com.example.produtosdelimpeza.compose.seller.dashboard

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalOffer
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


private const val activeOrdersMock = 7        // pedidos ativos


data class DaySales(
    val dayLabel: String,   // ex: "SEG", "TER"
    val itemsSold: Int,
    val revenue: Float      // in currency units, e.g. BRL
)


data class MinFabItem(
    var icon: ImageVector,
    var name: String,
)

const val COUPON = "Criar cupom"
const val PROMOTION = "Criar promoção"
const val PRODUCT = "Criar produto"



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel,
    onNotificationsScreenClick: () -> Unit = {},
    onNavigateToAnalyticsScreenClick: () -> Unit = {},
    onNavigateToItemFab: (String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        navigationLastUserModeViewModel.saveLastUserMode(ProfileMode.LoggedIn.Store)
    }

    // scaffold with top app bar (visual icon instead of text)
    val listState = rememberLazyListState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            PremiumTopBar(
                goToNotificationsScreen = onNotificationsScreenClick
            )
        },
        floatingActionButton = {
            MultiFloatingButton{
                onNavigateToItemFab(it)
            }
        },
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp)
        ) {
            item {
                StoreProfileCardAdvanced(
                    storeName = "Pastelaria do Zé",
                    avatarRes = null, // could be painterResource
                    itemsActive = 24,
                    avgResponseTime = "8m",
                    recentFeedbackCount = 7,
                    onClick = {  }
                )
            }

            item {
                // Top KPI row - always visible
                KPIHeroRow(
                    activeOrders = activeOrdersMock,
                    onNavigateToAnalyticsScreenClick = onNavigateToAnalyticsScreenClick
                )
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTopBar(goToNotificationsScreen: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        title = {
            ShopStatusComponent()
        },
        actions = {
            BadgedBox(
                badge = { Badge { Text("3") } },
                modifier = Modifier.padding(end = 12.dp)
            ) {
                IconButton(onClick = goToNotificationsScreen) { Icon(Icons.Default.Notifications, contentDescription = "Notificações") }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            Color.White
        )
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StoreProfileCardAdvanced(
    storeName: String,
    avatarRes: Int? = null,
    itemsActive: Int,
    avgResponseTime: String,
    recentFeedbackCount: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(10.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable { onClick() },

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                Text(storeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TinyMetric(label = "Itens", value = "$itemsActive")
                    TinyMetric(label = "Resposta", value = avgResponseTime)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("+${recentFeedbackCount} novos feedbacks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = true, onClick = {}, label = { Text("Aceita Retirada") })
                FilterChip(selected = false, onClick = {}, label = { Text("Delivery 24h") })
            }
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



@Composable
fun KPIHeroRow(activeOrders: Int, onNavigateToAnalyticsScreenClick: () -> Unit = {}) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val wide = this.maxWidth > 600.dp
        val spacing = 12.dp

        if (wide) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                RevenueCard(onNavigateToAnalyticsScreenClick = onNavigateToAnalyticsScreenClick)
                OrdersCard(activeOrders = activeOrders)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                RevenueCard(onNavigateToAnalyticsScreenClick = onNavigateToAnalyticsScreenClick)
                OrdersCard(activeOrders = activeOrders)
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
fun RevenueCard(onNavigateToAnalyticsScreenClick: () -> Unit = {}) {
    Column {
        SalesSummaryCardInteractive(
            itemsSoldToday = 34,
            revenueToday = 1250.75f,
            onClick = {
                onNavigateToAnalyticsScreenClick()
            }
        )
    }
}


@Composable
fun OrdersCard(activeOrders: Int) {

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

                AnimatedContent(targetState = activeOrders) { count ->
                    Text(
                        "$count",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            ElevatedButton(
                onClick = { /* abrir pedidos */ },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
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
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = onAction) { Text(actionText) }
        }

        OrderFilterChips()
    }
}


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
        Column(
            modifier = Modifier
                .weight(0.45f)
                .background(color =  MaterialTheme.colorScheme.background.copy(0.4f))
        ) {
            Row(
                Modifier.padding(10.dp)
            ) {
                Text(
                    "Vendas hoje",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$animatedItems itens",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Faturamento",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currencyFormatter.format(animatedRevenue.toDouble()),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSecondary.copy(blue = 1f)
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
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
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
    barGradientTop: Color = MaterialTheme.colorScheme.surfaceVariant,
    barGradientBottom: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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

            if (selectedIndex != null && selectedIndex!! in days.indices && canvasSize != Size.Zero) {
                val sel = days[selectedIndex!!]
                val tooltipText1 = "${sel.itemsSold} itens"
                val tooltipText2 = currencyFormatter.format(sel.revenue.toDouble())

                val anchorX = tooltipLocalAnchor.x
                val anchorY = tooltipLocalAnchor.y

                val sideMarginPx = with(density) { 8.dp.toPx() }
                var xPx = anchorX - tooltipWidthPxStable / 2f
                xPx = xPx.coerceIn(sideMarginPx, canvasSize.width - tooltipWidthPxStable - sideMarginPx)

                val tooltipMarginAbovePx = with(density) { 3.dp.toPx() }

                val preferredYPxAbove =
                    anchorY - (tooltipHeightPxStable + pointerHeightPx + safetyMarginPx + tooltipMarginAbovePx)


                val fitsAbove = preferredYPxAbove >= 0f

                // yPx local final (px)
                val yPx = if (fitsAbove) {
                    preferredYPxAbove.coerceAtLeast(0f)
                } else {
                    (anchorY + pointerHeightPx + safetyMarginPx).coerceAtMost((canvasSize.height - tooltipHeightPxStable))
                }

                val pointerXInTooltip = (anchorX - xPx).coerceIn(pointerHeightPx, tooltipWidthPxStable - pointerHeightPx)

                // converte para Dp (offset do tooltip dentro do Box/Canvas)
                val offsetXDp = with(density) { xPx.toDp() }
                val offsetYDp = with(density) { yPx.toDp() }

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

                                val path = Path().apply {
                                    moveTo(pointerXInTooltip, 0f)                     // topo do canvas (ponta para baixo)
                                    lineTo(pointerXInTooltip - triW / 2f, triH)       // base esquerda
                                    lineTo(pointerXInTooltip + triW / 2f, triH)       // base direita
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
                                val path = Path().apply {
                                    moveTo(pointerXInTooltip, triH)
                                    lineTo(pointerXInTooltip - triW / 2f, 0f)
                                    lineTo(pointerXInTooltip + triW / 2f, 0f)
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



@Composable
fun MultiFloatingButton(onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val list = listOf(
        MinFabItem(Icons.Outlined.Campaign, PROMOTION),
        MinFabItem(Icons.Outlined.LocalOffer, COUPON),
        MinFabItem(Icons.Outlined.Inventory2, PRODUCT)
    )


    Column(horizontalAlignment = Alignment.End,  verticalArrangement = Arrangement.Center) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically(),
        ) {
            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                list.forEach { item ->
                    MinFab(item) {
                        onClick(item.name)
                    }
                }
            }
        }


        val transition = updateTransition(targetState = expanded, label = "transition")
        val rotate by transition.animateFloat(label = "rotate") {
            if (it) 90f else 0f
        }
        ExtendedFloatingActionButton(
            onClick = { expanded = !expanded },
            text = { Text(if (expanded) "Fechar" else "Criar") },
            icon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotate)

                )
            },
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.background
        )
    }
}



@Composable
fun MinFab(item: MinFabItem, onClick: (String) -> Unit = {}) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                //.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                .padding(4.dp)
                .wrapContentWidth(),
        ) {
            Text(
                text = item.name, modifier = Modifier.wrapContentWidth(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleMedium
            )
        }


        FloatingActionButton(
            onClick = {
                when(item.name) {
                    "Add" -> onClick("Add")
                    "Delete" -> onClick("Delete")
                    else -> onClick("Save")
                }
            },
            modifier = Modifier
                .padding(start = 10.dp, bottom = 7.dp)
                .size(40.dp),
            containerColor = MaterialTheme.colorScheme.onBackground
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}