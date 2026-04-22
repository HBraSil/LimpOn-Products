package com.example.produtosdelimpeza.customer.profile.address.presentation


import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnSearchBar
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.map.presentation.MapViewModel
import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion
import com.example.produtosdelimpeza.core.map.presentation.SuggestionsContent


private const val ANIMATION_DURATION = 300
private val ANIMATION_EASING = FastOutSlowInEasing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    onGoToAddNewAddressScreen: () -> Unit,
    onBackNavigation: () -> Unit,
    goToMap: () -> Unit,
    addressViewModel: AddressViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val searchText by mapViewModel.searchText.collectAsStateWithLifecycle()
    val state by mapViewModel.searchState.collectAsStateWithLifecycle()
    val addresses by addressViewModel.getSavedPlaces.collectAsStateWithLifecycle()
    val mainAddress by addressViewModel.getSelectedAddress.collectAsStateWithLifecycle()


    AddressContent(
        state = state,
        searchText = searchText,
        addresses = addresses,
        mainAddress = mainAddress,
        onConfirmPlace = {
            mapViewModel.onPlaceSelected(it)
        },
        onQueryChanged = {
            mapViewModel.onQueryChange(it)
        },
        onGoToAddNewAddressScreen = onGoToAddNewAddressScreen,
        onBackNavigation = onBackNavigation,
        goToMap = goToMap
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressContent(
    state: List<PlaceSuggestion>,
    searchText: String,
    addressViewModel: AddressViewModel = hiltViewModel(),
    addresses: List<Address>,
    mainAddress: Address?,
    onConfirmPlace: (PlaceSuggestion) -> Unit,
    onQueryChanged: (String) -> Unit,
    onGoToAddNewAddressScreen: () -> Unit,
    onBackNavigation: () -> Unit,
    goToMap: () -> Unit,
) {
    var addressToEdit by remember { mutableStateOf<Address?>(null) }
    var addressToDelete by remember { mutableStateOf<Address?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AddressesHeader(onBack = onBackNavigation) },
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            LimpOnSearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                searchText = searchText,
                isSearchActive = isSearchActive,
                onActiveChange = { isSearchActive = it },
                onQueryChanged = onQueryChanged,
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                SuggestionsContent(
                    places = state,
                    onPlaceClick = {
                        onConfirmPlace(it)
                        isSearchActive = false
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (addresses.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhum endereço cadastrado.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    DividerLine()
                    AddressGoToMap(onGoToMapClick = goToMap)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Endereços Salvos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    addresses.reversed().forEach { address ->
                        Log.d(
                            "AddressesScreen",
                            "Address: ${mainAddress?.id}, ${mainAddress?.city}"
                        )
                        val isSelected = address.id == (mainAddress?.id ?: 1)
                        AddressCard(
                            address = address,
                            isSelected = isSelected,
                            onEdit = { address ->
                                addressToEdit = address
                                onGoToAddNewAddressScreen()
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


    // Dialog de Confirmação de Exclusão
    addressToDelete?.let { address ->
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja remover o endereço '${address.label}'?") },
            confirmButton = {
                Button(onClick = {}) {
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

@Composable
fun DividerLine() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TabRowDefaults.Divider(modifier = Modifier.weight(1f))
        Text(
            text = "  OR ENTER MANUALLY  ",
            style = MaterialTheme.typography.labelMedium
        )
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
    Row(
        modifier = Modifier.clickable { onGoToMapClick() },
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
            .padding(vertical = 8.dp)
            .semantics {
                contentDescription = "Endereço: ${address.label}. Clique para selecionar."
            }
            .clickable { onToggleDefault(address.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = address.icon,
                        contentDescription = "Ícone endereço",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = address.city,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Padrão") },
                                    colors = AssistChipDefaults.assistChipColors(
                                        labelColor = MaterialTheme.colorScheme.surfaceVariant,
                                        containerColor = MaterialTheme.colorScheme.background
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = address.state,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                        )
                        Text(
                            text = address.cityStateZip,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = { onEdit(address) }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                        )
                    }
                    IconButton(onClick = { onDelete(address) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Remover",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
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