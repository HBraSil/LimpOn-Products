package com.example.produtosdelimpeza.customer.cart.presentation

import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.AddAndSubButton
import com.example.produtosdelimpeza.core.component.ProductPrice
import com.example.produtosdelimpeza.core.ui.formatter.currencyFormatter
import com.example.produtosdelimpeza.customer.cart.domain.CartItem


@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit = {}
) {
    val cartList by cartViewModel.cartItemsList.collectAsState()
    val totalPrice by cartViewModel.cartTotalPrice.collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            CartTopBar(onBackNavigation) {
                cartViewModel.clearCart()
            }
        },
        bottomBar = { },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // item { HeaderNotification() } this composable goes here
            item {
                if (cartList.isEmpty()) {
                    EmptyCartScreen()
                } else {
                    CartItemSection(
                        items = cartList,
                        onRemoveItem = { item ->
                            cartViewModel.removeItem(item)
                        },
                        onAdd = {
                            cartViewModel.increaseQuantity(it)
                        },
                        onSub = {
                            cartViewModel.decreaseQuantity(it)
                        }
                    )
                }
            }
            item { UpsellSuggestionSection() }
            item { AddressSection() }
            item {
                ObservationField(
                    currentObservation = "Observation goes here",
                    onObservationChange = { newObs ->

                    }
                )
            }
            item { ValueSummarySection(totalPrice) }
            item { CheckoutButton(isEnabled = cartList.isNotEmpty()) }
        }
    }
}


@Composable
fun EmptyCartScreen() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_cart),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar(
    onBackNavigation: () -> Unit,
    cleanCart: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                "Meu Carrinho",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackNavigation() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
            }
        },
        actions = {
            Text(
                text = "Limpar",
                modifier = Modifier.padding(end = 16.dp).clickable {
                    cleanCart()
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(red = 1f)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/*
@Composable
fun HeaderNotification() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.AcUnit,
            contentDescription = "Refrigeração",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Itens refrigerados inclusos. Garanta a refrigeração na entrega.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
*/

@Composable
fun CartItemSection(
    items: List<CartItem>,
    onRemoveItem: (CartItem) -> Unit,
    onAdd: (CartItem) -> Unit,
    onSub: (String) -> Unit
) {
    Column(modifier = Modifier.padding(14.dp)) {
        items.forEachIndexed { index, item ->
            CartItemRow (
                item = item,
                onRemoveItem = { onRemoveItem(item) },
                onAdd = { onAdd(item) },
                onSub = { onSub(item.productId) }
            )
            if (index < items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            }
        }
    }
}



@Composable
fun CartItemRow(
    item: CartItem,
    onRemoveItem: () -> Unit,
    onAdd: () -> Unit,
    onSub: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0F7FA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Fastfood,
                contentDescription = item.name,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onRemoveItem,
                ){
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover Item",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }

            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductPrice(
                    item.totalPrice,
                    item.totalPromotionalPrice,
                )
                AddAndSubButton (
                    txtQuantity = item.quantity,
                    onAddProduct = onAdd,
                    onSubProduct = onSub
                )
            }
        }
    }
}

@Composable
fun UpsellSuggestionSection() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { /* Ir para a tela de sugestões */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocalOffer,
                contentDescription = "Sugestão",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Complemente seu pedido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    "Adicione uma sobremesa ou bebida!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Avançar",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}


@Composable
fun AddressSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Localização",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp ))
        Column(modifier = Modifier.weight(1f)) {
            Text("Entregar em:", style = MaterialTheme.typography.bodySmall)
            Text(
                "Rua das Palmeiras, 123 - Apto 401",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        TextButton(onClick = { /* Editar Endereço */ }) {
            Text(
                text = "Alterar",
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun ObservationField(currentObservation: String, onObservationChange: (String) -> Unit) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
        Text("Observações para o pedido", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = currentObservation,
            onValueChange = onObservationChange,
            placeholder = { Text("Ex: Tirar cebola, embalagem para presente...") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun ValueSummarySection(
    totalPrice: Double
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Resumo de Valores",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Subtotal
         /*   ValueRow("Subtotal dos itens:", state.subtotal)

            // Entrega
            ValueRow("Taxa de Entrega:", state.deliveryFee, isFree = state.deliveryFee == 0.0)

            // Desconto/Cupom
            if (state.coupon != null && state.coupon.isApplied) {
                ValueRow(
                    label = "Desconto (${state.coupon.code}):",
                    value = -state.discount, // Valor negativo para desconto
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
*/
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = currencyFormatter.format(totalPrice),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(blue = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Abrir tela de cupons */ }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ConfirmationNumber,
                        contentDescription = "Cupons",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Adicionar Cupom/Benefício",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Avançar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ValueRow(label: String, value: Double, color: Color? = null, isFree: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            if (isFree) "Grátis" else currencyFormatter.format(value),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (value < 0.0) FontWeight.SemiBold else FontWeight.Normal,
            color = color ?: MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CheckoutButton(
    isEnabled: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = { /* Ir para o pagamento */ },
            enabled = isEnabled,
            modifier = Modifier.width(250.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                "Ir para pagamento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

// --- 5. Preview ---

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0087B0), // Um verde vibrante de delivery
            secondary = Color(0xFFFFF422),
            surfaceVariant = Color(0xFFF1F1F1),
            primaryContainer = Color(0xFFE8F5E9), // Para a notificação
            onPrimaryContainer = Color(0xFF1B2C5E),
            error = Color(0xFFD32F2F),
        )
    ) {
        CartScreen()
    }
}