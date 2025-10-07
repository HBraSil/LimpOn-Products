package com.example.produtosdelimpeza.compose.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.model.CartProduct

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LimpOnCardProducts(
    modifier: Modifier = Modifier,
    favorites: Boolean = false,
    product: CartProduct,
    onClickProduct: () -> Unit = {},
    sumOfProducts: (String, Int, Double) -> Unit = {name, quantity, price ->},
    subOfProducts: (String, Int, Double) -> Unit = {name, quantity, price ->}
) {

    var productName by remember { mutableStateOf("Sabão líquido 5 litros") }
    var qntProduct by remember { mutableIntStateOf(0) }
    var showLeftBtn by remember { mutableStateOf(false) }
    var productPrice by remember { mutableDoubleStateOf(25.0) }
    val leftAlpha by animateFloatAsState(
        targetValue = if (showLeftBtn) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200, // duração da animação em milissegundos
            easing = LinearEasing // como a velocidade se comporta (linear, acelerando, desacelerando)
        )
    )


    Column(
        modifier = modifier
            .width(150.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .clickable { onClickProduct() },
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.highlight),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        if (favorites) {
            Text(
                text = productName,
                modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = product.name,
                modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                fontSize = 18.sp
            )
        }


        Text(
            text = "Lava roupas, lava louça",
            modifier = Modifier
                .padding(start = 6.dp)
                .align(Alignment.Start),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = product.price.toString(),
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 10.dp, end = 10.dp, top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                onClick = {
                    qntProduct--
                    if (qntProduct < 1) showLeftBtn = false
                    subOfProducts(productName, qntProduct, productPrice)
                },
                enabled = showLeftBtn,
                modifier = Modifier
                    .width(35.dp)
                    .height(30.dp)
                    .alpha(leftAlpha),
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    bottomStart = 6.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null
                )
            }

            Box(
                modifier = modifier
                    .width(20.dp)
                    .height(15.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = product.quantity.toString()
                )
            }

            Card(
                onClick = {
                    qntProduct++
                    showLeftBtn = true
                    sumOfProducts(productName, qntProduct, productPrice)
                },
                modifier = Modifier
                    .width(35.dp)
                    .height(30.dp),
                shape = RoundedCornerShape(
                    topEnd = 6.dp,
                    bottomEnd = 6.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun LimpCardProductsPreview() {
    LimpOnCardProducts(
        product = CartProduct(
            name = "Sabão líquido 5 litros",
            price = 25.0,
            quantity = 1
        )
    )
}