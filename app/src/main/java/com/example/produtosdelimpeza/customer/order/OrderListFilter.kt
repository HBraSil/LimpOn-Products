package com.example.produtosdelimpeza.customer.order

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.produtosdelimpeza.customer.catalog.presentation.fractionToValue
import com.example.produtosdelimpeza.customer.catalog.presentation.valueToFraction
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.roundToInt


enum class PedidoStatus(val label: String, val color: Color) {
    RECEBIDO("Pedido Recebido", Color(0xFF1976D2)),
    EM_PREPARO("Em preparo", Color(0xFFFFA000)),
    A_CAMINHO("A caminho", Color(0xFF0288D1)),
    ENTREGUE("Entregue", Color(0xFF2E7D32)),
    CANCELADO("Cancelado", Color(0xFFD32F2F))
}

// ------------------------
// Filters: model & sheet (USANDO DOIS SLIDERS)
// ------------------------
data class OrderFilters(
    val statuses: Set<PedidoStatus> = emptySet(),
    val minValue: Float? = null,
    val maxValue: Float? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet_DoubleSliders(
    initialFilters: OrderFilters = OrderFilters(),
    availableStatuses: List<PedidoStatus> = PedidoStatus.entries,
    minPossibleValue: Float = 0f,
    maxPossibleValue: Float = 200f,
    onApply: (OrderFilters) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedStatuses by remember { mutableStateOf(initialFilters.statuses.toMutableSet()) }
    var minSelected by remember { mutableFloatStateOf(initialFilters.minValue ?: minPossibleValue) }
    var maxSelected by remember { mutableFloatStateOf(initialFilters.maxValue ?: maxPossibleValue) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Filtrar pedidos", style = MaterialTheme.typography.titleLarge)
            TextButton(
                onClick = {
                    selectedStatuses = mutableSetOf()
                    minSelected = minPossibleValue
                    maxSelected = maxPossibleValue
                },
                modifier = Modifier.semantics { contentDescription = "Limpar filtros" },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary.copy(blue = 1.5f))
            ) {
                Text("Limpar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            availableStatuses.forEach { status ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (selectedStatuses.contains(status)) selectedStatuses.remove(status) else selectedStatuses.add(status)
                        }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedStatuses.contains(status),
                        onCheckedChange = { checked ->
                            if (checked) selectedStatuses.add(status) else selectedStatuses.remove(status)
                        },
                        modifier = Modifier.semantics { contentDescription = "Checkbox ${status.label}" }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = status.label, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Faixa de valor (R$)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(14.dp))

        // Slider para MIN
        FancyRangeSelectorOrderFilter(
            modifier = Modifier.fillMaxWidth(),
            priceStart = minSelected,
            priceEnd = maxSelected,
            onRangeChange = { start, end ->
                minSelected = start
                maxSelected = end
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.semantics { contentDescription = "Cancelar filtros" },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val filters = OrderFilters(
                        statuses = selectedStatuses.toSet(),
                        minValue = minSelected,
                        maxValue = maxSelected
                    )
                    onApply(filters)
                    onDismissRequest()
                },
                modifier = Modifier.semantics { contentDescription = "Aplicar filtros" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text("Aplicar")
            }
        }
    }
}

@Composable
fun FancyRangeSelectorOrderFilter(
    modifier: Modifier = Modifier,
    priceStart: Float,
    priceEnd: Float,
    onRangeChange: (Float, Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 0,
)  {
    // layout size em px
    var sizePx by remember { mutableStateOf(IntSize(0, 0)) }

    // fractions (0..1) dos thumbs (estado fonte)
    var startFrac by remember {
        mutableFloatStateOf(
            valueToFraction(priceStart, valueRange.start, valueRange.endInclusive)
        )
    }
    var endFrac by remember {
        mutableFloatStateOf(
            valueToFraction(priceEnd, valueRange.start, valueRange.endInclusive)
        )
    }

    // manter sincronia quando estado externo muda
    LaunchedEffect(priceStart, priceEnd) {
        startFrac = valueToFraction(priceStart, valueRange.start, valueRange.endInclusive)
        endFrac = valueToFraction(priceEnd, valueRange.start, valueRange.endInclusive)
    }

    // animações sutis para frações
    val animatedStartFrac by animateFloatAsState(targetValue = startFrac)
    val animatedEndFrac by animateFloatAsState(targetValue = endFrac)

    // constantes visuais em Dp -> Px (capturadas aqui)
    val density = LocalDensity.current
    val trackHeight = 6.dp
    val thumbRadius = 12.dp
    val trackStrokePx = with(density) { trackHeight.toPx() }
    val thumbPx = with(density) { thumbRadius.toPx() }
    val bubbleHeight = 34.dp
    val minGapBetweenBubblesPx = with(density) { 8.dp.toPx() } // gap visual mínimo entre bolhas

    // cores/brush capturados do tema (não usar dentro do draw lambda)
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val inactiveTrackColor = onSurfaceColor.copy(alpha = 0.08f)
    val activeGradient = Brush.horizontalGradient(colors = listOf(surfaceColor, secondaryColor))

    // usable width considerando thumbs (igual ao seu)
    val usableWidthPx = remember(sizePx.width, thumbPx) {
        (sizePx.width - thumbPx * 2f).coerceAtLeast(1f)
    }

    // medição das larguras das bolhas em px (para evitar overflow/overlap)
    var leftBubbleWidthPx by remember { mutableIntStateOf(0) }
    var rightBubbleWidthPx by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        // BOLHAS (labels) posicionadas com offsets calculados
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bubbleHeight)
        ) {
            // posição base em px (centro do thumb, igual ao usado no Canvas)
            val baseLeftCenterPx = (thumbPx + animatedStartFrac * usableWidthPx)
            val baseRightCenterPx = (thumbPx + animatedEndFrac * usableWidthPx)

            // calcular as posições da borda esquerda dos boxes a partir do centro desejado:
            // leftPos = center - width/2
            var leftPosPx = (baseLeftCenterPx - leftBubbleWidthPx / 2f)
            var rightPosPx = (baseRightCenterPx - rightBubbleWidthPx / 2f)

            // limites para a posição (a 'left pos' não deve ser < 0 e nem ultrapassar o container)
            val maxLeftPos = (sizePx.width - leftBubbleWidthPx).coerceAtLeast(0)
            val maxRightPos = (sizePx.width - rightBubbleWidthPx).coerceAtLeast(0)

            leftPosPx = leftPosPx.coerceIn(0f, maxLeftPos.toFloat())
            rightPosPx = rightPosPx.coerceIn(0f, maxRightPos.toFloat())

            // checar overlap entre boxes - usando bordas: se right edge da esquerda + gap > left edge da direita => overlap
            val leftEdge = leftPosPx + leftBubbleWidthPx
            val rightEdge = rightPosPx
            val overlap = (leftEdge + minGapBetweenBubblesPx) - rightEdge

            if (overlap > 0f) {
                // separar simetricamente: mover left para esquerda e right para direita pela metade do overlap
                val moveHalf = overlap / 2f
                var newLeft = (leftPosPx - moveHalf).coerceIn(0f, maxLeftPos.toFloat())
                var newRight = (rightPosPx + moveHalf).coerceIn(0f, maxRightPos.toFloat())

                // se ainda houver overlap (por causa de limites), tentar ajustar movendo o máximo possível
                val newLeftEdge = newLeft + leftBubbleWidthPx
                val newOverlap = (newLeftEdge + minGapBetweenBubblesPx) - newRight
                if (newOverlap > 0f) {
                    // quanto ainda precisa mover
                    val needed = newOverlap
                    val canMoveLeft = newLeft // quanto left pode ainda se mover para a esquerda
                    val canMoveRight = (maxRightPos - newRight) // quanto right pode se mover para a direita

                    if (canMoveLeft >= needed) {
                        newLeft = (newLeft - needed).coerceIn(0f, maxLeftPos.toFloat())
                    } else if (canMoveRight >= needed) {
                        newRight = (newRight + needed).coerceIn(0f, maxRightPos.toFloat())
                    } else {
                        // força separação colocando right logo após left
                        newRight = (newLeft + leftBubbleWidthPx + minGapBetweenBubblesPx).coerceIn(0f, maxRightPos.toFloat())
                        newLeft = (newRight - leftBubbleWidthPx - minGapBetweenBubblesPx).coerceIn(0f, maxLeftPos.toFloat())
                    }
                }

                leftPosPx = newLeft
                rightPosPx = newRight
            }

            // Left bubble — agora posicionamos pela borda esquerda calculada (sem padding extra)
            Box(
                modifier = Modifier
                    .offset { IntOffset(leftPosPx.roundToInt(), 0) }
                    .onSizeChanged { leftBubbleWidthPx = it.width }
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 4.dp,
                    modifier = Modifier.semantics {
                        contentDescription = "Preço mínimo R$${fractionToValue(animatedStartFrac, valueRange.start, valueRange.endInclusive).roundToInt()}"
                    }
                ) {
                    Text(
                        "R$${fractionToValue(animatedStartFrac, valueRange.start, valueRange.endInclusive).roundToInt()}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Right bubble
            Box(
                modifier = Modifier
                    .offset { IntOffset(rightPosPx.roundToInt(), 0) }
                    .onSizeChanged { rightBubbleWidthPx = it.width }
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 4.dp,
                    modifier = Modifier.semantics {
                        contentDescription = "Preço máximo R$${fractionToValue(animatedEndFrac, valueRange.start, valueRange.endInclusive).roundToInt()}"
                    }
                ) {
                    Text(
                        "R$${fractionToValue(animatedEndFrac, valueRange.start, valueRange.endInclusive).roundToInt()}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        // Track + thumbs desenhados em Canvas; detectDragGestures captura toques
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onSizeChanged { sizePx = it }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val x = change.position.x
                        val usable = usableWidthPx
                        val relative = ((x - thumbPx) / usable).coerceIn(0f, 1f)

                        val distToStart = abs(relative - startFrac)
                        val distToEnd = abs(relative - endFrac)
                        val minFracSeparation =
                            (max(with(density) { 24.dp.toPx() }, (sizePx.width * 0.04f))) / usable

                        if (distToStart <= distToEnd) {
                            val newStart = min(relative, endFrac - minFracSeparation)
                            startFrac = newStart.coerceIn(0f, 1f)
                        } else {
                            val newEnd = max(relative, startFrac + minFracSeparation)
                            endFrac = newEnd.coerceIn(0f, 1f)
                        }

                        val startVal =
                            fractionToValue(startFrac, valueRange.start, valueRange.endInclusive)
                        val endVal =
                            fractionToValue(endFrac, valueRange.start, valueRange.endInclusive)

                        if (steps > 0) {
                            val stepSize =
                                (valueRange.endInclusive - valueRange.start) / (steps + 1)
                            val snappedStart = (round(startVal / stepSize) * stepSize).coerceIn(
                                valueRange.start,
                                valueRange.endInclusive
                            )
                            val snappedEnd = (round(endVal / stepSize) * stepSize).coerceIn(
                                valueRange.start,
                                valueRange.endInclusive
                            )
                            onRangeChange(snappedStart, snappedEnd)
                        } else {
                            onRangeChange(startVal, endVal)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)) {
                val height = size.height
                val centerY = height / 2f

                val startX = thumbPx + animatedStartFrac * usableWidthPx
                val endX = thumbPx + animatedEndFrac * usableWidthPx

                // fundo do track
                drawRoundRect(
                    color = inactiveTrackColor,
                    topLeft = Offset(thumbPx, centerY - trackStrokePx / 2f),
                    size = Size(usableWidthPx, trackStrokePx),
                    cornerRadius = CornerRadius(trackStrokePx / 2f, trackStrokePx / 2f)
                )

                // track ativo com gradiente
                val activeLeft = startX
                val activeWidth = (endX - startX).coerceAtLeast(0f)
                drawRoundRect(
                    brush = activeGradient,
                    topLeft = Offset(activeLeft, centerY - trackStrokePx / 2f),
                    size = Size(activeWidth, trackStrokePx),
                    cornerRadius = CornerRadius(trackStrokePx / 2f, trackStrokePx / 2f)
                )

                // ticks discretos
                val tickCount = 5
                val tickHeight = 6f
                for (i in 0..tickCount) {
                    val fx = thumbPx + i * usableWidthPx / tickCount
                    drawLine(
                        color = onSurfaceColor.copy(alpha = 0.06f),
                        start = Offset(fx, centerY - tickHeight),
                        end = Offset(fx, centerY + tickHeight),
                        strokeWidth = 1f
                    )
                }

                // thumbs usando cores capturadas
                drawCircle(color = surfaceColor, radius = thumbPx, center = Offset(startX, centerY))
                drawCircle(
                    color = primaryColor,
                    radius = thumbPx - 4f,
                    center = Offset(startX, centerY)
                )
                drawCircle(color = onPrimaryColor, radius = 2f, center = Offset(startX, centerY))

                drawCircle(color = surfaceColor, radius = thumbPx, center = Offset(endX, centerY))
                drawCircle(
                    color = primaryColor,
                    radius = thumbPx - 4f,
                    center = Offset(endX, centerY)
                )
                drawCircle(color = onPrimaryColor, radius = 2f, center = Offset(endX, centerY))
            }
        }
    }
}
