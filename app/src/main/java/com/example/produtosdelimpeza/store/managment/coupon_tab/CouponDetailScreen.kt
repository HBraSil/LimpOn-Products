package com.example.produtosdelimpeza.store.managment.coupon_tab


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

// --- MODELO DE DADOS ---
data class CouponData(
    val code: String,
    val value: String,
    val status: String,
    val expiration: String,
    val minOrder: String,
    val totalLimit: String,
    val usedCount: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailScreen(
    onBackNavigation: () -> Unit
) {
    val coupon = CouponData(
        code = "VERAO2025",
        value = "Frete Grátis",
        status = "Ativo",
        expiration = "Vencido em 10/01/2026",
        minOrder = "R$ 30,00",
        totalLimit = "50",
        usedCount = "50",
        description = "Promoção de Verão encerrada"
    )


    var showEditSheet by remember { mutableStateOf(false) }
    val isLive = coupon.status == "Ativo"
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detalhes do Cupom") },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isLive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else Color.LightGray.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = coupon.code,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(coupon.value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Status: ${coupon.status}", color = if (isLive) Color(0xFF2E7D32) else Color.Gray)
                }
            }

            Text("Regras e Condições", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CouponInfoRow(title = "Descrição", detail = coupon.description, icon = Icons.Default.Description)
                CouponInfoRow(title = "Pedido Mínimo", detail = coupon.minOrder, icon = Icons.Default.ShoppingBag)
                CouponInfoRow(title = "Validade", detail = coupon.expiration, icon = Icons.Default.Event)
            }

            CouponAdvancedRulesCard(
                isStackable = false,
                firstPurchaseOnly = true,
                usesPerCpf = "1 uso por CPF"
            )


            Text("Uso do Cupom", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CouponStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Resgatados",
                    value = coupon.usedCount,
                    total = coupon.totalLimit,
                    icon = Icons.Default.People
                )
                CouponStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Limite Total",
                    value = coupon.totalLimit,
                    icon = Icons.Default.ConfirmationNumber
                )
            }

            if (isLive) {
                ElevatedButton(
                    onClick = { showEditSheet = true },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ajustar Prazo ou Limite")
                }


                CancelCouponButton(
                    onCancelClick = {}
                )
            }
        }
    }

    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Ajustar Cupom", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Text(
                    "O código e o valor do desconto não podem ser editados após o lançamento.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = coupon.expiration.replace("Vence em ", ""),
                    onValueChange = {},
                    label = { Text("Nova Data de Vencimento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = coupon.totalLimit,
                    onValueChange = {},
                    label = { Text("Novo Limite de Resgates") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { showEditSheet = false },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirmar Alterações")
                }
            }
        }
    }
}

@Composable
fun CancelCouponButton(
    onCancelClick: () -> Unit
) {
    OutlinedButton(
        onClick = onCancelClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            Icons.Default.DeleteForever,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text("Cancelar cupom")
    }
}


@Composable
fun CouponInfoRow(title: String, detail: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(detail, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CouponStatCard(modifier: Modifier, label: String, value: String, total: String? = null, icon: ImageVector) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (total != null) {
                    Text("/$total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}


@Composable
fun CouponAdvancedRulesCard(
    isStackable: Boolean,
    firstPurchaseOnly: Boolean,
    usesPerCpf: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Regras adicionais imútáveis",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            CouponRuleRow(
                icon = Icons.Default.Layers,
                label = "Acumulável",
                value = if (isStackable) "Sim" else "Não"
            )

            CouponRuleRow(
                icon = Icons.Default.PersonOutline,
                label = "Primeira compra",
                value = if (firstPurchaseOnly) "Sim" else "Não"
            )

            CouponRuleRow(
                icon = Icons.Default.Badge,
                label = "Usos por CPF",
                value = usesPerCpf
            )
        }
    }
}


@Composable
private fun CouponRuleRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.surface
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}


// --- PREVIEW DA TELA DE DETALHES DO CUPOM ---
@Preview(showBackground = true, name = "Detalhes do Cupom - Ativo")
@Composable
fun PreviewCouponDetailActive() {
    val mockCoupon = CouponData(
        code = "GANHE20",
        value = "R$ 20,00 de desconto",
        status = "Ativo",
        expiration = "Vence em 25/01/2026",
        minOrder = "R$ 60,00",
        totalLimit = "100",
        usedCount = "45",
        description = "Válido para a primeira compra na loja"
    )

    // Aplicando um tema básico do Material3 para o Preview
    MaterialTheme {
        CouponDetailScreen(
            onBackNavigation = {}
        )
    }
}

@Preview(showBackground = true, name = "Detalhes do Cupom - Inativo")
@Composable
fun PreviewCouponDetailInactive() {
    val mockCoupon = CouponData(
        code = "VERAO2025",
        value = "Frete Grátis",
        status = "Expirado",
        expiration = "Vencido em 10/01/2026",
        minOrder = "R$ 30,00",
        totalLimit = "50",
        usedCount = "50",
        description = "Promoção de Verão encerrada"
    )

    MaterialTheme {
        CouponDetailScreen(
            onBackNavigation = {}
        )
    }
}

// --- PREVIEW DO CARD INDIVIDUAL (Para ajuste fino de UI) ---
@Preview(showBackground = true, name = "Card de Cupom Listagem")
@Composable
fun PreviewCouponCardItem() {
    val mockCoupon = Coupon(
        code = "PROMO10",
        description = "10% de desconto em todo cardápio",
        status = "Ativo",
        expiration = "Vence em 3 dias",
        conditions = "Pedido mínimo de R$ 40,00"
    )

    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            CouponCard(coupon = mockCoupon) {}
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Modal de Edição de Cupom")
@Composable
fun PreviewEditCouponBottomSheet() {
    val mockCoupon = CouponData(
        code = "GANHE20",
        value = "R$ 20,00 de desconto",
        status = "Ativo",
        expiration = "25/01/2026",
        minOrder = "R$ 60,00",
        totalLimit = "100",
        usedCount = "45",
        description = "Válido para a primeira compra"
    )

    MaterialTheme {
        // Simulando o conteúdo do BottomSheet dentro de uma Surface
        // já que o ModalBottomSheet real precisa de um Scaffold e estado de animação
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Ajustar Cupom",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Banner de Alerta que explicamos anteriormente
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "O código 'GANHE20' e o desconto de 'R$ 20,00' não podem ser alterados pois o cupom já está em uso.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                // Campo de Data (Editável)
                OutlinedTextField(
                    value = mockCoupon.expiration,
                    onValueChange = {},
                    label = { Text("Nova Data de Vencimento") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Event, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )

                // Campo de Limite (Editável)
                OutlinedTextField(
                    value = mockCoupon.totalLimit,
                    onValueChange = {},
                    label = { Text("Novo Limite de Resgates") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Salvar Alterações")
                }
            }
        }
    }
}