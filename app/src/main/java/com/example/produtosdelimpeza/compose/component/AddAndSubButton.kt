package com.example.produtosdelimpeza.compose.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.model.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAndSubButton(
    modifier: Modifier = Modifier,
    txtQuantity: Int = 0,
    productEntity: ProductEntity = ProductEntity(),
    subOfProducts: (String, Int, Double) -> Unit = {name, quantity, price ->},
    sumOfProducts: (String, Int, Double) -> Unit = {name, quantity, price ->},
) {
    val transition = updateTransition(targetState = txtQuantity, label = "qtyTransition")
    val scale by transition.animateFloat(label = "qtyScale") { 1.0f + (if (it > 0) 0.05f else 0f) }


    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            color = MaterialTheme.colorScheme.primary, // Nova cor para os ripples na subárvore
            rippleAlpha = RippleAlpha(
                pressedAlpha = 0.4f, // Alpha mais alto!
                focusedAlpha = 0.3f,
                draggedAlpha = 0.3f,
                hoveredAlpha = 0.2f
            )// Você também pode definir 'rippleAlpha' se precisar de controle sobre a transparência
        )
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = { subOfProducts(productEntity.name, txtQuantity, productEntity.price) },

            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Diminuir",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.width(4.dp))

            Text(
                text = txtQuantity.toString(),
                modifier = Modifier.scale(scale),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )

            Spacer(Modifier.width(4.dp))

            IconButton(onClick = { sumOfProducts(productEntity.name, txtQuantity, productEntity.price) }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Aumentar",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}