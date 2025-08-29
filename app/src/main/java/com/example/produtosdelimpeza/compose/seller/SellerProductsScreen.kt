package com.example.produtosdelimpeza.compose.seller

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.component.LimpCardProducts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerProductsScreen(nameSeller: String = "", onBackNavigation: () -> Unit = {}) {

    val rememberScroll = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Aurubu", color = Black) },
                    navigationIcon = {
                        IconButton(onClick = { onBackNavigation() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.icon_navigate_back),
                            )
                        }
                    }
                )
            }
        ) {
            val produtos = List(10) { "Produto $it" }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(it),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Header principal
                item {
                    InformationCard(nameSeller)
                }

                // Favoritos (linha com 3 cards)
                item {
                    Column {

                        Text(
                            text = stringResource(R.string.favorites_products),
                            modifier = Modifier.padding(start = 20.dp).padding(vertical = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = Bold
                        )

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            items(3) { fav ->
                                LimpCardProducts(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(155.dp),
                                    favorites = true
                                )
                            }
                        }
                    }
                }

                // Todos os produtos (grid 2 colunas usando FlowRow)
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.all_products),
                            modifier = Modifier.padding(start = 20.dp).padding(vertical = 20.dp),
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
                                LimpCardProducts(
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
fun InformationCard(nameSeller: String, modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row {
            Card(
                onClick = {},
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 10.dp)
                    .size(100.dp),
                shape = CircleShape,
                elevation = CardDefaults.elevatedCardElevation(2.dp)
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
                contentDescription = stringResource(R.string.icon_navigate_back),
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
fun FavoritesProducts(modifier: Modifier = Modifier) {



    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(18.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {

    }
}



@Preview
@Composable
private fun SellerScreenPreview() {
    SellerProductsScreen()
}