package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.core.ui.formatter.currencyFormatter


@Composable
fun ProductPrice(
    price: Double,
    promotionalPrice: Double
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val hasPromotion = promotionalPrice > 0
        val finalPrice = if (hasPromotion) promotionalPrice else price

        val formattedFinalPrice = currencyFormatter.format(finalPrice)
        val formattedOriginalPrice = currencyFormatter.format(price)

        Text(
            text = formattedFinalPrice,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (hasPromotion) {
            Spacer(Modifier.width(4.dp))
            Text(
                text = formattedOriginalPrice,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.LineThrough,
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}