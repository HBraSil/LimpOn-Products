package com.example.produtosdelimpeza.compose.seller

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.component.LimpOnCardProducts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerProductsScreen(
    nameSeller: String = "",
    onBackNavigation: () -> Unit = {},
    onClickCardSellerProfile: () -> Unit,
) {
    var expandableFavoriteState by remember { mutableStateOf(false) }
    var expandableFeaturedState by remember { mutableStateOf(true) }


    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
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
            containerColor = Color.Transparent
        ) {
            val produtos = List(10) { "Produto $it" }

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
                        Text(
                            text = stringResource(R.string.all_products),
                            modifier = Modifier
                                .padding(start = 20.dp, bottom = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = Bold
                        )
                        FlowRow(
                            maxItemsInEachRow = 2,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            produtos.forEach { produto ->
                                LimpOnCardProducts(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(240.dp)
                                ) {
                                    // TODO: Navegar para a tela de detalhes do produto
                                }
                            }
                        }
                    }
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
                    fontWeight = Bold,
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
                fontWeight = Bold
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
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(6) { fav ->
                    LimpOnCardProducts(
                        modifier = Modifier
                            .width(120.dp)
                            .height(155.dp),
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
    SellerProductsScreen(onClickCardSellerProfile = {})
}