package com.example.produtosdelimpeza.compose.seller.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.ui.theme.GradientSignupMainText


@Composable
fun CouponsTab() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO: Implementar um botão de filtro
        Text(
            text = "Cupons disponíveis",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        DiscountTicketCardEvenOdd()
    }
}

@Composable
fun DiscountTicketCardEvenOdd(
    modifier: Modifier = Modifier,
    discountText: String = "12%",
    description: String = "Válidos para compra acima de R$ 100",
    code: String = "1234567890"
) {
    Box(modifier = modifier.padding(16.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val w = size.width
            val h = size.height
            val corner = 20.dp.toPx()
            val cutR = 18.dp.toPx()

            val rect = Rect(0f, 0f, w, h)

            val path = Path().apply {
                addRoundRect(RoundRect(rect, CornerRadius(corner, corner)))
                arcTo(
                    rect = Rect(
                        left = -cutR,              // começa antes da borda esquerda
                        top = h / 2f - cutR,
                        right = cutR,              // até o meio do círculo
                        bottom = h / 2f + cutR
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = true
                )
                lineTo(0f, h / 2f + cutR)

                // recorte da direita
                arcTo(
                    rect = Rect(
                        left = w - cutR,           // começa no meio do círculo encostado na borda
                        top = h / 2f - cutR,
                        right = w + cutR,          // passa um pouco pra fora da borda
                        bottom = h / 2f + cutR
                    ),
                    startAngleDegrees = -270f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = true
                )
                lineTo(w, h / 2f + cutR)
                fillType = PathFillType.EvenOdd
            }

            // gradiente horizontal
            val gradient = GradientSignupMainText

            drawPath(path = path, brush = gradient)

            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f), 0f)
            drawLine(
                color = Color.White,
                start = Offset(w - 160f, 0f), // posição do traço (ajuste 80f conforme o espaço desejado)
                end = Offset(w - 160f, h),
                strokeWidth = 4f,
                pathEffect = dashEffect
            )
        }

        // Conteúdo por cima do Canvas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 14.dp, bottom = 14.dp, start = 14.dp, end = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = discountText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(12) { i ->
                        Box(
                            modifier = Modifier
                                .width(if (i % 2 == 0) 3.dp else 1.5.dp)
                                .height(22.dp)
                                .background(Color.Black)
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }
                
                Spacer(Modifier.width(16.dp))

                Text(
                    text = "Cupom",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Blue.copy(0.7f),
                )
            }

            Text(
                text = description,
                modifier = Modifier.padding(start = 14.dp),
                fontSize = 12.sp,
                color = Color.White
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Simples "código de barras" fake com retângulos


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Ícone de relógio",
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Espaçamento entre ícone e texto
                    Text(
                        text = "Vence em 20 de outubro",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Espaçamento entre ícone e texto
                    Text(
                        text = "USAR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .shadow(
                                elevation = 0.5.dp,
                                shape = RoundedCornerShape(5.dp),
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .clickable{}
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun CouponsTabPreview() {
    CouponsTab()
}