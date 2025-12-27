package com.example.produtosdelimpeza.compose.user.profile.address


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


private const val ANIMATION_DURATION = 300
private val ANIMATION_EASING = FastOutSlowInEasing

data class Address(
    val id: Int,
    val label: String,
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zip: String,
    var isDefault: Boolean,
    val distance: String? = null,
) {
    val fullAddress: String
        get() = "$street, $number${complement?.let { ", $it" } ?: ""}"

    val cityStateZip: String
        get() = "$neighborhood, $city - $state, $zip"

    val icon: ImageVector
        get() = when (label) {
            "Casa" -> Icons.Default.Home
            "Trabalho" -> Icons.Default.Work
            else -> Icons.Default.LocationOn
        }
}

object AddressDataSource {
    val predefinedLabels = listOf("Casa", "Trabalho", "Outro")

    val sampleAddresses = listOf(
        Address(
            id = 1,
            label = "Casa",
            street = "Rua das Flores",
            number = "123",
            complement = "Apto 101",
            neighborhood = "Jardim Botânico",
            city = "São Paulo",
            state = "SP",
            zip = "01234-567",
            isDefault = true,
            distance = "2.5 km"
        ),
        Address(
            id = 2,
            label = "Trabalho",
            street = "Avenida Paulista",
            number = "1000",
            complement = "Andar 5",
            neighborhood = "Bela Vista",
            city = "São Paulo",
            state = "SP",
            zip = "09876-543",
            isDefault = false,
            distance = "15 km"
        ),
        Address(
            id = 3,
            label = "Outro",
            street = "Rua da Praia",
            number = "50",
            complement = null,
            neighborhood = "Centro",
            city = "Santos",
            state = "SP",
            zip = "11000-000",
            isDefault = false,
            distance = "80 km"
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesScreen() {

    var selectedAddressId by remember { mutableIntStateOf(1) }
    // Estado Local da Lista de Endereços
    val addresses = remember {
        mutableStateListOf<Address>().apply { addAll(AddressDataSource.sampleAddresses) }
    }


    var showFormSheet by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<Address?>(null) }

    var addressToDelete by remember { mutableStateOf<Address?>(null) }

    // Lógica de manipulação de endereços
    val handleSaveAddress: (Address) -> Unit = { newAddress ->
        if (newAddress.isDefault) {
            addresses.forEach { it.isDefault = false }
        }
        val index = addresses.indexOfFirst { it.id == newAddress.id }
        if (index != -1) {
            addresses[index] = newAddress
        } else {
            addresses.add(newAddress)
        }
        showFormSheet = false
    }


    val handleToggleDefault: (Address) -> Unit = { selectedAddress ->
        for (i in addresses.indices) {
            val current = addresses[i]
            // somente cria novo objeto (copy) se precisar mudar o isDefault — evita substituições desnecessárias
            if (current.isDefault != (current.id == selectedAddress.id)) {
                addresses[i] = current.copy(isDefault = current.id == selectedAddress.id)
            }
        }
    }

    val handleDeleteAddress: (Address) -> Unit = { address ->
        addresses.remove(address)
        addressToDelete = null
        // Se o endereço removido era o padrão, define o primeiro restante como padrão
        if (address.isDefault && addresses.isNotEmpty()) {
            handleToggleDefault(addresses.first())
        }
    }


    Scaffold(
        topBar = { AddressesHeader() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addressToEdit = null
                    showFormSheet = true
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar novo endereço")
            }
        }
    ) { paddingValues ->
/*      Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
           if (isLoading) {
                AddressListSkeleton()
            } else if (addresses.isEmpty()) {
                EmptyState(onCtaClick = {
                    addressToEdit = null
                    showFormSheet = true
                })
            } else {*/
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(addresses, key = { it.id }) { address ->
                val isSelected = address.id == selectedAddressId

                AddressCard(
                    address = address,
                    isSelected = isSelected,
                    onEdit = {
                        addressToEdit = it
                        showFormSheet = true
                    },
                    onDelete = { addressToDelete = it },
                    onToggleDefault = {
                        handleToggleDefault(it)
                        selectedAddressId = it.id
                    }
                )
            }
        }
    }


    // Modal Sheet para Formulário
    if (showFormSheet) {
        AddressFormSheet(
            addressToEdit = addressToEdit,
            onDismiss = {
                showFormSheet = false
                addressToEdit = null
            },
            onSave = handleSaveAddress
        )
    }

    // Dialog de Confirmação de Exclusão
    addressToDelete?.let { address ->
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja remover o endereço '${address.label}'?") },
            confirmButton = {
                Button(onClick = { handleDeleteAddress(address) }) {
                    Text("Remover")
                }
            },
            dismissButton = {
                TextButton(onClick = { addressToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesHeader() {
    TopAppBar(
        title = { Text("Meus Endereços") },
        navigationIcon = {
            IconButton(onClick = { /* Ação de voltar */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun AddressCard(
    address: Address,
    isSelected: Boolean,
    onEdit: (Address) -> Unit,
    onDelete: (Address) -> Unit,
    onToggleDefault: (Address) -> Unit
) {
    // 🎨 ANIMAÇÃO DE COR/ALPHA
    val targetContainerColor = if (isSelected) {
        MaterialTheme.colorScheme.background // Cor mais clara/viva (selecionado)
    } else {
        MaterialTheme.colorScheme.surface // Cor de fundo suave (não selecionado)
    }

    val targetContentAlpha = if (isSelected) 1f else 0.6f
    val animatedContainerColor by animateColorAsState(
        targetValue = targetContainerColor,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "containerColor"
    )
    val animatedContentAlpha by animateFloatAsState(
        targetValue = targetContentAlpha,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "contentAlpha"
    )

    // ⏫ ANIMAÇÃO DE ELEVAÇÃO (X.dp -> 0.dp para afundar)
    val baseShadowElevation = 4.dp
    val targetShadowElevation: Dp = if (isSelected) 0.dp else baseShadowElevation
    val animatedShadowElevation by animateDpAsState(
        targetValue = targetShadowElevation,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "shadowElevation"
    )

    // ESTA ANIMAÇÃO É ESSENCIAL PARA O EFEITO DE "AFUNDAMENTO" NO M3
    val baseTonalElevation = 2.dp
    val targetTonalElevation: Dp = if (isSelected) 0.dp else baseTonalElevation
    val animatedTonalElevation by animateDpAsState(
        targetValue = targetTonalElevation,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "tonalElevation"
    )


    // 🤏 ANIMAÇÃO DE ESCALA (Para reforçar o efeito de "afundar" ou "levantar")
    val targetScale = if (isSelected) 0.99f else 1f
    val animatedScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "scale"
    )

    Surface(
        shape = MaterialTheme.shapes.medium,
        // 1. ELEVAÇÃO DE SOMBRA (Controla a sombra projetada)
        shadowElevation = animatedShadowElevation,

        // 2. ELEVAÇÃO TONAL (Controla a cor de "lift" do M3)
        tonalElevation = animatedTonalElevation,
        color =  animatedContainerColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = animatedContentAlpha)),
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale) // Aplica a animação de escala
            .padding(vertical = 8.dp) // Espaçamento vertical entre os cards
            .semantics { contentDescription = "Endereço: ${address.label}. Clique para selecionar." }
            .clickable{onToggleDefault(address)}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(address.icon, contentDescription = "Ícone endereço", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(address.label, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha))
                            if (address.isDefault) {
                                Spacer(modifier = Modifier.width(8.dp))
                                AssistChip(onClick = {}, label = { Text("Padrão") }, colors = AssistChipDefaults.assistChipColors(labelColor = MaterialTheme.colorScheme.surfaceVariant, containerColor = MaterialTheme.colorScheme.background))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(address.fullAddress, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha))
                        Text(address.cityStateZip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha))
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { onEdit(address) }) { Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)) }
                    IconButton(onClick = { onDelete(address) }) { Icon(Icons.Default.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = address.isDefault, onClick = { onToggleDefault(address) }, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary.copy(blue = 1f)))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Definir como endereço padrão", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha))
            }
        }
    }
}

// --- Address Form Sheet Component ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormSheet(
    addressToEdit: Address? = null,
    onDismiss: () -> Unit,
    onSave: (Address) -> Unit,
) {
    val isEditing = addressToEdit != null
    val title = if (isEditing) "Editar Endereço" else "Adicionar Novo Endereço"

    // Estados do Formulário
    var label by remember {
        mutableStateOf(
            addressToEdit?.label ?: AddressDataSource.predefinedLabels.first()
        )
    }
    var street by remember { mutableStateOf(addressToEdit?.street ?: "") }
    var number by remember { mutableStateOf(addressToEdit?.number ?: "") }
    var complement by remember { mutableStateOf(addressToEdit?.complement ?: "") }
    var neighborhood by remember { mutableStateOf(addressToEdit?.neighborhood ?: "") }
    var city by remember { mutableStateOf(addressToEdit?.city ?: "") }
    var state by remember { mutableStateOf(addressToEdit?.state ?: "") }
    var zip by remember { mutableStateOf(addressToEdit?.zip ?: "") }

    val isFormValid =
        street.isNotBlank() && number.isNotBlank() && neighborhood.isNotBlank() && city.isNotBlank() && state.isNotBlank() && zip.isNotBlank()

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Rótulo (Dropdown)
            LabelDropdown(
                selectedLabel = label,
                onLabelSelected = { label = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campos de Endereço
            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Rua") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Número") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = complement,
                    onValueChange = { complement = it },
                    label = { Text("Complemento (Opcional)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Cidade") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("Estado") },
                    modifier = Modifier.weight(0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = zip,
                onValueChange = { zip = it },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth().imePadding(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botões de Ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Button(
                    onClick = {
                        val newAddress = Address(
                            id = addressToEdit?.id
                                ?: 0, // ID 0 para novo, será tratado na tela principal
                            label = label,
                            street = street,
                            number = number,
                            complement = complement.ifBlank { null },
                            neighborhood = neighborhood,
                            city = city,
                            state = state,
                            zip = zip,
                            isDefault = addressToEdit?.isDefault ?: false
                        )
                        onSave(newAddress)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isEditing) "Salvar Alterações" else "Adicionar Endereço")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelDropdown(
    selectedLabel: String,
    onLabelSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedLabel,
            onValueChange = { },
            label = { Text("Rótulo") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AddressDataSource.predefinedLabels.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onLabelSelected(label)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

// --- States and Shimmer ---

@Composable
fun EmptyState(
    title: String = "Nenhum endereço cadastrado",
    message: String = "Adicione seu primeiro endereço para começar a fazer pedidos.",
    icon: ImageVector = Icons.Default.AddLocationAlt,
    ctaText: String = "Adicionar Endereço",
    onCtaClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCtaClick) {
            Text(ctaText)
        }
    }
}

@Composable
fun ShimmerBrush(
    showShimmer: Boolean = true,
    targetColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            targetColor.copy(alpha = 0.9f),
            targetColor.copy(alpha = 0.4f),
            targetColor.copy(alpha = 0.9f),
        )

        val transition = rememberInfiniteTransition(label = "Shimmer Transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = "Shimmer Animation"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@Composable
fun AddressCardSkeleton() {
    val brush = ShimmerBrush()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Rótulo e Badge Skeleton
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(brush, RoundedCornerShape(4.dp))
                    )
                }
                // Ações Skeleton
                Row {
                    Spacer(
                        modifier = Modifier
                            .size(24.dp)
                            .background(brush, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Spacer(
                        modifier = Modifier
                            .size(24.dp)
                            .background(brush, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Linhas de Endereço Skeleton
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(16.dp)
                    .background(brush, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .background(brush, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle Skeleton
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(
                    modifier = Modifier
                        .width(150.dp)
                        .height(16.dp)
                        .background(brush, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun AddressListSkeleton(count: Int = 3) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(count) {
            AddressCardSkeleton()
        }
    }
}