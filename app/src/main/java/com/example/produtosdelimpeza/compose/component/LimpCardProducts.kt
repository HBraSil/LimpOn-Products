package com.example.produtosdelimpeza.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R

@Composable
fun LimpCardProducts(modifier: Modifier = Modifier, favorites: Boolean = false, onClickProduct: () -> Unit = {}) {

    Column(
        modifier = modifier
            .width(150.dp)
            .height(220.dp)
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
                text = "Sabão - 5 Litros ",
                modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                fontSize = 14.sp,
                fontWeight = Bold,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Sabão - 5 Litros ",
                modifier = Modifier.padding(top = 6.dp, start = 6.dp),
                fontSize = 18.sp
            )
        }

        //Spacer(Modifier.weight(1f))

        Text(
            text = "Lava roupas, lava louça",
            modifier = Modifier
                .padding(start = 6.dp, top = 4.dp)
                .align(Alignment.Start),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "R$ 25,00",
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        )
    }

}

@Preview
@Composable
private fun LimpCardProductsPreview() {
    LimpCardProducts()
}