package com.example.produtosdelimpeza.compose.seller.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Pix
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R


@Composable
fun InfoTab() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        Address()
        OpeningHours()
        PaymentMethods()
    }
}


@Composable
fun Address() {
    Text(
        text = "Endereço",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
    val slotWidth = 56.dp
    Box(
        modifier = Modifier
            .width(slotWidth)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Quando showRightButton == true mostramos o botão
        AnimatedVisibility(
            visible = true,
            enter = expandVertically(tween(160)) + fadeIn(),
            exit = shrinkVertically(tween(120)) + fadeOut()
        ) {
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6A00FF))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "plus", tint = Color.White)
            }
        }
        // quando não visível, o Box continua ocupando largura, evitando movimento
    }
    // TODO: Adicionar mapa

}


@Composable
fun OpeningHours() {
    var rememberScrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

    var weekDays = listOf("Dom", "Seg", "Ter", "Quar", "Quin", "Sex", "Sáb")
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                },
        ) {
            Text(
                text = "Horário de funcionamento",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        ) {
            weekDays.forEach { day ->
                // TODO: Adicionar cor de fundo dinâmica
                Card(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    "Segunda - das 8 às 17"
                )
                Text(
                    "Terça - das 8 às 17"
                )
                Text(
                    "Quarta - das 8 às 17"
                )
                Text(
                    "Quinta - das 8 às 17"
                )
            }
        }
    }
}


@Composable
fun PaymentMethods() {

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Formas de pagamento",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Via aplicativo"
            )

            Spacer(Modifier.weight(1f))


            Image(
                painter = painterResource(R.drawable.mc_symbol_opt_73_2x),
                contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(40.dp),
            )
            Image(
                painter = painterResource(R.drawable.logo_blue_visa),
                contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(40.dp),
            )

            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF7F7F7), // fundo levemente cinza
                        shape = RoundedCornerShape(8.dp) // bordas arredondadas
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Pix, // ícone de dinheiro
                    contentDescription = "Dinheiro",
                    tint = Color(0xFF52BD9F), // Verde do próprio Pix
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Pix",
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Na entrega"
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF7F7F7), // fundo levemente cinza
                        shape = RoundedCornerShape(8.dp) // bordas arredondadas
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Payments, // ícone de dinheiro
                    contentDescription = "Dinheiro",
                    tint = Color(0xFF2E7D32), // verde dinheiro
                    modifier = Modifier.size(18.dp),

                    )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Dinheiro",
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF7F7F7), // fundo levemente cinza
                        shape = RoundedCornerShape(8.dp) // bordas arredondadas
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Pix, // ícone de dinheiro
                    contentDescription = "Dinheiro",
                    tint = Color(0xFF52BD9F), // Verde do próprio Pix
                    modifier = Modifier.size(18.dp),

                    )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Pix",
                    fontSize = 14.sp

                )
            }
        }
    }
}


@Preview
@Composable
private fun InfoTabPreview() {
    InfoTab()
}
