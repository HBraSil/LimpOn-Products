package com.example.produtosdelimpeza.customer.catalog.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreProfileScreen(onBackNavigation: () -> Unit = {}) {
    val tabItems = listOf(
        "Informações",
        "Cupons",
        "Avaliações",
    )


    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState { tabItems.size }

        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.targetPage
        }

        CustomHeader(onBackClick = onBackNavigation, onShareClick = {}, onFavoriteClick = {})

        Text(
            text = "RA Produtos de Limpeza",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 20.dp)
        )

        AutoEllipsisText(
            text = "Somos da RA produtos de limpeza e temos como objetivo entregar o melhor em produtos de limpeza para sua casa."
        )

        Row(
            modifier = Modifier.padding(start = 20.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "4.7  • ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            RatingBarStars(
                rating = 4.3,
                modifier = Modifier.padding(end = 16.dp)
            )


            VerticalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .height(50.dp)
                    .padding(vertical = 16.dp)
            )

            Text(
                text = "479 avaliações",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.padding(top = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Black
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) {index ->
            when(index) {
                0 -> InfoTab()
                1 -> CouponsTab()
                2 -> FeedbackTab()
            }
        }
    }
}



@Composable
fun AutoEllipsisText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 1,
    fontSize: Int = 12,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var canExpand by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Box(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontSize = fontSize.sp,
            onTextLayout = { layoutResult ->
                textLayoutResult = layoutResult
                canExpand = layoutResult.hasVisualOverflow
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 60.dp, top = 6.dp)
                .fillMaxWidth()
                .clickable(enabled = canExpand) { isExpanded = !isExpanded }
        )
    }
}


@Composable
fun CustomHeader(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(80.dp) // altura do header
    ) {
        // Fundo com curva azul
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(width * 0.4f, 0f) // início do azul
                cubicTo(
                    width * 0.75f, 0f,   // controle 1
                    width * 0.7f, height, // controle 2
                    width, height * 0.88f   // fim da curva
                )
                lineTo(width, 0f)
                close()
            }

            drawPath(
                path = path,
                color = Color(0xFF0D47A1) // azul
            )
        }

        // Ícones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF0D47A1)
                )
            }

            Row {
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartilhar",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color.White
                    )
                }
            }
        }
    }
}



@Composable
fun RatingBarStars(
    rating: Double,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starSize: Dp = 14.dp,
    filledColor: Color = Color.Black,
    emptyColor: Color = Color.LightGray
) {
    Row(modifier = modifier) {
        for (i in 1..stars) {
            val starProgress = when {
                i <= rating.toInt() -> 1f // estrela cheia
                i == (rating.toInt() + 1) -> (rating % 1).toFloat() // fracionada
                else -> 0f // estrela vazia
            }

            Canvas(modifier = Modifier.size(starSize)) {
                // Caminho da estrela (pode ser desenhado ou substituído por ImageVector customizado)
                val path = Path().apply {
                    // aqui vamos desenhar uma estrela simples
                    moveTo(size.width / 2f, 0f)
                    lineTo(size.width * 0.62f, size.height * 0.38f)
                    lineTo(size.width, size.height * 0.38f)
                    lineTo(size.width * 0.7f, size.height * 0.62f)
                    lineTo(size.width * 0.82f, size.height)
                    lineTo(size.width / 2f, size.height * 0.76f)
                    lineTo(size.width * 0.18f, size.height)
                    lineTo(size.width * 0.3f, size.height * 0.62f)
                    lineTo(0f, size.height * 0.38f)
                    lineTo(size.width * 0.38f, size.height * 0.38f)
                    close()
                }

                // desenha estrela vazia
                drawPath(path, color = emptyColor)

                // desenha preenchimento parcial
                clipPath(path) {
                    drawRect(
                        color = filledColor,
                        size = Size(size.width * starProgress, size.height)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun StoreProfileScreenPreview() {
    StoreProfileScreen()
}