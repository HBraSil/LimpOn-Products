package com.example.produtosdelimpeza.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.model.ProductEntity
import com.example.produtosdelimpeza.core.ui.formatter.toBrazilianCurrency

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LimpOnCardProducts(
    modifier: Modifier = Modifier,
    productEntity: ProductEntity = ProductEntity(),
    txtQuantity: Int = 0,
    isProductScreen: Boolean = true,
    onClickProduct: () -> Unit = {},
    subOfProducts: (String, Int, Double) -> Unit = { name, quantity, price -> },
    sumOfProducts: (String, Int, Double) -> Unit = { name, quantity, price -> },
) {
    Card(
        onClick = onClickProduct,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.wrapContentWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sabao_lava_roupa),
                contentDescription = "ProductEntity Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.align(Alignment.Center),
                alignment = Alignment.Center
            )


            IconButton(onClick = {},
                modifier = Modifier
                    .size(42.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        )
                    )
                )
        ) {
            Text(
                text = productEntity.name,
                modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                fontSize = 15.sp,
                maxLines = 1,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Lava roupas, lava louça",
                modifier = Modifier
                    .padding(start = 6.dp)
                    .align(Alignment.Start),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                color = Color.DarkGray,
                overflow = TextOverflow.Ellipsis,
            )


            if (productEntity.badges.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
                ) {
                    productEntity.badges.forEach { badge ->
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(50),
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                badge,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "R$ ${productEntity.price.toBrazilianCurrency()}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "unid.",
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            if (isProductScreen) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AddAndSubButton(
                        modifier = Modifier,
                        txtQuantity = txtQuantity,
                        productEntity = productEntity,
                        subOfProducts = subOfProducts,
                        sumOfProducts = sumOfProducts
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun LimpCardProductsPreview(modifier: Modifier = Modifier) {
    LimpOnCardProducts(
        productEntity = ProductEntity(
            name = "Sabão líquido 5 litros",
            price = 25.0,
            quantity = 1
        )
    )
}