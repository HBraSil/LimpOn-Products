package com.example.produtosdelimpeza.customer.profile.address.presentation


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnSearchBar
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.AddressType
import com.example.produtosdelimpeza.core.map.presentation.MapViewModel
import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion


private const val ANIMATION_DURATION = 300
private val ANIMATION_EASING = FastOutSlowInEasing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    onBackNavigation: () -> Unit,
    goToMap: () -> Unit,
    addressViewModel: AddressViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val searchText by mapViewModel.searchText.collectAsStateWithLifecycle()
    val mapState by mapViewModel.searchState.collectAsStateWithLifecycle()
    val addressState by addressViewModel.addressState.collectAsStateWithLifecycle()
    val addresses by addressViewModel.getSavedPlaces.collectAsStateWithLifecycle()
    val mainAddress by addressViewModel.getSelectedAddress.collectAsStateWithLifecycle()



    AddressContent(
        mapState = mapState,
        addressState = addressState,
        searchText = searchText,
        addresses = addresses,
        mainAddress = mainAddress,
        onConfirmPlace = {
            addressViewModel.addAddress(it)
        },
        onDeleteAddress = {
            addressViewModel.deleteAddress(it)
        },
        onQueryChanged = {
            mapViewModel.onQueryChange(it)
        },
        onBackNavigation = onBackNavigation,
        goToMap = goToMap
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressContent(
    mapState: List<PlaceSuggestion>,
    addressState: AddressUiState,
    searchText: String,
    addressViewModel: AddressViewModel = hiltViewModel(),
    addresses: List<Address>,
    mainAddress: Address?,
    onConfirmPlace: (String) -> Unit,
    onDeleteAddress: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    onBackNavigation: () -> Unit,
    goToMap: () -> Unit,
) {
    var addressToDelete by remember { mutableStateOf<Address?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }
    var editSavedAlertDialog by remember { mutableStateOf(false) }

    val addressToEdit by addressViewModel.addressState.collectAsStateWithLifecycle()
    val editState by addressViewModel.addressState.collectAsStateWithLifecycle()


    LaunchedEffect(addressState) {
        if (addressState.addressSavedSuccessfully) {
            isSearchActive = false
        }

        if (addressState.editedSuccessfully) {
            editSavedAlertDialog = true
        }
    }


    Scaffold(
        topBar = { AddressesHeader(onBack = onBackNavigation) },
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .padding(paddingValues)
        ){
            LimpOnSearchBar(
                mapState = mapState,
                searchText = searchText,
                isSearchActive = isSearchActive,
                onActiveChange = { isSearchActive = it },
                onQueryChanged = onQueryChanged,
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon_description),
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(0.4f),
                                shape = CircleShape
                            )
                            .padding(6.dp)
                    )
                },
                containerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                onConfirmPlace = {
                    onConfirmPlace(it.placeId)
                }
            )

            Spacer(modifier = Modifier.height(22.dp))
            DividerLine()
            Spacer(modifier = Modifier.height(20.dp))
            AddressGoToMap(onGoToMapClick = goToMap)
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (addresses.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AddressTitleInfo(
                        text = stringResource(R.string.without_address),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                    AddressTitleInfo(
                        text = stringResource(R.string.salved_addresses),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    addresses.reversed().forEach { address ->
                        val isSelected = address.id == (mainAddress?.id ?: 1)
                        AddressCard(
                            address = address,
                            isSelected = isSelected,
                            onEdit = { address ->
                                addressViewModel.addressToEdit(address)
                            },
                            onDelete = { address ->
                                addressToDelete = address
                            },
                            onToggleDefault = {
                                addressViewModel.selectMainAddress(it)
                            }
                        )
                    }
                }
            }
        }
    }


    addressToDelete?.let { address ->
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja remover o endereço '${address.street}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d("AddressScreen", "Address deleted: ${address.id}, ${address.city}")
                        onDeleteAddress(address.id)
                        addressToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Remover")
                }
            },
            dismissButton = {
                TextButton(onClick = { addressToDelete = null }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }



    addressToEdit.address?.let { address ->
        AddressEditBottomSheet(
            address = address,
            addressType = editState.address?.addressType,
            complement = editState.address?.complement ?: "",
            isButtonEnabled = editState.isAddressTypeFilled || editState.isAddressComponentFilled,
            onDismiss = {
                addressViewModel.addressToEdit(null)
            },
            onTypeSelected = { id, type ->
                addressViewModel.updateAddressType(id, type)
            },
            /*onPlaceNameChange = { text ->
                // uiState = uiState.copy(placeName = text)
            },*/
            onComplementChange = { id, text ->
                addressViewModel.updateComplement(id, text)
            },
            onSaveChangeClick = {
                addressViewModel.saveEditButton()
            }
        )
    }

    if (editSavedAlertDialog) {
        AddressUpdatedDialog(
            onDismiss = {
                editSavedAlertDialog = false
                addressViewModel.addressToEdit(null)
            }
        )
    }
}

@Composable
fun AddressTitleInfo(
    text: String,
    style: TextStyle,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color
) {
    Text(
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color
    )
}

@Composable
fun DividerLine() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TabRowDefaults.Divider(modifier = Modifier.weight(1f))
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.insert_manually),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.width(16.dp))
        TabRowDefaults.Divider(modifier = Modifier.weight(1f))
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
            containerColor = Color.Transparent,
        )
    )
}

@Composable
fun AddressGoToMap(
    onGoToMapClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onGoToMapClick() },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(R.drawable.img),
            contentDescription = stringResource(R.string.center_location_icon),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "Selecionar no mapa"
        )
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = ""
        )
    }
}

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address,
    isSelected: Boolean,
    onEdit: (Address) -> Unit,
    onDelete: (Address) -> Unit,
    onToggleDefault: (String) -> Unit,
) {
    val targetContainerColor = if (isSelected) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.surface
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
    val baseShadowElevation = 4.dp
    val targetShadowElevation: Dp = if (isSelected) 0.dp else baseShadowElevation
    val animatedShadowElevation by animateDpAsState(
        targetValue = targetShadowElevation,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "shadowElevation"
    )
    val baseTonalElevation = 2.dp
    val targetTonalElevation: Dp = if (isSelected) 0.dp else baseTonalElevation
    val animatedTonalElevation by animateDpAsState(
        targetValue = targetTonalElevation,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "tonalElevation"
    )
    val targetScale = if (isSelected) 0.99f else 1f
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(ANIMATION_DURATION, easing = ANIMATION_EASING),
        label = "scale"
    )

    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = animatedShadowElevation,
        tonalElevation = animatedTonalElevation,
        color = animatedContainerColor,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = animatedContentAlpha)
        ),
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .semantics {
                contentDescription = "Endereço: ${address.addressType}. Clique para selecionar."
            }
            .clickable { onToggleDefault(address.id) }
    ) {
        val primary = address.primaryLabel()
        val secondary = address.secondaryLabel()

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = address.icon,
                        contentDescription = "Ícone endereço",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (primary.isNotBlank()) {
                                Text(
                                    text = primary,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha),
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )
                            }/* else {
                                Spacer(modifier = Modifier.weight(1f))
                            }*/
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                AssistChip(
                                    onClick = {},
                                    label = { Text(text = "Padrão", maxLines = 1) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        labelColor = MaterialTheme.colorScheme.surfaceVariant,
                                        containerColor = MaterialTheme.colorScheme.background
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        if (secondary.isNotBlank()) {
                            Text(
                                text = secondary,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha * 0.9f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        if (address.complement.isNotBlank()) {
                            Text(
                                text = "Complemento: ${address.complement}",
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = animatedContentAlpha)
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(
                        onClick = { onEdit(address) },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.background.copy(alpha = animatedContentAlpha),
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = animatedContentAlpha)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                        )
                    }
                    IconButton(
                        onClick = { onDelete(address) },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error.copy(alpha = animatedContentAlpha),
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = animatedContentAlpha)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onToggleDefault(address.id) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.secondary.copy(blue = 1f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Definir como endereço padrão",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditBottomSheet(
    address: Address,
    addressType: AddressType? = null,
    complement: String = "",
    onDismiss: () -> Unit,
    onTypeSelected: (String, AddressType) -> Unit,
    onComplementChange: (String, String) -> Unit,
    onSaveChangeClick: () -> Unit,
    isButtonEnabled: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var otherAddressType by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            HeaderSection(address.street, onDismiss)

            Spacer(modifier = Modifier.height(6.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TIPO DE ENDEREÇO",
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(18.dp))

            AddressTypeSection(
                selectedType = addressType,
                onTypeSelected = { onTypeSelected(address.id, it) }
            )

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(
                visible = addressType == AddressType.Other(""),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    RoundedInput(
                        value = otherAddressType,
                        onValueChange = { otherAddressType = it },
                        placeholder = "Nomeie este lugar (ex: Academia)",
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                }
            }

            RoundedInput(
                value = complement,
                onValueChange = { onComplementChange(address.id, it) },
                placeholder = "Complemento (opcional)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ex: Bloco B, Apartamento 42",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(26.dp))

            SaveButton(isButtonEnabled = isButtonEnabled, onSaveChangeClick)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HeaderSection(
    addressName: String?,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Editar Endereço",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = addressName ?: "Endereço não selecionado",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
fun AddressTypeSection(
    selectedType: AddressType?,
    onTypeSelected: (AddressType) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AddressTypeChip(
            label = AddressType.Home.label,
            icon = Icons.Default.Home,
            selected = selectedType == AddressType.Home,
            onClick = { onTypeSelected(AddressType.Home) }
        )

        AddressTypeChip(
            label = AddressType.Work.label,
            icon = Icons.Default.Work,
            selected = selectedType == AddressType.Work,
            onClick = { onTypeSelected(AddressType.Work) }
        )

        AddressTypeChip(
            label = AddressType.Other("").label,
            icon = Icons.Default.LocationOn,
            selected = selectedType == AddressType.Other(""),
            onClick = { onTypeSelected(AddressType.Other("")) }
        )
    }
}

@Composable
fun AddressTypeChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    val content =
        if (selected) MaterialTheme.colorScheme.onSecondary
        else MaterialTheme.colorScheme.onBackground.copy(0.6f)

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = content
        )
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        Text(label)
    }
}

@Composable
fun RoundedInput(
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    placeholder: String
) {
    if (onValueChange != null) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            placeholder = { Text(placeholder) },
            singleLine = true
        )
    }
}


@Composable
fun SaveButton(isButtonEnabled: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = isButtonEnabled,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text(
            text = "Salvar Endereço",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.size(8.dp))

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null
        )
    }
}
@Composable
fun AddressUpdatedDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .widthIn(min = 280.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(92.dp)
                        .background(
                            color = Color(0xFFEDEEFF),
                            shape = CircleShape
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = Color(0xFF2F49F5),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Endereço Atualizado",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "As alterações no seu endereço foram salvas com sucesso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F49F5)
                    )
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
