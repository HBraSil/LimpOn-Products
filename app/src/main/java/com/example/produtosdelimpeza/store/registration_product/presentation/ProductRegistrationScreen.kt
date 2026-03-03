package com.example.produtosdelimpeza.store.registration_product.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.AppliesToCategorySelector
import com.example.produtosdelimpeza.core.component.LimpOnRegistrationButton
import com.example.produtosdelimpeza.core.component.SessionExpiredAlertDialog
import com.example.produtosdelimpeza.core.ui.formatter.currencyFormatter
import com.example.produtosdelimpeza.core.ui.util.asString
import com.example.produtosdelimpeza.store.component.SuccessRegistrationOverlay
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRegistrationScreen(
    onBackNavigation: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    productRegistrationViewModel: ProductRegistrationViewModel = hiltViewModel()
) {
    val formState = productRegistrationViewModel.productFormState
    val uiState by productRegistrationViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val context = LocalContext.current
    LaunchedEffect(uiState.showNoInternet) {
        if (uiState.showNoInternet) {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }
    if (uiState.showSessionExpired) {
        SessionExpiredAlertDialog{
            productRegistrationViewModel.signOut()
            onNavigateToLogin()
        }
    }


    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            delay(1500)
            listState.animateScrollToItem(0)
            productRegistrationViewModel.reset()
        }
    }

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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    DietaryTagsSection(
                        selectedTags = formState.classificationField.field,
                        onTagToggle = {
                            productRegistrationViewModel.updateClassification(it)
                        }
                    )
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        PriceInputField(
                            label = "Preço Base",
                            value = formState.priceField.field,
                            isError = formState.priceField.error != null,
                            errorMessage = formState.priceField.error?.asString(),
                            onValueChange = {
                                productRegistrationViewModel.updateProductPrice(it)
                            },
                        )

                        PriceInputField(
                            label = "Valor promocional",
                            value = formState.promotionalPriceField.field,
                            onValueChange = {
                                productRegistrationViewModel.updateProductPromotionalPrice(it)
                            },
                            isPromotional = true
                        )
                    }
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
                        loading = uiState.isLoading,
                        isValid = formState.formIsValid
                    ) {
                        productRegistrationViewModel.registerProduct()
                    }
                }

            }

            SuccessRegistrationOverlay(
                message = stringResource(R.string.product_created),
                uiState.success
            )
        }
    }
}



@Composable
fun PriceInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    isPromotional: Boolean = false,
) {

    var fieldValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val formatted = currencyFormatter.format(
        (value.toDoubleOrNull() ?: 0.0) / 100
    )

    LaunchedEffect(formatted) {
        fieldValue = TextFieldValue(
            text = formatted,
            selection = TextRange(formatted.length)
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp, start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style =
                    if (!isPromotional)
                        MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    else
                        MaterialTheme.typography.labelMedium,
                color = if (!isPromotional) Color.Black else Color.Gray
            )
            if (isPromotional) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(Opcional)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
        }

        OutlinedTextField(
            value = fieldValue,
            onValueChange = { input ->
                val digits = input.text.filter { it.isDigit() }.take(12)
                onValueChange(digits)
            },
            label = { Text("Digite o valor") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            supportingText = {
                errorMessage?.let {
                    Text(it)
                }
            },
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp),
            shape = CircleShape,
        )

        if (isPromotional) {
            Text(
                text = "Valor do produto com desconto",
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 40.dp)
            )
        }
    }
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
            label = { Text("Estoque (opcional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProductRegistration() {
    MaterialTheme {
        ProductRegistrationScreen()
    }
}
