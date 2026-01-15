package com.example.produtosdelimpeza.compose.seller.managment.product_tab

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.invoke

data class ProductDetail(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stockCount: Int,
    val isActive: Boolean,
    val images: List<String>,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(onBackNavigation: () -> Unit) {
    val product = ProductDetail(
        id = "1",
        name = "Hambúrguer Artesanal X-Monster",
        description = "Pão brioche, dois blends de 160g, muito queijo cheddar, bacon crocante e molho especial da casa. Acompanha batatas rústicas.",
        price = 45.90,
        category = "Hambúrgueres",
        stockCount = 25,
        isActive = true,
        images = listOf()
    )

    var showEditBottomSheet by remember { mutableStateOf(false) }
    var currentProduct by remember { mutableStateOf(product) }
    val scrollState = rememberScrollState()

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {

                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        ), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Restaurant,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SmallFloatingActionButton(
                            onClick = { /* Deletar */ },
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        ) {
                            Icon(
                                Icons.Default.Delete, contentDescription = null
                            )
                        }

                        FloatingActionButton(
                            onClick =
                                { /* Adicionar */ },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                Icons.Default.AddAPhoto, contentDescription = null
                            )
                        }
                    }
                    IconButton(
                        onClick = onBackNavigation,
                        modifier = Modifier.padding(16.dp).clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                currentProduct.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                currentProduct.category,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "R$ ${ String.format("%.2f", currentProduct.price)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ProductStatusChips(
                        hasPromotion = true,
                        hasCoupon = false,
                        outOfStock = currentProduct.stockCount == 0,
                        isVisible = true
                    )

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )

                    ProductPerformanceRow(
                        soldAmount = 128,
                        revenue = "R$ 4.860,00"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )
                    SectionHeader(title = "Sobre o produto") {
                        showEditBottomSheet = true
                    }
                    Text(
                        currentProduct.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    SectionHeader(title = "Inventário") {
                        showEditBottomSheet = true
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "${currentProduct.stockCount} unidades em estoque",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ProductVisibilitySection(
                        isVisible = true,
                        onChange = { /* atualizar estado */ }
                    )

                }
            }
        }

        if (showEditBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showEditBottomSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp).padding(bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Editar Produto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = currentProduct.name,
                        onValueChange = {
                            currentProduct = currentProduct.copy(name = it)
                        },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = currentProduct.price.toString(),
                        onValueChange = {
                            currentProduct = currentProduct.copy(price = it.toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Preço") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = currentProduct.description,
                        onValueChange = {
                            currentProduct = currentProduct.copy(description = it)
                        },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Button(
                        onClick = {showEditBottomSheet = false},
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Salvar Alterações"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SectionHeader(title: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onEdit) { Text("Editar") }
    }
}

@Composable
fun ProductStatusChips(
    hasPromotion: Boolean,
    hasCoupon: Boolean,
    outOfStock: Boolean,
    isVisible: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasPromotion) StatusChip("Em promoção", Color(0xFF2E7D32))
        if (hasCoupon) StatusChip("Cupom ativo", Color(0xFF6A1B9A))
        if (outOfStock) StatusChip("Sem estoque", Color(0xFFC62828))
        if (!isVisible) StatusChip("Oculto", Color.Gray)
    }
}

@Composable
private fun StatusChip(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}


@Composable
fun ProductPerformanceRow(
    soldAmount: Int,
    revenue: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PerformanceItem("Vendidos", soldAmount.toString())
            Divider(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp)
            )
            PerformanceItem("Faturamento", revenue)
        }
    }
}

@Composable
private fun PerformanceItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun ProductVisibilitySection(
    isVisible: Boolean,
    onChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Visibilidade no app", fontWeight = FontWeight.Bold)
                Text(
                    text = if (isVisible) "Produto visível para clientes"
                    else "Produto oculto dos clientes",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = isVisible, onCheckedChange = onChange)
        }
    }
}