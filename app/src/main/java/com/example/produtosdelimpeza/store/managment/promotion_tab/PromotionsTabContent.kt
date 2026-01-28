package com.example.produtosdelimpeza.store.managment.promotion_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen


data class PromotionUiModel(
    val id: String,
    val title: String,
    val category: String,
    val benefit: String,
    val expiresAt: String,
    val isActive: Boolean
)

val mockPromotions = listOf(
    PromotionUiModel(
        id = "1",
        title = "Promoção Hambúrguer",
        category = "Lanches",
        benefit = "20% OFF",
        expiresAt = "Até 25/01/2026",
        isActive = true
    ),
    PromotionUiModel(
        id = "2",
        title = "Semana das Bebidas",
        category = "Bebidas",
        benefit = "Leve 2 Pague 1",
        expiresAt = "Até 22/01/2026",
        isActive = true
    ),
    PromotionUiModel(
        id = "3",
        title = "Promoção Natal",
        category = "Geral",
        benefit = "15% OFF",
        expiresAt = "Encerrada em 25/12/2025",
        isActive = false
    )
)

@Composable
fun PromotionsTabContent(
    promotions: List<PromotionUiModel> = mockPromotions,
    onPromotionClick: (String) -> Unit,
    onNavigateToCreatePromotionScreenClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp).weight(1f)) {
                Text(
                    text = "Promoções",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Promoções inativas são exibidas apenas por 30 dias",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            Button(
                onClick = onNavigateToCreatePromotionScreenClick,
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

        Spacer(Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(promotions, key = { it.id }) { promotion ->
                PromotionCompactCard(
                    promotion = promotion,
                    onClick = { onPromotionClick(StoreScreen.PROMOTION_DETAIL.route) }
                )
            }
        }
    }
}

@Composable
fun PromotionCompactCard(
    promotion: PromotionUiModel,
    onClick: () -> Unit
) {
    val statusColor =
        if (promotion.isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline

    val cardAlpha = if (promotion.isActive) 1f else 0.5f


    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().alpha(cardAlpha),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                //.background(Color.Transparent)
                .height(IntrinsicSize.Min)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = promotion.title,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Categoria: ${promotion.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = promotion.expiresAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = promotion.benefit,
                style = MaterialTheme.typography.titleMedium,
                color = if (promotion.isActive)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}