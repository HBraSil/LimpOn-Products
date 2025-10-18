package com.example.produtosdelimpeza.compose.seller

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.component.LimpOnCardProducts
import com.example.produtosdelimpeza.compose.generic_components.AddAndSubButton
import com.example.produtosdelimpeza.model.CartProduct
import com.example.produtosdelimpeza.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerProductsScreen(
    cartViewModel: CartViewModel,
    nameSeller: String = "",
    onBackNavigation: () -> Unit = {},
    onClickCardSellerProfile: () -> Unit = {},
    onClickCartScreen: () -> Unit = {},
) {
    var expandableFavoriteState by remember { mutableStateOf(false) }
    var expandableFeaturedState by remember { mutableStateOf(true) }

    val totalQuantity by cartViewModel.totalQuantity.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    val cartIdxQuantity = remember { List(10) { 0 }.toMutableStateList() }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBackNavigation() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.icon_navigate_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            CartBottomBarScaffoldStyle(
                items = totalQuantity,
                total = totalPrice,
                onOpenCart = {
                    onClickCartScreen()
                }
            )
        },
        containerColor = Color.Transparent
    ) {contentPadding ->
        val sampleProducts = listOf(
            CartProduct(id = 1, name = "Sabão líquido 5 litros", price = 25.0, badges = listOf("Oferta", "5L", "Mais vendido"), quantity = cartIdxQuantity[0]),
            CartProduct(id = 2, name = "Desinfetante Floral 2L", price = 15.0, badges = listOf("Oferta", "5L", "Mais vendido"), quantity = cartIdxQuantity[1]),
            CartProduct(id = 3, name = "Detergente Neutro 500ml", price = 5.0, badges = listOf("Oferta", "5L", "Mais vendido"), quantity = cartIdxQuantity[2]),
            CartProduct(id = 4, name = "Álcool 70% 1L", price = 10.0, badges = listOf("Oferta", "5L", "Mais vendido"), quantity = cartIdxQuantity[3]),
            CartProduct(id = 5, name = "Amaciante Concentrado 1L", price = 18.0, quantity = cartIdxQuantity[4]),
            CartProduct(id = 6, name = "Sabão em pó 1kg", price = 12.0, quantity = cartIdxQuantity[5]),
            CartProduct(id = 7, name = "Esponja multiuso (pacote com 3)", price = 8.0, quantity = cartIdxQuantity[6]),
            CartProduct(id = 8, name = "Lustra móveis 500ml", price = 14.0, quantity = cartIdxQuantity[7]),
            CartProduct(id = 9, name = "Desengordurante 500ml", price = 9.0, quantity = cartIdxQuantity[8]),
            CartProduct(id = 10, name = "Limpa vidros 500ml", price = 7.0, quantity = cartIdxQuantity[9])
        )

        val favAndHighLightsOption = listOf(stringResource(R.string.favorites_products), stringResource(R.string.featured_products))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header principal
            item {
                InformationCard(nameSeller, onClickCardSellerProfile = onClickCardSellerProfile)
            }

            // FAVORITOS
            item {
                val primary = MaterialTheme.colorScheme.primary
                val surface = MaterialTheme.colorScheme.surface

                var selectedIndex by remember { mutableIntStateOf(0) }

                Row(
                    modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                ) {

                    favAndHighLightsOption.forEachIndexed { index, option ->
                        val selected = index == selectedIndex
                        val bg = if (selected) Brush.horizontalGradient(listOf(primary, primary.copy(alpha = 0.2f))) else Brush.linearGradient(listOf(surface, surface))
                        val contentColor = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .weight(1f) // distribute pill widths evenly
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedIndex = index
                                }
                                .background(bg)
                                .border(
                                    width = if (!selected) 0.dp else 1.dp,
                                    color = if (!selected) Color.Transparent else MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // small icon representing the tab (optional)
                                if (index == 0) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Favoritos",
                                        tint = contentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Destaques",
                                        tint = contentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = option,
                                    color = contentColor,
                                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Todos os produtos (grid 2 colunas usando FlowRow)
            item {
                Spacer(Modifier.height(20.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.all_products),
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                            )
                        }
                    }


                    FlowRow(
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(26.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
                    ) {
                        sampleProducts.forEachIndexed { index, product ->
                            val quantity = cartItems.firstOrNull { it.id == index }?.quantity ?: 0

                        LimpOnCardProducts(
                            modifier = Modifier
                                //.fillParentMaxWidth(0.48f)
                                .weight(1f)
                                .wrapContentHeight(),
                                product = product,
                                txtQuantity = quantity,
                                contentBackgroundColor = MaterialTheme.colorScheme.primary,
                                onClickProduct = {
                                    isSheetOpen = true
                                },
                                subOfProducts = { name, quantity, curPrice ->
                                    cartViewModel.deleteOrRemoveProduct(
                                        CartProduct(
                                            id = index,
                                            name = name,
                                            price = curPrice,
                                            quantity = quantity
                                        )
                                    )
                                },
                                sumOfProducts = { name, quantity, curPrice ->
                                    cartViewModel.addOrUpdateProduct(
                                        CartProduct(
                                            id = index,
                                            name = name,
                                            price = curPrice,
                                            quantity = quantity
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                isSheetOpen = false
            },
            sheetState = sheetState,
            dragHandle = null,
            modifier = Modifier.statusBarsPadding()
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    isSheetOpen = false
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color(0xFFD3D5DC)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    modifier = Modifier
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                    )
                },
                bottomBar = {
                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AddAndSubButton(
                                txtQuantity = 1
                            )

                            Spacer(Modifier.weight(1f))

                            Button(
                                onClick = {}
                            ) {
                                Text(
                                    text = "Adicionar ao carrinho"
                                )
                            }
                        }
                    }
                }
            ) { contentPadding ->
                val sampleProductsOfSpecificSeller = listOf(
                    CartProduct(id = 6, name = "Sabão em pó 1kg", price = 12.0),
                    CartProduct(id = 7, name = "Esponja multiuso (pacote com 3)", price = 8.0),
                    CartProduct(id = 8, name = "Lustra móveis 500ml", price = 14.0),
                    CartProduct(id = 9, name = "Desengordurante 500ml", price = 9.0),
                    CartProduct(id = 10, name = "Limpa vidros 500ml", price = 7.0)
                )

                var expanded by remember { mutableStateOf(false) }

                val rotation by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    animationSpec = tween(durationMillis = 250)
                )
                val rowScrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues = contentPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sabao_lava_roupa),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Detergente",
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "5kg - (R$ 5,00/kg)",
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = "R$ 25,00",
                        modifier = Modifier.padding(top = 16.dp, start = 26.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ver todas as formas de pagamento",
                            modifier = Modifier
                                .padding(start = 26.dp)
                                .clickable{},
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 26.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                color = Color(0xFFDCDBDB), // fundo levemente cinza
                            )
                    ) {

                        Row(
                            modifier = Modifier
                                .clickable {
                                    expanded = !expanded
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 12.dp)
                            )

                            Text(
                                text = "Descrição do produto",
                                modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 6.dp, end = 6.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .rotate(rotation)
                            )

                        }
                        AnimatedVisibility(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                            visible = expanded,
                            enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
                            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
                        ) {

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray
                            )

                            Text(
                                text = "Poder concentrado para a sua casa. O Sabão [Nome] penetra nas fibras, eliminando as manchas mais difíceis e a gordura, enquanto cuida dos seus tecidos/louças. Limpeza superior, rendimento máximo.",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                            )
                        }
                    }

                    Text(
                        text = "Mais produtos de RA produtos de limpeza",
                        modifier = Modifier.padding(bottom = 10.dp, start = 16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.DarkGray
                    )

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rowScrollState)
                            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        sampleProductsOfSpecificSeller.forEachIndexed { index, product ->
                            LimpOnCardProducts(
                                modifier = Modifier
                                    .width(150.dp)
                                    .wrapContentHeight()
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                product = product,
                                isProductScreen = false,
                                favorites = true,
                                onClickProduct = {}
                            )
                        }
                    }
                }
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
    // anima entrada/saída vertical
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
                .navigationBarsPadding(), // evita área da nav bar
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
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

                Button(onClick = onOpenCart) {
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
) {
    Card(
        onClick = onClickCardSellerProfile,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
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
                        contentDescription = stringResource(R.string.icon_navigate_back),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            Spacer(Modifier.weight(1f))


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
                    color = Color.White
                )

                Text(
                    text = "Tuntum-Ma",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }

            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Circle,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(top = 15.dp, end = 15.dp)
            )
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
}


@Composable
fun ExpandableCardProducts(
    modifier: Modifier = Modifier,
    title: Int,
    expanded: Boolean,
) {
    var expandedState by remember { mutableStateOf(expanded) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        onClick = {
            expandedState = !expandedState
        },
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 400,
                    easing = LinearOutSlowInEasing
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (!expandedState) MaterialTheme.colorScheme.primary else Color.Gray,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(title),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .padding(vertical = 20.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                modifier = Modifier.rotate(rotationState),
                onClick = { expandedState = !expandedState },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.icon_navigate_back),
                )
            }

        }
        if (expandedState) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(6) { fav ->
                    Column(
                        modifier = Modifier.width(120.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sabao_lava_roupa),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        TextButton(
                            onClick = {}
                        ) {
                            Text("Comprar novamente")
                        }
                    }
                }
            }
        }
    }
}