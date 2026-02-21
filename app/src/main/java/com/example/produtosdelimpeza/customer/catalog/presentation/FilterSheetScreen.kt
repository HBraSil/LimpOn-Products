package com.example.produtosdelimpeza.customer.catalog.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.math.roundToInt
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.theme.StarColor

// --- Dados fictícios para preview/demo
private val sampleCategories = listOf("Limpeza", "Mercado", "Bebidas", "Lanches", "Higiene")

/** Helpers de conversão */
internal fun valueToFraction(value: Float, start: Float, end: Float): Float {
    if (end - start == 0f) return 0f
    return ((value - start) / (end - start)).coerceIn(0f, 1f)
}

internal fun fractionToValue(frac: Float, start: Float, end: Float): Float {
    return (start + frac * (end - start)).coerceIn(start, end)
}
/**
 * Conteúdo do bottom sheet com todos os controles de filtragem.
 * Usa rememberSaveable para a maioria dos estados; faixa de preço salva dois floats.
 */
@Composable
fun FilterBottomSheetContent(
    onApply: () -> Unit,
    onClear: () -> Unit,
) {
    var onlyDiscount by rememberSaveable { mutableStateOf(false) }
    var onlyFastDelivery by rememberSaveable { mutableStateOf(false) }
    var selectedSort by rememberSaveable { mutableStateOf("Mais vendidos") }
    var priceStart by rememberSaveable { mutableFloatStateOf(0f) }      // salvo como Float
    var priceEnd by rememberSaveable { mutableFloatStateOf(100f) }      // salvo como Float
    var selectedCategories by rememberSaveable { mutableStateOf(setOf<String>()) }
    var minRating by rememberSaveable { mutableIntStateOf(0) }

    val activeFilters = remember(
        onlyDiscount,
        onlyFastDelivery,
        selectedCategories,
        minRating,
        selectedSort,
        priceStart,
        priceEnd
    ) {
        listOfNotNull(
            onlyDiscount.takeIf { it },
            onlyFastDelivery.takeIf { it },
            selectedCategories.takeIf { it.isNotEmpty() },
            minRating.takeIf { it > 0 },
            if (priceStart != 0f || priceEnd != 100f) "price" else null
        ).size
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filtros", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        AnimatedVisibility(visible = activeFilters > 0) {
            Text("$activeFilters filtros ativos", color = MaterialTheme.colorScheme.surfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Apenas produtos com desconto", Modifier.weight(1f))
            Switch(
                checked = onlyDiscount,
                onCheckedChange = { onlyDiscount = it },
                modifier = Modifier.semantics { contentDescription = "Apenas com desconto" })
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Somente entrega rápida", Modifier.weight(1f))

            Switch(
                checked = onlyFastDelivery,
                onCheckedChange = { onlyFastDelivery = it },
                modifier = Modifier.semantics { contentDescription = "Somente entrega rápida" })
        }

        SortOptions(selectedSort = selectedSort, onSelect = { selectedSort = it })


        Spacer(Modifier.height(1.dp))
        Text("Faixa de valor (R$)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        // Aqui usamos o FancyRangeSelector (custom)
        FancyRangeSelector(
            priceStart = priceStart,
            priceEnd = priceEnd,
            onRangeChange = { s, e ->
                priceStart = s.coerceAtLeast(0f)
                priceEnd = e.coerceAtMost(100f)
            },
            valueRange = 0f..100f,
            steps = 0,
            modifier = Modifier.fillMaxWidth()
        )

        CategoryChips(
            categories = sampleCategories,
            selected = selectedCategories,
            onSelect = { selectedCategories = it }
        )

        RatingSelector(rating = minRating, onRatingChange = { minRating = it })

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    onlyDiscount = false
                    onlyFastDelivery = false
                    selectedSort = "Mais vendidos"
                    priceStart = 0f
                    priceEnd = 100f
                    selectedCategories = emptySet()
                    minRating = 0
                    onClear()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            ) { Text("Limpar") }

            Button(
                onClick = onApply,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) { Text("Aplicar") }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

/** Ordenação simples com RadioButtons */
@Composable
private fun SortOptions(selectedSort: String, onSelect: (String) -> Unit) {
    Column {
        Text("Ordenar por", fontWeight = FontWeight.SemiBold)
        val options = listOf(
            "Mais barato → Mais caro",
            "Mais caro → Mais barato",
            "Mais vendidos",
            "Mais avaliados"
        )
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedSort == option,
                    onClick = { onSelect(option) },
                    modifier = Modifier.semantics { contentDescription = "Ordenar por $option" }
                )
                Spacer(Modifier.width(8.dp))
                Text(option)
            }
        }
    }
}

/**
 * FancyRangeSelector - Slider customizado desenhado em Canvas com duas "bolhas" acima.
 * Correções:
 * - evita que bolhas saiam da tela (clamp usando largura da bolha medida)
 * - evita sobreposição mantendo um gap mínimo entre bolhas quando próximos
 */
@Composable
fun FancyRangeSelector(
    modifier: Modifier = Modifier,
    priceStart: Float,
    priceEnd: Float,
    onRangeChange: (Float, Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 0,
) {
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
/** Chips de categorias com multi-seleção */
@Composable
private fun CategoryChips(
    categories: List<String>,
    selected: Set<String>,
    onSelect: (Set<String>) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Categorias", fontWeight = FontWeight.SemiBold)
            val allSelected = selected.size == categories.size
            TextButton(onClick = { onSelect(if (allSelected) emptySet() else categories.toSet()) }) {
                Text(
                    text = if (allSelected) "Limpar seleção" else "Selecionar tudo",
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = category in selected,
                    onClick = { onSelect(if (category in selected) selected - category else selected + category) },
                    label = { Text(category) },
                    modifier = Modifier.semantics { contentDescription = "Categoria $category" }
                )
            }
        }
    }
}

/** Seleção de avaliação mínima com estrelas */
@Composable
private fun RatingSelector(rating: Int, onRatingChange: (Int) -> Unit) {
    Column {
        Text("Avaliação mínima", fontWeight = FontWeight.SemiBold)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 6.dp)
        ) {
            (1..5).forEach { star ->
                IconToggleButton(
                    checked = rating >= star,
                    onCheckedChange = { onRatingChange(star) },
                    modifier = Modifier.semantics {
                        contentDescription = "Selecionar mínimo $star estrelas"
                    }
                ) {
                    Icon(
                        imageVector = if (rating >= star) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = stringResource(R.string.rating_star, star),
                        tint = if (rating >= star) StarColor else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}