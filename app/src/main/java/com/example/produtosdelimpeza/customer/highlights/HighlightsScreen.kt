package com.example.produtosdelimpeza.customer.highlights



import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import java.util.Locale
import kotlin.random.Random


fun generateMockProducts(count: Int = 10): List<ProductEntity> {
    val mockProductEntities = mutableListOf<ProductEntity>()
    for (i in 1..count) {
        val hasDiscount = i % 3 != 0

        val discount = if (hasDiscount) Random.nextInt(10, 50) else null
        val price = Random.nextDouble(50.0, 300.0)
        val oldPrice = if (discount != null) price / (1 - discount / 100.0) else null

        mockProductEntities.add(
            ProductEntity(
                id = i,
                name = "Produto Incrível $i",
                price = "%.2f".format(Locale.US, price).toDouble(),
                oldPrice = oldPrice?.let { "%.2f".format(Locale.US, it).toDouble() },
                discountPercent = discount,
            )
        )
    }
    return mockProductEntities
}

sealed class ScreenState<out T> {
    data object Loading : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Empty(val message: String) : ScreenState<Nothing>()
    data class Error(val message: String) : ScreenState<Nothing>()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightsScreen(
    //state: ScreenState<List<ProductEntity>>,
    onProductClick: (Int) -> Unit = {},
    onAddToCart: (Int) -> Unit = {},
    onBackNavigation: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val listProductEntityMock: List<ProductEntity> = generateMockProducts(15)

    /*Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destaques da Loja") },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar à tela anterior"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { *//* Ação de pesquisa *//* }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar ou Filtrar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        // Transição de estado para carregar a UI (Success/Empty/Error)
        Crossfade(
            targetState = state,
            animationSpec = tween(500),
            modifier = Modifier.padding(paddingValues)
        ) { currentStatus ->
            when (currentStatus) {
                is ScreenState.Loading -> {}
                is ScreenState.Empty -> {}*//*EmptyState(
                    message = currentStatus.message,
                    onActionClick = onRetry
                ) // Usando retry como ação principal para simplicidade*//*
                is ScreenState.Error -> {}*//*ErrorState(
                    message = currentStatus.message,
                    onRetry = onRetry
                )*//*
                is ScreenState.Success -> {
                    val products = currentStatus.data
                    val discountedProducts = products.filter { it.discountPercent != null }
                    val topSellerProducts = products.filter { it.isTopSeller }


                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (discountedProducts.isNotEmpty()) {
                            items(discountedProducts, key = { it.id }) { product ->
                                ProductCardHighlightScreen(product, onProductClick, onAddToCart)
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(16.dp))
                        }

                        // --- SEÇÃO: MAIS VENDIDOS ---
                        if (topSellerProducts.isNotEmpty()) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text("⭐ Mais Vendidos", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 8.dp))
                            }
                            items(topSellerProducts, key = { it.id }) { product ->
                                ProductCardHighlightScreen(product, onProductClick, onAddToCart)
                            }
                        }
                    }
                }
            }
        }
    }*/
}



@Composable
fun DiscountBadge(percent: Int, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        shape = RoundedCornerShape(topEnd = 8.dp, bottomStart = 8.dp),
        modifier = modifier
    ) {
        Text(
            text = "-${percent}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ProductPriceText(productEntity: ProductEntity) {
    val currentPrice = "R$ ${"%.2f".format(Locale.US, productEntity.price)}"

    Text(
        text = buildAnnotatedString {
            if (productEntity.oldPrice != null) {
                // Preço antigo riscado
                val oldPriceText = "R$ ${"%.2f".format(Locale.US, productEntity.oldPrice)}"
                pushStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                append(oldPriceText)
                pop()
                append(" ")
            }
            // Preço atual (destacado)
            pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (productEntity.discountPercent != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )
            append(currentPrice)
            pop()
        },
        modifier = Modifier.padding(top = 4.dp)
    )
}
