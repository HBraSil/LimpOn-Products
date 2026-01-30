package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.core.component.LimpOnRegistrationButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRegistrationScreen(
    onBackNavigation: () -> Unit = {},
    productRegistrationViewModel: ProductRegistrationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var isAvailable by remember { mutableStateOf(true) }

    val formState by productRegistrationViewModel._productFormState.collectAsState()
    val isValidToSave by productRegistrationViewModel._isValid.collectAsState()

    val state by productRegistrationViewModel.uiState.collectAsState()

    if (state.showSessionExpired) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {}) {
                    Text("OK")
                }
            },
            text = { Text("Sua sessão expirou. Faça login novamente.") }
        )
    }

    val context = LocalContext.current
    LaunchedEffect(state.showNoInternet) {
        if (state.showNoInternet) {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Novo Produto") },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Foto do Produto", style = MaterialTheme.typography.titleSmall)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { /* Abrir Galeria */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Adicionar imagem", color = MaterialTheme.colorScheme.primary)
                }
            }

            OutlinedTextField(
                value = formState.productName,
                onValueChange = { productRegistrationViewModel.onEvent(AddProductField.NameField(it)) },
                label = { Text("Nome do Produto*") },
                placeholder = { Text("Ex: Hambúrguer Artesanal") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = formState.productDescription,
                onValueChange = { productRegistrationViewModel.onEvent(AddProductField.ProductDescriptionField(it)) },
                label = { Text("Descrição detalhada*") },
                placeholder = { Text("Descreva ingredientes, tamanho, etc.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = formState.productPrice,
                    onValueChange = { productRegistrationViewModel.onEvent(AddProductField.PriceField(it)) },
                    label = { Text("Preço*") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                CategoryDropdown(
                    modifier = Modifier.weight(1.2f),
                    onCategorySelected = {  }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Disponível para venda", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "O produto aparecerá imediatamente no app",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isAvailable,
                    onCheckedChange = { isAvailable = it }
                )
            }
            PromotionSection(originalPrice = formState.promotionalPrice, onPriceChange = { productRegistrationViewModel.onEvent(AddProductField.PromotionalPriceField(it)) })

            Spacer(Modifier.height(16.dp))

            DietaryTagsSection(selectedTags = setOf("carne", "Lactício"), onTagToggle = { /* lógica de toggle */ })

            Spacer(Modifier.height(16.dp))

            InventorySection(sku = "Sodoku", onSkuChange = {}, quantity = "8*", onQuantityChange = {})

            Spacer(Modifier.height(16.dp))

            LimpOnRegistrationButton(
                text = "Salvar Produto",
                isValid = isValidToSave
            ) {
                productRegistrationViewModel.registerProduct(formState)
            }
        }
    }
}

@Composable
fun PromotionSection(
    originalPrice: String,
    onPriceChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalOffer, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Preço Promocional", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = originalPrice,
                onValueChange = onPriceChange,
                label = { Text("Valor com desconto") },
                prefix = { Text("R$ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent)
            )
        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietaryTagsSection(
    selectedTags: Set<String>,
    onTagToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tags = listOf("Vegano", "Vegetariano", "Sem Glúten", "Sem Lactose", "Zero Açúcar", "Picante")

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Classificação", style = MaterialTheme.typography.titleSmall)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                FilterChip(
                    selected = selectedTags.contains(tag),
                    onClick = { onTagToggle(tag) },
                    label = { Text(tag) },
                    leadingIcon = if (selectedTags.contains(tag)) {
                        { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                    } else null
                )
            }
        }
    }
}



@Composable
fun InventorySection(
    sku: String,
    onSkuChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = sku,
            onValueChange = onSkuChange,
            label = { Text("SKU / Código") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = onQuantityChange,
            label = { Text("Estoque") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(modifier: Modifier, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Lanches", "Bebidas", "Sobremesas", "Porções")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedText = category
                        expanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProductRegistration() {
    MaterialTheme {
        ProductRegistrationScreen()
    }
}
