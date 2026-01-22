package com.example.produtosdelimpeza.customer.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

// --- 1. Estrutura de Dados (Hardcoded) ---

data class CartItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String = "", // Placeholder para URL de imagem
)

data class DeliveryOption(
    val id: Int,
    val type: String,
    val time: String,
    val price: Double,
    val isTurbo: Boolean = false,
)

data class Coupon(
    val code: String,
    val description: String,
    val value: Double,
    val isApplied: Boolean,
)

data class CartState(
    val items: List<CartItem>,
    val deliveryOptions: List<DeliveryOption>,
    val selectedDeliveryId: Int,
    val coupon: Coupon?,
    val subtotal: Double,
    val discount: Double,
    val observations: String,
) {
    val deliveryFee: Double
        get() = deliveryOptions.find { it.id == selectedDeliveryId }?.price ?: 0.0

    val total: Double
        get() = subtotal + deliveryFee - discount
}

// --- 2. Dados de Exemplo ---

val mockItems = listOf(
    CartItem(1, "Combo Mega Burger King", "2x Whopper, 2x Batata Média, 2x Refrigerante", 68.90, 1),
    CartItem(2, "Porção de Feijão Tropeiro", "Com bacon e torresmo. Serve 2.", 29.90, 1),
    CartItem(3, "Cerveja IPA Artesanal (330ml)", "Gelada e 'congelada'", 18.50, 6)
)

val mockDeliveryOptions = listOf(
    DeliveryOption(1, "Turbo", "20 - 30 min", 9.99, isTurbo = true),
    DeliveryOption(2, "Rápida", "35 - 45 min", 5.99),
    DeliveryOption(3, "Padrão", "50 - 60 min", 2.99)
)

val mockCoupon = Coupon("DEZOFF", "10% OFF em pedidos acima de R$ 100", 12.00, true)

val initialCartState = CartState(
    items = mockItems,
    deliveryOptions = mockDeliveryOptions,
    selectedDeliveryId = 1,
    coupon = mockCoupon,
    subtotal = mockItems.sumOf { it.price * it.quantity },
    discount = mockCoupon.value,
    observations = "Sem cebola no Mega Combo, por favor."
)

// --- 3. Utilitários ---

val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

// --- 4. Composables ---

@Composable
fun CartScreen() {
    // Usamos o 'mutableStateOf' para simular a mudança de estado da UI,
    // como a seleção de entrega e observações.
    var cartState by remember { mutableStateOf(initialCartState) }

    Scaffold(
        topBar = { CartTopBar() },
        bottomBar = { },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CartItemSection(cartState.items) }
            item { UpsellSuggestionSection() }
            item { AddressSection() }
            item {
                ObservationField(
                    currentObservation = cartState.observations,
                    onObservationChange = { newObs ->
                        cartState = cartState.copy(observations = newObs)
                    }
                )
            }
            item { ValueSummarySection(cartState) }
            item {
                CheckoutButton()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar() {
    TopAppBar(
        title = {
            Text(
                "Meu Carrinho",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Ação de Voltar */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
            }
        },
        actions = {
            Text(
                text = "Limpar",
                modifier = Modifier.padding(end = 16.dp),
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


@Composable
fun CartItemSection(items: List<CartItem>) {
    Column(modifier = Modifier.padding(14.dp)) {
        items.forEachIndexed { index, item ->
            CartItemRow(item = item)
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
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Imagem/Ícone Placeholder
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

        // Nome e Descrição
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // Botão/Ícone de Remoção/Edição (inspirado no Rappi/Burger King)
                IconButton(
                    onClick = {}
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
                Text(
                    currencyFormatter.format(item.price * item.quantity),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { /* Diminuir */ }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Diminuir",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        "${item.quantity}",
                        modifier = Modifier.padding(horizontal = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { /* Aumentar */ }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aumentar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
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
fun ValueSummarySection(state: CartState) {
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
            ValueRow("Subtotal dos itens:", state.subtotal)

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

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            // Total
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
                    currencyFormatter.format(state.total),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(blue = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Seção de Cupons/Benefícios (inspirado no Zé/iFood)
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
fun CheckoutButton() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = { /* Ir para o pagamento */ },
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