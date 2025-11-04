package com.example.produtosdelimpeza.compose.seller

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

// --- Dados fictícios para preview/demo
private val sampleCategories = listOf("Limpeza", "Mercado", "Bebidas", "Lanches", "Higiene")

/** Helpers de conversão */
private fun fractionToValue(frac: Float, min: Float, max: Float): Float =
    (min + frac.coerceIn(0f, 1f) * (max - min))

private fun valueToFraction(value: Float, min: Float, max: Float): Float =
    ((value - min) / (max - min)).coerceIn(0f, 1f)

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

    val activeFilters = remember(onlyDiscount, onlyFastDelivery, selectedCategories, minRating, selectedSort, priceStart, priceEnd) {
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
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Filtros", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        AnimatedVisibility(visible = activeFilters > 0) {
            Text("$activeFilters filtros ativos", color = MaterialTheme.colorScheme.surfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Apenas produtos com desconto", Modifier.weight(1f))
            Switch(checked = onlyDiscount, onCheckedChange = { onlyDiscount = it }, modifier = Modifier.semantics { contentDescription = "Apenas com desconto" })
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Somente entrega rápida", Modifier.weight(1f))

            Switch(checked = onlyFastDelivery, onCheckedChange = { onlyFastDelivery = it }, modifier = Modifier.semantics { contentDescription = "Somente entrega rápida" })
        }

        SortOptions(selectedSort = selectedSort, onSelect = { selectedSort = it })

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

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
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
        val options = listOf("Mais barato → Mais caro", "Mais caro → Mais barato", "Mais vendidos", "Mais avaliados")
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

    // fractions (0..1) dos thumbs
    var startFrac by remember { mutableFloatStateOf(valueToFraction(priceStart, valueRange.start, valueRange.endInclusive)) }
    var endFrac by remember { mutableFloatStateOf(valueToFraction(priceEnd, valueRange.start, valueRange.endInclusive)) }

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

    // usable width considerando thumbs
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
            // posição base em px (antes de aplicar clamps / separação)
            val baseLeftPx = (animatedStartFrac * usableWidthPx)
            val baseRightPx = (animatedEndFrac * usableWidthPx)

            // clamp para bordas: garante que bolhas fiquem dentro do usable width quando desenhadas
            val leftMax = (usableWidthPx - leftBubbleWidthPx.toFloat()).coerceAtLeast(0f)
            val rightMax = (usableWidthPx - rightBubbleWidthPx.toFloat()).coerceAtLeast(0f)

            var leftPosPx = baseLeftPx.coerceIn(0f, leftMax)
            var rightPosPx = baseRightPx.coerceIn(0f, rightMax)

            // corrigir sobreposição: se a borda direita da esquerda ultrapassa borda esquerda da direita,
            // empurra as bolhas para os lados mantendo gap mínimo.
            val leftEdge = leftPosPx + leftBubbleWidthPx
            val rightEdge = rightPosPx
            val overlap = (leftEdge + minGapBetweenBubblesPx) - rightEdge
            if (overlap > 0f) {
                // tenta mover left para a esquerda e right para a direita igualmente
                val moveHalf = overlap / 2f
                leftPosPx = (leftPosPx - moveHalf).coerceIn(0f, leftMax)
                rightPosPx = (rightPosPx + moveHalf).coerceIn(0f, rightMax)

                // se ainda houver overlap (por limites), forçar separação movendo conforme possível
                val newLeftEdge = leftPosPx + leftBubbleWidthPx
                val newOverlap = (newLeftEdge + minGapBetweenBubblesPx) - rightPosPx
                if (newOverlap > 0f) {
                    // prefer mover left mais para esquerda se possível, senão mover right para direita
                    val canMoveLeft = leftPosPx
                    val canMoveRight = rightMax - rightPosPx
                    val needed = newOverlap
                    if (canMoveLeft >= needed) {
                        leftPosPx = (leftPosPx - needed).coerceIn(0f, leftMax)
                    } else if (canMoveRight >= needed) {
                        rightPosPx = (rightPosPx + needed).coerceIn(0f, rightMax)
                    } else {
                        // último recurso: just align so right sits after left
                        rightPosPx = (leftPosPx + leftBubbleWidthPx + minGapBetweenBubblesPx).coerceIn(0f, rightMax)
                        leftPosPx = (rightPosPx - leftBubbleWidthPx - minGapBetweenBubblesPx).coerceIn(0f, leftMax)
                    }
                }
            }

            // Left bubble
            Box(
                modifier = Modifier
                    .offset { IntOffset(leftPosPx.roundToInt(), 0) }
                    .padding(start = with(density) { thumbRadius })
                    .wrapContentSize()
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
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Right bubble
            Box(
                modifier = Modifier
                    .offset { IntOffset(rightPosPx.roundToInt(), 0) }
                    .padding(start = with(density) { thumbRadius })
                    .wrapContentSize()
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
                        fontWeight = FontWeight.Medium,
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
                        val minFracSeparation = (max(with(density) { 24.dp.toPx() }, (sizePx.width * 0.04f))) / usable

                        if (distToStart <= distToEnd) {
                            val newStart = min(relative, endFrac - minFracSeparation)
                            startFrac = newStart.coerceIn(0f, 1f)
                        } else {
                            val newEnd = max(relative, startFrac + minFracSeparation)
                            endFrac = newEnd.coerceIn(0f, 1f)
                        }

                        val startVal = fractionToValue(startFrac, valueRange.start, valueRange.endInclusive)
                        val endVal = fractionToValue(endFrac, valueRange.start, valueRange.endInclusive)

                        if (steps > 0) {
                            val stepSize = (valueRange.endInclusive - valueRange.start) / (steps + 1)
                            val snappedStart = (round(startVal / stepSize) * stepSize).coerceIn(valueRange.start, valueRange.endInclusive)
                            val snappedEnd = (round(endVal / stepSize) * stepSize).coerceIn(valueRange.start, valueRange.endInclusive)
                            onRangeChange(snappedStart, snappedEnd)
                        } else {
                            onRangeChange(startVal, endVal)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
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
                drawCircle(color = primaryColor, radius = thumbPx - 4f, center = Offset(startX, centerY))
                drawCircle(color = onPrimaryColor, radius = 2f, center = Offset(startX, centerY))

                drawCircle(color = surfaceColor, radius = thumbPx, center = Offset(endX, centerY))
                drawCircle(color = primaryColor, radius = thumbPx - 4f, center = Offset(endX, centerY))
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
    onSelect: (Set<String>) -> Unit
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

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 8.dp)) {
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
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 6.dp)) {
            (1..5).forEach { star ->
                IconToggleButton(
                    checked = rating >= star,
                    onCheckedChange = { onRatingChange(star) },
                    modifier = Modifier.semantics { contentDescription = "Selecionar mínimo $star estrelas" }
                ) {
                    Icon(
                        imageVector = if (rating >= star) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null
                    )
                }
            }
        }
    }
}