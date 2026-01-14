package com.example.produtosdelimpeza.compose.seller.managment


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color


@Composable
fun CouponsTabContent() {
    var searchQuery by remember { mutableStateOf("") }


    val mockCoupons = listOf(
        Coupon("BEMVINDO20", "R$ 20,00 de desconto", "Ativo", "Vence em 25/01/2026", "Pedido mín. R$ 50"),
        Coupon("FRETEGRATIS", "Entrega Grátis", "Ativo", "Vence em 30/01/2026", "Válido para toda loja"),
        Coupon("NATAL2025", "15% de desconto", "Expirado", "Venceu em 25/12/2025", "Uso único"),
        Coupon("PROMO30", "R$ 30,00 de desconto", "Cancelado", "Cancelado em 05/01/2026", "Mínimo R$ 100")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)) {
            Text(
                text = "Cupons",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Cupons inativos são exibidos apenas por 30 dias",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredCoupons = mockCoupons.filter { it.code.contains(searchQuery, ignoreCase = true) }

            items(filteredCoupons) { coupon ->
                CouponCard(coupon)
            }
        }
    }
}

@Composable
fun CouponCard(coupon: Coupon) {
    val isActive = coupon.status == "Ativo"
    val cardAlpha = if (coupon.status == "Ativo") 1f else 0.5f


    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(if (isActive) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = coupon.code,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = coupon.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isActive) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
                Text(
                    text = coupon.conditions,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = coupon.expiration,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) Color(0xFF2E7D32) else Color.Red
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (!isActive) {
                    Surface(
                        color = Color.LightGray.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "INVÁLIDO",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

data class Coupon(
    val code: String,
    val description: String,
    val status: String,
    val expiration: String,
    val conditions: String
)


