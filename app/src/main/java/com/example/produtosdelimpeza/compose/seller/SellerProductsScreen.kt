package com.example.produtosdelimpeza.compose.seller

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.basescaffold.BaseScaffold
import com.example.produtosdelimpeza.compose.component.LimpOnCardProducts
import com.example.produtosdelimpeza.model.CartProduct
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerProductsScreen(
    nameSeller: String = "",
    onBackNavigation: () -> Unit = {},
    onClickCardSellerProfile: () -> Unit = {},
    onClickCartScreen: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(factory = CartViewModel.Factory),
) {
    var expandableFavoriteState by remember { mutableStateOf(false) }
    var expandableFeaturedState by remember { mutableStateOf(true) }

    var items by remember { mutableIntStateOf(0) }
    var price by remember { mutableDoubleStateOf(0.0) }

    val totalQuantity by cartViewModel.totalQuantity.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    BaseScaffold(
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
                }
            )
        },
        bottomBar = {
            CartBottomBarScaffoldStyle(
                items = totalQuantity    ,
                total = totalPrice,
                onOpenCart = {
                    onClickCartScreen()
                }
            )
        },
        containerColor = Color.Transparent
    ) {
        val produtos = listOf(
            CartProduct(id = 1, name = "Sabão líquido 5 litros", price = 25.0, quantity = 1),
            CartProduct(id = 2, name = "Desinfetante Floral 2L", price = 15.0, quantity = 1),
            CartProduct(id = 3, name = "Detergente Neutro 500ml", price = 5.0, quantity = 1),
            CartProduct(id = 4, name = "Álcool 70% 1L", price = 10.0, quantity = 1),
            CartProduct(id = 5, name = "Amaciante Concentrado 1L", price = 18.0, quantity = 1),
            CartProduct(id = 6, name = "Sabão em pó 1kg", price = 12.0, quantity = 1),
            CartProduct(id = 7, name = "Esponja multiuso (pacote com 3)", price = 8.0, quantity = 1),
            CartProduct(id = 8, name = "Lustra móveis 500ml", price = 14.0, quantity = 1),
            CartProduct(id = 9, name = "Desengordurante 500ml", price = 9.0, quantity = 1),
            CartProduct(id = 10, name = "Limpa vidros 500ml", price = 7.0, quantity = 1)
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Header principal
            item {
                InformationCard(nameSeller, onClickCardSellerProfile = onClickCardSellerProfile)
            }

            // FAVORITOS
            item {
                ExpandableCardProducts(
                    modifier = Modifier.padding(top = 20.dp),
                    title = R.string.favorites_products,
                    expanded = expandableFavoriteState,
                )
            }

            // DESTAQUES
            item {
                ExpandableCardProducts(
                    modifier = Modifier.padding(bottom = 20.dp),
                    title = R.string.featured_products,
                    expanded = expandableFeaturedState,
                )
            }

            // Todos os produtos (grid 2 colunas usando FlowRow)
            item {
                Column {
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        produtos.forEachIndexed { index, product ->
                            LimpOnCardProducts(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(270.dp),
                                product = product,
                                onClickProduct = { /*TODO*/ },
                                subOfProducts = { name, quantity, price ->
                                    cartViewModel.addOrUpdateProduct(
                                        CartProduct(
                                            name = name,
                                            price = price,
                                            quantity = quantity
                                        )
                                    )
                                },
                                sumOfProducts = { name, quantity, curPrice ->
                                    items++
                                    price += curPrice
                                    // os dois itens acima são utilizados para atualizar o composable do bottomBar


                                    cartViewModel.addOrUpdateProduct(
                                        CartProduct(
                                            id = index,
                                            name = name,
                                            price = price,
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
                    color = White
                )

                Text(
                    text = "Tuntum-Ma",
                    fontSize = 13.sp,
                    color = White
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
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 400,
                    easing = LinearOutSlowInEasing
                )
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

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
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(6) { fav ->
                    LimpOnCardProducts(
                        modifier = Modifier
                            .width(120.dp)
                            .height(155.dp),
                        product = CartProduct(
                            name = "Sabão líquido 5 litros",
                            price = 25.0,
                            quantity = 1
                        ),
                        favorites = true
                    )
                }
            }
        }

    }

}


@Preview
@Composable
private fun SellerScreenPreview() {
    SellerProductsScreen(onClickCardSellerProfile = {}, onClickCartScreen = {})
}