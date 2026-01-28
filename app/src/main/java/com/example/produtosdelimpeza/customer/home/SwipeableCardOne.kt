import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.keyframes
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.lang.Float.max
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex

data class CardItem(val id: Long, val color: Color, val drawableResId: Int)

@Preview
@Composable
fun SwipeableCardOne(modifier: Modifier = Modifier) {
    val items = remember {
        mutableStateListOf<CardItem>().apply {
            addAll(
                listOf(
                    CardItem(1L, Color(0xff90caf9), R.drawable.sabao_lava_roupa),
                    CardItem(2L, Color(0xfffafafa), R.drawable.chat_sample_banner),
                    CardItem(3L, Color(0xffef9a9a), R.drawable.highlight),
                    CardItem(5L, Color(0xff90caf9), R.drawable.sample_clean_products),
                    CardItem(3L, Color(0xffef9a9a), R.drawable.highlight),
                    CardItem(4L, Color(0xfffff59d), R.drawable.chat_sample_banner),
                ).reversed() // inverte a lista antes de addAll
            )
        }
    }

    val verticalSpacingPx = 10f
    val cardCount = items.size

    val topCompensationDp = ((cardCount - 1) * verticalSpacingPx).dp

    var userInteracted by remember { mutableStateOf(false) }

    val hintOffsetX = remember { Animatable(0f) }
    val hintOffset by remember { snapshotFlow { hintOffsetX.value } }.collectAsState(initial = 0f)  // NOVO: Observa mudanças para recomposição

    var lastSwipeSign by remember { mutableFloatStateOf(0f) }

    fun moveTopToBack(sign: Float) {
        if (items.size <= 1) return
        val first = items.removeAt(0)
        items.add(first)
        lastSwipeSign = sign
    }

    var isTopCardBeingSwapped by remember { mutableStateOf(false) }

    LaunchedEffect(isTopCardBeingSwapped) {
        if (isTopCardBeingSwapped) {
            delay(350)
        }
    }

    LaunchedEffect(key1 = userInteracted, key2 = items) {

        if (userInteracted) {
            hintOffsetX.snapTo(0.0f)
            return@LaunchedEffect
        }

        if (items.size <= 1) return@LaunchedEffect
        repeat(4) { // exemplo: 3 repetições
            if (!isActive) return@LaunchedEffect
            hintOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 800
                    0f at 0
                    60f at 600
                    0f at 0
                }
            )
            delay(2500L)
        }
        hintOffsetX.snapTo(0.0f)
    }
    Box(
        modifier = modifier
            .background(Color.White)
            .padding(top = topCompensationDp),
        contentAlignment = Alignment.Center
    ) {
        items.forEachIndexed { idx, item ->
            val zIndex = (items.size - idx).toFloat()
            val isLastCard = idx == items.size - 1
            val entrySign = if (isLastCard) lastSwipeSign else 0f

            val shouldBeRendered = !(isTopCardBeingSwapped && idx == 0)

            // Apenas renderiza se não for o card do topo durante a troca
            if (shouldBeRendered) {
                key(item.id) {
                    SwipeableCardHorizontalSubtle(
                        modifier = Modifier.zIndex(zIndex),
                        item = item,
                        order = idx,
                        ghostMoveOffset = hintOffset,
                        isTopCard = idx == 0,
                        lastSwipeSign = entrySign,
                        onMoveToBack = { sign ->
                            moveTopToBack(sign)
                        },
                        onUserInteraction = { userInteracted = true }
                    )
                }
            }
        }
    }
}



@Composable
fun SwipeableCardHorizontalSubtle(
    modifier: Modifier = Modifier,
    item: CardItem,
    order: Int,
    isTopCard: Boolean,
    ghostMoveOffset: Float,
    onUserInteraction: () -> Unit,
    lastSwipeSign: Float, // Recebe o sinal (ex: -1f para esquerda, 1f para direita)
    onMoveToBack: (sign: Float) -> Unit,
    onCardClick: () -> Unit = {}
) {
    val verticalSpacingPx = 10f // 10 pixels por card
    val scaleFactor = 0.02f

    val animatedScaleStack by animateFloatAsState(
        targetValue = 1f - order * scaleFactor,
        label = "stackScale"
    )

    val animatedYOffset by animateDpAsState(
        targetValue = (-order * verticalSpacingPx).dp,
        label = "stackYOffset"
    )
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val animatedScaleClick by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "clickScale"
    )

    val partialStopDp = 180.dp * 1.2f
    val partialStopPx = with(LocalDensity.current) { partialStopDp.toPx() }
    val entryOffsetX = remember { Animatable(0f) }

    val interactionModifier = if (isTopCard) {
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCardClick
            )
            .swipeToBackOne(
                onUserInteraction = onUserInteraction,
                onMoveToBack = onMoveToBack
            )  // NOVO: Passa o onUserInteract
    } else {
        Modifier
    }

    LaunchedEffect(key1 = lastSwipeSign) {
        // Se houver um swipe válido e não for o card do topo (que lida com o arrasto):
        if (lastSwipeSign != 0f && !isTopCard) {

            entryOffsetX.snapTo(lastSwipeSign * partialStopPx)

            entryOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(400)
            )
        } else {
            // O card sempre começa em 0 se não houver swipe
            entryOffsetX.snapTo(0f)
        }
    }

    val totalOffsetX = if (isTopCard) {
        ghostMoveOffset.roundToInt() // Usa o offset de arrasto/hint do topo
    } else {
        entryOffsetX.value.roundToInt() // Usa o offset de reentrada lateral
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(x = totalOffsetX, y = animatedYOffset.roundToPx())
            }
            .graphicsLayer {
                scaleX = animatedScaleStack * animatedScaleClick
                scaleY = animatedScaleStack * animatedScaleClick
            }

            .then(interactionModifier),
        contentAlignment = Alignment.CenterStart
    ) {
        SampleCard(
            backgroundColor = item.color,
            modifier = Modifier
                .width(280.dp)
                .height(180.dp),
            drawableResId = item.drawableResId,
            isPressed = isPressed
        )
    }
}

@Composable
fun SampleCard(
    backgroundColor: Color = Color.White,
    modifier: Modifier = Modifier,
    drawableResId: Int,
    isPressed: Boolean = false
) {
    // NOVO: Animação de opacidade para o overlay de "esbranquiçado"
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.5f else 0f, // 0.5f para semi-transparente
        animationSpec = tween(durationMillis = 150), // Animação rápida
        label = "overlayAlpha"
    )

    Card(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth(.8f),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(drawableResId),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = overlayAlpha))
            )
        }
    }
}

fun Modifier.swipeToBackOne(
    onUserInteraction: () -> Unit,
    onMoveToBack: (sign: Float) -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    val rotateZ = remember { Animatable(0f) }
    var topSide by remember { mutableStateOf(true) }
    var clearedHurdle by remember { mutableStateOf(false) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                offsetX.stop()
                rotateZ.stop()
                clearedHurdle = false
                val velocityTracker = VelocityTracker()


                onUserInteraction.invoke()  // NOVO: Inicia o "press" para animação de scale
                awaitPointerEventScope {

                    horizontalDrag(awaitFirstDown().id) { change ->
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        val verticalPosition = change.previousPosition.y

                        topSide = verticalPosition <= size.height / 2
                        val offsetXRatioFromMiddle = if (topSide) {
                            verticalPosition / (size.height / 2)
                        } else {
                            (size.height - verticalPosition) / (size.height / 2)
                        }
                        val rotationalOffset = max(1f, (1f - offsetXRatioFromMiddle) * 4f)
                        val rotationValue = if (horizontalDragOffset > 0) rotationalOffset else -rotationalOffset


                        launch {

                            offsetX.snapTo(horizontalDragOffset)
                            rotateZ.snapTo(rotationValue)
                        }

                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }

                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                if (targetOffsetX.absoluteValue <= size.width / 3) {
                    // Not enough velocity; Reset.
                    launch { offsetX.animateTo(targetValue = 0f, initialVelocity = velocity) }
                    launch { rotateZ.animateTo(targetValue = 0f, initialVelocity = velocity) }
                } else {
                    // Enough velocity to fling the card to the back
                    val subtleMoveDuration = 300
                    val sign = if (targetOffsetX > 0) 1f else -1f // Direção

                    val targetOffScreenX = sign * size.width * 1.1f


                    val animationJobs = listOf(
                        // ANIMAÇÃO DE ROTAÇÃO (Inclina e depois volta a 0)
                        launch {
                            rotateZ.animateTo(
                                targetValue = sign * 15f, // Mantém inclinado por um breve momento
                                animationSpec = keyframes {
                                    durationMillis = subtleMoveDuration
                                    sign * 15f at (subtleMoveDuration / 4)
                                }
                            )
                            rotateZ.snapTo(0f)
                        },

                        // ANIMAÇÃO DE TRANSLADAÇÃO (Recuo para o monte)
                        // FUNÇÃO swipeToBack: Bloco de código a ser substituído
                        launch {
                            offsetX.animateTo(
                                targetValue = targetOffScreenX,
                                initialVelocity = velocity,
                                animationSpec = tween(subtleMoveDuration)
                            ) {
                                /*if (abs(value) >= size.width && !clearedHurdle) {
                                    clearedHurdle = true
                                    onMoveToBack.invoke(sign)
                                }*/
                            }
                        }
                    )

                    animationJobs.joinAll()// termina a coroutine local para reiniciar pointer handling
                    onMoveToBack(sign)
                    clearedHurdle = true
                    return@coroutineScope
                }
            }
        }
    }
    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    .graphicsLayer {
        transformOrigin = TransformOrigin.Center
        rotationZ = rotateZ.value
    }
}

