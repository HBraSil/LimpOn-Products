package com.example.produtosdelimpeza.store.registration_product.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.core.component.AppliesToCategorySelector
import com.example.produtosdelimpeza.core.component.LimpOnRegistrationButton
import com.example.produtosdelimpeza.core.component.SessionExpiredAlertDialog
import com.example.produtosdelimpeza.core.ui.util.asString
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRegistrationScreen(
    onBackNavigation: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    productRegistrationViewModel: ProductRegistrationViewModel = hiltViewModel()
) {
    val formState = productRegistrationViewModel.productFormState
    val state by productRegistrationViewModel.uiState.collectAsState()

    //val promotionalPrice by remember { mutableIntStateOf(0.0) }


    val context = LocalContext.current
    LaunchedEffect(state.showNoInternet) {
        if (state.showNoInternet) {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }
    if (state.showSessionExpired) {
        SessionExpiredAlertDialog{
            productRegistrationViewModel.signOut()
            onNavigateToLogin()
        }
    }

    Box {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Novo Produto") },
                    navigationIcon = {
                        IconButton(onClick = onBackNavigation) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text("Foto do Produto", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .width(180.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { /* Abrir Galeria */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text("Adicionar imagem", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                item {
                    val errorFormState = formState.nameField.error?.asString()

                    OutlinedTextField(
                        value = formState.nameField.field,
                        onValueChange = {
                            productRegistrationViewModel.updateProductName(it)
                        },
                        label = { Text("Nome do Produto*") },
                        placeholder = { Text("Ex: Hambúrguer Artesanal") },
                        isError = errorFormState != null,
                        supportingText = {
                            errorFormState?.let {
                                Text(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                item {
                    val errorFormState = formState.descriptionField.error?.asString()

                    OutlinedTextField(
                        value = formState.descriptionField.field,
                        onValueChange = {
                            productRegistrationViewModel.updateProductDescription(it)
                        },
                        label = { Text("Descrição detalhada*") },
                        placeholder = { Text("Descreva ingredientes, tamanho, etc.") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        isError = errorFormState != null,
                        supportingText = {
                            errorFormState?.let {
                                Text(it)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                item {
                    ProductPriceSection()
                }
                item {
                    AppliesToCategorySelector(
                        options = listOf(
                            "Todos os produtos",
                            "Bebidas",
                            "Combos",
                            "Lanches",
                            "Porções",
                            "Sobremesas"
                        ),
                        selectedOption = formState.categoryField.field,
                        onOptionSelected = {
                            productRegistrationViewModel.updateCategory(it)
                        }
                    )
                }
                item {
                    PromotionSection(
                        originalPrice = formState.promotionalPriceField.field,
                        onPriceChange = {
                            productRegistrationViewModel.updateProductPromotionalPrice(it)
                        }
                    )
                }
                item {
                    DietaryTagsSection(
                        selectedTags = formState.classificationField.field,
                        onTagToggle = {
                            productRegistrationViewModel.updateClassification(it)
                        }
                    )
                }
                item {
                    InventorySection(
                        sku = "Sodoku",
                        onSkuChange = {},
                        quantity = formState.stockCountField.field,
                        onQuantityChange = {
                            productRegistrationViewModel.updateStock(it)
                        }
                    )
                }
                item {
                    LimpOnRegistrationButton(
                        text = "Salvar Produto",
                        loading = state.isLoading,
                        isValid = formState.formIsValid
                    ) {
                        productRegistrationViewModel.registerProduct()
                    }
                }
            }
        }
    }
}



fun formatCurrency(rawInput: String): String {
    if (rawInput.isEmpty()) return "R$ 0,00"

    // Remove qualquer caractere que não seja dígito
    val digits = rawInput.replace(Regex("\\D"), "")
    if (digits.isEmpty()) return "R$ 0,00"

    // Converte para centavos (Double)
    val cents = digits.toDouble() / 100

    // Formata usando o padrão brasileiro
    val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return nf.format(cents)
}

@Composable
fun PriceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPromotional: Boolean = false,
    isOptional: Boolean = false
) {
    // Cores dinâmicas para diferenciar os campos
    val isEditing = value.isNotEmpty() && value != "0"
    val accentColor = when {
        isPromotional && isEditing -> Color(0xFF2E7D32) // Verde vivo se tiver valor
        isPromotional -> Color(0xFF757575) // Cinza se estiver vazio (opcional)
        else -> Color(0xFF1976D2) // Azul para o preço base
    }

    val backgroundColor = if (isPromotional && !isEditing) Color(0xFFF1F1F1) else Color.White

    Column(modifier = modifier.padding(vertical = 4.dp)) {
        // HEADER: Label + Tag Opcional
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isEditing || !isPromotional) Color.Black else Color.Gray
            )
            if (isOptional) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(Opcional)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
        }

        // CAMPO DE INPUT
        BasicTextField(
            value = value,
            onValueChange = { if (it.all { char -> char.isDigit() }) onValueChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(backgroundColor, RoundedCornerShape(12.dp))
                        .border(
                            width = if (isEditing) 2.dp else 1.dp,
                            color = accentColor.copy(alpha = if (isEditing) 1f else 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPromotional) Icons.Default.TagFaces else Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = formatCurrency(value),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isEditing || !isPromotional) Color.Black else Color.Gray
                            )
                        )
                        // O input real fica oculto, apenas capturando os cliques
                        Box(Modifier.height(0.dp)) { innerTextField() }
                    }
                }
            }
        )

        // FOOTER: Legenda explicativa
        if (isPromotional) {
            Text(
                text = if (isEditing) "Valor de venda com desconto" else "Deixe vazio se não houver oferta",
                style = MaterialTheme.typography.labelSmall,
                color = if (isEditing) accentColor else Color.Gray,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun ProductPriceSection() {
    var priceInput by remember { mutableStateOf("") }
    var promoInput by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Campo Preço Normal
        PriceInputField(
            label = "Preço Base",
            value = priceInput,
            onValueChange = { priceInput = it },
            modifier = Modifier.weight(1f)
        )

        // Campo Preço Promocional
        PriceInputField(
            label = "Valor promocional",
            value = promoInput,
            onValueChange = { promoInput = it },
            modifier = Modifier.weight(1f),
            isPromotional = true
        )
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
                Text("Preço Promocional (opcional)", style = MaterialTheme.typography.titleSmall)
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
        var showMessage by remember { mutableStateOf(false) }
        if (showMessage) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SuccessSnackbar(message = "Cupom criado com sucesso!")
            }
        }
    }
}



@Composable
fun CurrencyField(
    rawValue: String,
    onRawValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPromo: Boolean = false
) {
    val formatted = remember(rawValue) {
        formatCurrencyBR(rawValue)
    }

    val borderColor = if (isPromo) Color(0xFF66BB6A) else Color.LightGray
    val backgroundColor = if (isPromo) Color(0xFFF1F8E9) else Color(0xFFF5F5F5)

    Column(modifier = modifier) {

        // 🔤 Label com contexto
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            if (isPromo) {
                Text(
                    text = " (opcional)",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            value = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            ),
            onValueChange = { newValue ->
                val digits = newValue.text.filter { it.isDigit() }
                onRawValueChange(digits)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = if (isPromo) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isPromo) Color(0xFF2E7D32) else Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.5.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 💸 Ícone só no promo
                    if (isPromo) {
                        Text(
                            text = "💸",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = "R$",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    innerTextField()
                }
            }
        )
    }
}


fun formatCurrencyBR(raw: String): String {
    if (raw.isEmpty()) return "0,00"

    val number = raw.toLong()
    val value = number / 100.0

    val symbols = DecimalFormatSymbols(Locale("pt", "BR")).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }

    return DecimalFormat("#,##0.00", symbols).format(value)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietaryTagsSection(
    selectedTags: String,
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


@Composable
fun SuccessSnackbar(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
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
