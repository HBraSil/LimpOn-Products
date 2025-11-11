package com.example.produtosdelimpeza.compose.highlights

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.model.Product
import java.util.Locale
import kotlin.random.Random


fun generateMockProducts(count: Int = 10): List<Product> {
    val mockProducts = mutableListOf<Product>()
    for (i in 1..count) {
        val hasDiscount = i % 3 != 0 // 2/3 dos produtos têm desconto
        val isTopSeller = i % 4 == 0 // 1/4 dos produtos são mais vendidos

        val discount = if (hasDiscount) Random.nextInt(10, 50) else null
        val price = Random.nextDouble(50.0, 300.0)
        val oldPrice = if (discount != null) price / (1 - discount / 100.0) else null

        mockProducts.add(
            Product(
                id = i,
                name = "Produto Incrível $i",
                price = "%.2f".format(Locale.US, price).toDouble(),
                oldPrice = oldPrice?.let { "%.2f".format(Locale.US, it).toDouble() },
                discountPercent = discount,
                imageUrl = "mock_url_$i",
                isTopSeller = isTopSeller
            )
        )
    }
    return mockProducts
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
    //state: ScreenState<List<Product>>,
    onProductClick: (Int) -> Unit = {},
    onAddToCart: (Int) -> Unit = {},
    onGoBack: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val listProductMock: List<Product> = generateMockProducts(15)
    val state: ScreenState<List<Product>> = ScreenState.Success(listProductMock)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destaques da Loja") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar à tela anterior"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ação de pesquisa */ }) {
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
                is ScreenState.Loading -> {}// LoadingState()
                is ScreenState.Empty -> {}/*EmptyState(
                    message = currentStatus.message,
                    onActionClick = onRetry
                ) // Usando retry como ação principal para simplicidade*/
                is ScreenState.Error -> {}/*ErrorState(
                    message = currentStatus.message,
                    onRetry = onRetry
                )*/
                is ScreenState.Success -> {
                    val products = currentStatus.data
                    val discountedProducts = products.filter { it.discountPercent != null }
                    val topSellerProducts = products.filter { it.isTopSeller }


                    // Grid com cabeçalhos para as seções
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // --- SEÇÃO: PRODUTOS COM DESCONTO ---
                        if (discountedProducts.isNotEmpty()) {
                            items(discountedProducts, key = { it.id }) { product ->
                                ProductCardHighlightScreen(product, onProductClick, onAddToCart)
                            }
                        }

                        // Separador
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
    }
}


@Composable
fun ProductCardHighlightScreen(
    product: Product,
    onProductClick: (Int) -> Unit,
    onAddToCart: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onProductClick(product.id) },
                // Adicionando um pequeno efeito de escala ao pressionar
            )
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Área da Imagem e Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                // Mock Image - Use Coil/Glide aqui com o product.imageUrl
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0))
                ) {
                    // Texto mock para imagem
                    Text(
                        text = "Imagem do\nProduto",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }


                // Discount Badge
                product.discountPercent?.let { percent ->
                    DiscountBadge(
                        percent = percent,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }

            // Área de Informações
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))

                ProductPriceText(product = product)

                Spacer(modifier = Modifier.height(8.dp))

                // Botão de Ação
                ElevatedButton(
                    onClick = { onAddToCart(product.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .semantics { contentDescription = "Adicionar ${product.name} ao carrinho" },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar", fontSize = 12.sp)
                }
            }
        }
    }
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
fun ProductPriceText(product: Product) {
    val currentPrice = "R$ ${"%.2f".format(Locale.US, product.price)}"

    Text(
        text = buildAnnotatedString {
            if (product.oldPrice != null) {
                // Preço antigo riscado
                val oldPriceText = "R$ ${"%.2f".format(Locale.US, product.oldPrice)}"
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
                    color = if (product.discountPercent != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )
            append(currentPrice)
            pop()
        },
        modifier = Modifier.padding(top = 4.dp)
    )
}
