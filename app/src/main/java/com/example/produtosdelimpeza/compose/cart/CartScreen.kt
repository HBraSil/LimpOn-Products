package com.example.produtosdelimpeza.compose.cart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.model.CartProduct
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackNavigation: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(),
) {
    val allProducts by cartViewModel.cartItems.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Carrinho",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackNavigation,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            cartViewModel.clearCart()
                        }
                    ) {
                        Text(
                            text = "Limpar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            item {
                Text(
                    text = "Itens adicionados",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                )
            }


            itemsIndexed(allProducts) { index, product ->
                FeaturedProducts(
                    id = index,
                    name = product.name,
                    price = product.price,
                    quantity = product.quantity,
                    cartViewModel = cartViewModel
                )

                Spacer(
                    modifier = Modifier.padding(vertical = 10.dp),
                )
            }
        }
    }
}


@Composable
fun FeaturedProducts(
    id: Int,
    name: String,
    price: Double,
    quantity: Int,
    cartViewModel: CartViewModel,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        border = BorderStroke(
            width = 1.dp, // aumenta a espessura
            color = Color(0x3606284D) // azul de destaque
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(R.drawable.sabao_lava_roupa),
                contentDescription = stringResource(R.string.product),
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(100.dp)
            )
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "RA produtos de limpeza",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = price.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.weight(1f))

                    Surface(
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.LightGray),
                        color = Color.White,
                        shadowElevation = 0.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .height(40.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            // Botão de diminuir (com ícone de lixeira no caso de 1)
                            IconButton(
                                onClick = {
                                 cartViewModel.deleteOrRemoveProduct(
                                         CartProduct(
                                             id = id,
                                             name = name,
                                             price = price,
                                             quantity = quantity
                                         )
                                     )
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (quantity > 1) Icons.Default.Remove else Icons.Default.Delete,
                                    contentDescription = "Remover",
                                    tint = Color(0xFF007AFF) // azul
                                )
                            }

                            // Quantidade
                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            // Botão de aumentar
                            IconButton(
                                onClick = {
                                    cartViewModel.addOrUpdateProduct(
                                        CartProduct(
                                            id = id,
                                            name = name,
                                            price = price,
                                            quantity = quantity
                                        )
                                    )
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Aumentar",
                                    tint = Color(0xFF007AFF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun CartScreenPreview() {
    CartScreen()
}