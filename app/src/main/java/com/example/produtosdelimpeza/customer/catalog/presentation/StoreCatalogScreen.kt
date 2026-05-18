package com.example.produtosdelimpeza.customer.catalog.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnCardProducts
import com.example.produtosdelimpeza.core.component.LimpOnSectionHeader
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.ui.formatter.currencyFormatter
import com.example.produtosdelimpeza.customer.cart.domain.CartItem
import com.example.produtosdelimpeza.customer.cart.presentation.CartUiEvent
import com.example.produtosdelimpeza.customer.cart.presentation.CartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    cartViewModel: CartViewModel,
    onBackNavigation: () -> Unit = {},
    onCardStoreProfileClick: () -> Unit = {},
    onCartScreenClick: () -> Unit = {},
    catalogViewModel: CatalogViewModel = hiltViewModel(),
) {
    val store by catalogViewModel.store.collectAsState()
    val allProducts by catalogViewModel.productList.collectAsState()
    val productDetail by catalogViewModel.productDetail.collectAsState()
    val cartList by cartViewModel.cartItemsList.collectAsState()
    val totalQtd by cartViewModel.cartQuantity.collectAsState(initial = 0)
    val totalPrice by cartViewModel.cartTotalPrice.collectAsState(initial = 0.0)


    CatalogScreenContent(
        store = store,
        cartList = cartList,
        totalQtd = totalQtd,
        totalPrice = totalPrice,
        allProducts = allProducts,
        productDetail = productDetail,
        onCartScreenClick = onCartScreenClick,
        onCardStoreProfileClick = onCardStoreProfileClick,
        onBackNavigation = onBackNavigation,
        updateProductDetail = {
            catalogViewModel.updateProductDetail(it)
        },
        onCatalogEvent = {
            cartViewModel.onCartEvent(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreenContent(
    store: Store = Store(),
    cartList: List<CartItem> = emptyList(),
    totalQtd: Int = 0,
    totalPrice: Double = 0.0,
    productDetail: Product = Product(),
    allProducts: List<Product> = emptyList(),
    onCardStoreProfileClick: () -> Unit = {},
    onCartScreenClick: () -> Unit = {},
    onBackNavigation: () -> Unit = {},
    updateProductDetail: (Product) -> Unit = {},
    onCatalogEvent: (CartUiEvent) -> Unit = {},
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetProductOpen by rememberSaveable { mutableStateOf(false) }
    var isSheetFilterOpen by rememberSaveable { mutableStateOf(false) }


    Scaffold(
        bottomBar = {
            CartBottomBarScaffoldStyle(
                items = totalQtd,
                total = totalPrice,
                onOpenCart = {
                    onCartScreenClick()
                }
            )
        },
        containerColor = Color.Transparent
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                InformationCard(store.name, onCardStoreProfileClick, onBackNavigation)
            }

            item { LimpOnSectionHeader(title = R.string.highlitghs) }
            item {
                StoreCatalogHighlights(
                    allProducts = allProducts
                )
            }

            item { LimpOnSectionHeader(title = R.string.all_products, icon = Icons.Default.FilterList) }
            item {
                ProductsCatalog(
                    allProducts = allProducts,
                    cartList = cartList,
                    onCatalogEvent = onCatalogEvent,
                    updateProductDetail = updateProductDetail,
                    isSheetProductOpen = { isSheetProductOpen = it }
                )
            }
        }
    }
    if (isSheetFilterOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetFilterOpen = false },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            FilterBottomSheetContent(
                onApply = { isSheetFilterOpen = false },
                onClear = { /* callback if needed */ },
            )
        }
    }


    if (isSheetProductOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetProductOpen = false },
            sheetState = sheetState,
            dragHandle = null,
            modifier = Modifier.statusBarsPadding()
        ) {
            ProductDetailScreen(
                productDetail = productDetail,
                store = store,
                allProducts = allProducts,
                isSheetProductOpen = { isSheetProductOpen = it }
            )
        }
    }
}

@Composable
fun ProductsCatalog(
    allProducts: List<Product> = emptyList(),
    cartList: List<CartItem> = emptyList(),
    onCatalogEvent: (CartUiEvent) -> Unit = {},
    updateProductDetail: (Product) -> Unit = {},
    isSheetProductOpen: (Boolean) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            allProducts.forEachIndexed { _, product ->
                val qtd = cartList.find { it.productId == product.id }

                LimpOnCardProducts(
                    modifier = Modifier
                        .fillMaxWidth(0.48f)
                        .wrapContentHeight(),
                    product = product,
                    txtQuantity = qtd?.quantity ?: 0,
                    subOfProducts = {
                        onCatalogEvent(CartUiEvent.DecreaseQuantity(product.id))
                    },
                    sumOfProducts = {
                        onCatalogEvent(CartUiEvent.AddProductToCart(product))
                    },
                    onClickProduct = {
                        isSheetProductOpen(true)
                        updateProductDetail(product)
                    }
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ProductCard(
    product: Product,
    //isFavorite: Boolean,
    onToggleFavorite: (String) -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    cardElevation: Dp = 6.dp,
) {
    Card(
        modifier = modifier
            .clickable { onClick(product.name) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // Imagem simulada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                Text(
                    text = "Imagem",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = { onToggleFavorite(product.name) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                    //.scale(favScale)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favoritar",
                        //tint = favColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${currencyFormatter.format(product.price)}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
fun StoreCatalogHighlights(
    allProducts: List<Product> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(allProducts) { product ->
                ProductCard(
                    product = product,
                    //isFavorite = favorites[product.id] == true,
                    onToggleFavorite = { },
                    onClick = { },
                    modifier = Modifier
                        .width(180.dp)
                )
            }
        }
    }
}


@Composable
fun CartBottomBarScaffoldStyle(
    items: Int,
    total: Double,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = items > 0,
        enter = slideInVertically(
            initialOffsetY = { it /* começa abaixo */ },
            animationSpec = tween(400)
        ) + fadeIn(animationSpec = tween(200)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(250)) + fadeOut(),
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ícone com badge
                BadgedBox(
                    badge = {
                        if (items > 0) Badge { Text("$items") }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrinho"
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Total: R$ ${"%.2f".format(total)}", fontWeight = FontWeight.Bold)
                    Text("$items item(s)", style = MaterialTheme.typography.bodySmall)
                }

                ElevatedButton(
                    onClick = onOpenCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Ver sacola")
                }
            }
        }
    }
}


@Composable
fun InformationCard(
    nameSeller: String,
    onClickCardSellerProfile: () -> Unit,
    onBackNavigation: () -> Unit = {},
) {
    val bannerHeight = 160
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        ) {
            Text(
                text = "Banner do vendedor",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackNavigation,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(0.6f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.background,
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favoritar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }


        Column(
            modifier = Modifier.padding(top = (bannerHeight / 1.8).dp),
            horizontalAlignment = Alignment.End
        ) {
            Card(
                onClick = onClickCardSellerProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row {
                    Card(
                        onClick = {},
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp, bottom = 10.dp)
                            .size(100.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.elevatedCardElevation(1.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.icon_navigation_back),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))


                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = nameSeller,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = "Tuntum-Ma",
                            fontSize = 13.sp,
                        )
                    }
                }

                Text(
                    text = "Avaliações: ★ 4,5 (40 avaliações)",
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    fontSize = 16.sp,
                )

                Text(
                    text = "Próximo dia em Gonçalves Dias - 23/05",
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 15.dp),
                    fontSize = 13.sp,
                )
            }

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = "favoritos deste vendedor",
                    color = MaterialTheme.colorScheme.secondary.copy(blue = 1f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreCatalogPreview() {
    CatalogScreenContent(
        allProducts = listOf(
            Product(
                id = "1",
                storeId = "store_001",
                name = "Wireless Mouse",
                price = 89.90,
                promotionalPrice = 69.90,
                description = "Ergonomic wireless mouse with silent clicks.",
                classification = "Electronics",
                category = "Accessories",
                stockCount = 25
            ),
            Product(
                id = "2",
                storeId = "store_001",
                name = "Mechanical Keyboard",
                price = 299.99,
                promotionalPrice = 249.99,
                description = "RGB mechanical keyboard with blue switches.",
                classification = "Electronics",
                category = "Accessories",
                stockCount = 12
            ),
            Product(
                id = "3",
                storeId = "store_002",
                name = "Gaming Headset",
                price = 199.90,
                promotionalPrice = 159.90,
                description = "Surround sound gaming headset with noise cancellation.",
                classification = "Electronics",
                category = "Audio",
                stockCount = 18
            ),
        )
    )
}