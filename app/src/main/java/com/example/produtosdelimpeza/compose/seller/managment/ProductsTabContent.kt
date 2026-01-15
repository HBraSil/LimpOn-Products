package com.example.produtosdelimpeza.compose.seller.managment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ProductsTabContent(
    onNewProductClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    val mockProducts = listOf(
        Product("Hambúrguer Duplo", "R$ 35,00", 15, true),
        Product("Batata Frita G", "R$ 18,00", 0, false),
        Product("Refrigerante Lata", "R$ 6,00", 42, true)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(
                targetState = isSearchExpanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(600)) + expandHorizontally() togetherWith
                            fadeOut(animationSpec = tween(600)) + shrinkHorizontally()
                },
                modifier = Modifier.weight(1f),
                label = "SearchAnimation"
            ) { expanded ->
                if (expanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(text = "Buscar produto...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp), // Mais arredondado = mais moderno
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                isSearchExpanded = false
                                searchQuery = ""
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Fechar")
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Meus Produtos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = { isSearchExpanded = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                        }
                    }
                }
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = {}) {
                Text(text = "Ver todos os produtos")
            }

            Button(
                onClick = onNewProductClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text(text = "Novo")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val filteredList = mockProducts.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {},
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Lanchonete", style = MaterialTheme.typography.titleLarge)

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))
            }
            items(filteredList) { product ->
                ProductListItem(product)
            }


            item {
                Row(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .fillMaxWidth()
                        .clickable {},
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Remédio", style = MaterialTheme.typography.titleLarge)

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))
            }
            items(filteredList) { product ->
                ProductListItem(product)
            }
        }
    }
}


@Composable
fun ProductListItem(product: Product) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = product.name, fontWeight = FontWeight.Bold)
                    Text(text = product.price, color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (product.inStock) "Em estoque: ${product.stockCount}" else "Esgotado",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (product.inStock) Color(0xFF4CAF50) else Color.Red
                )
            }
        }
    }
}

data class Product(
    val name: String,
    val price: String,
    val stockCount: Int,
    val isActive: Boolean,
    val inStock: Boolean = stockCount > 0
)