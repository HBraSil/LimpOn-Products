package com.example.produtosdelimpeza.customer.profile.presentation.address


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.R


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
        ),
        Address(
            id = 4,
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
        ),
        Address(
            id = 5,
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
        ),
        Address(
            id = 6,
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
fun AddressesScreen(onGoToAddNewAddressScreen: () -> Unit, onBackNavigation: () -> Unit) {

    var selectedAddressId by remember { mutableIntStateOf(1) }
    val addresses = remember {
        mutableStateListOf<Address>().apply { addAll(AddressDataSource.sampleAddresses) }
    }


    var addressToEdit by remember { mutableStateOf<Address?>(null) }

    var addressToDelete by remember { mutableStateOf<Address?>(null) }



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
        topBar = { AddressesHeader(onBack = onBackNavigation) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    addressToEdit = null
                    onGoToAddNewAddressScreen()
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Text(text = stringResource(R.string.add_new_address))
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
            contentPadding = PaddingValues(bottom = 90.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(addresses, key = { it.id }) { address ->
                val isSelected = address.id == selectedAddressId

                AddressCard(
                    modifier = Modifier,
                    address = address,
                    isSelected = isSelected,
                    onEdit = {
                        addressToEdit = it
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


/*    if (showFormSheet) {
        AddressFormSheet(
            addressToEdit = addressToEdit,
            onDismiss = {
                showFormSheet = false
                addressToEdit = null
            },
            onSave = handleSaveAddress
        )
    }*/

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
fun AddressesHeader(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Meus Endereços") },
        navigationIcon = {
            IconButton(onClick = onBack) {
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
    modifier: Modifier = Modifier,
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
    val animatedScale by animateFloatAsState(
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
        modifier = modifier
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