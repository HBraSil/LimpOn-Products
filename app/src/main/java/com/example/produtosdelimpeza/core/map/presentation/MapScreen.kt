package com.example.produtosdelimpeza.core.map.presentation

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.text.Spannable
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnSearchBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import kotlin.collections.forEach

internal val predictionsHighlightStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
internal fun Spannable.toAnnotatedString(spanStyle: SpanStyle?): AnnotatedString {
    return buildAnnotatedString {
        if (spanStyle == null) {
            append(this@toAnnotatedString.toString())
        } else {
            var last = 0
            for (span in getSpans(0, this@toAnnotatedString.length, Any::class.java)) {
                val start = this@toAnnotatedString.getSpanStart(span)
                val end = this@toAnnotatedString.getSpanEnd(span)
                if (last < start) {
                    append(this@toAnnotatedString.substring(last, start))
                }
                withStyle(spanStyle) {
                    append(this@toAnnotatedString.substring(start, end))
                }
                last = end
            }
            if (last < this@toAnnotatedString.length) {
                append(this@toAnnotatedString.substring(last))
            }
        }
    }
}


@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    goToAddressScreen: () -> Unit = {}
/*    state: LocationUiState,
    onQueryChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onMenuClick: () -> Unit,*/
) {
    MapContent(
        mapViewModel,
        goToAddressScreen = goToAddressScreen
        /*state = state,
        onQueryChange = onQueryChange,
        onConfirmClick = onConfirmClick,
        onMenuClick = onMenuClick*/
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
    mapViewModel: MapViewModel = hiltViewModel(),
    goToAddressScreen: () -> Unit = {}
) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-23.5505, -46.6333),
            10f
        )
    }
    var withoutPermission by remember { mutableStateOf(false) }

    val events = mapViewModel.mapUiEvent
    val state by mapViewModel.mapState.collectAsStateWithLifecycle()
    val text by mapViewModel.searchText.collectAsStateWithLifecycle()
    val searchState by mapViewModel.searchState.collectAsStateWithLifecycle()

    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_json),
        isMyLocationEnabled = true
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val coarseLocationGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted && coarseLocationGranted) {
            mapViewModel.fetchUserLocation()
        }
    }

    // Novo launcher para resolução do LocationSettings (esse é o que você precisa)
    val resolutionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            mapViewModel.fetchUserLocation()
        }
    }

    LaunchedEffect(state.placeSavedSuccessfully) {
        if (state.placeSavedSuccessfully) {
            goToAddressScreen()
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val target = cameraPositionState.position.target
            mapViewModel.onCameraMove(LatLng(target.latitude, target.longitude))
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is MapUiEvent.OnCenterLocationClick -> {
                    state.userLocation?.let { latLng ->
                        Log.d("TESTE", "RESULTADO dentro do Launched: ${latLng.latitude} ${latLng.longitude}")
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                            600
                        )
                    }
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is MapUiEvent.RequestLocationResolution -> {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(event.exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        sendEx.printStackTrace()
                    }
                }
                else -> {}
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()
        .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary.copy(blue = 1f),
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .offset(y = (-24).dp)
        )

        TopSearchSection(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopCenter)
                .padding(horizontal = 10.dp)
                .zIndex(1f),
            state = searchState,
            searchText = text,
            cameraPositionState,
            onQueryChanged = { mapViewModel.onQueryChange(it) },
            onConfirmPlace = { mapViewModel.onPlaceSelected(it) },
            goToUserLocation = { mapViewModel.onCenterLocationClick() }
        )

        if (state.place != null /*&& state.userLocation != null*/) {
            ConfirmLocationCard(
                place = state.place!!,
                //userLocation = state.userLocation!!,
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                onConfirmClick = {
                    mapViewModel.savePlace(it)
                },
                closeCard = {},
            )
        }
    }


    if (withoutPermission) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = { withoutPermission = false }
                ){
                    Text("Cancel")
                }
            },
            title = {
                Text(
                    text = "Sem permissão",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {}
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchSection(
    modifier: Modifier = Modifier,
    state: List<PlaceSuggestion>,
    searchText: String,
    cameraPositionState: CameraPositionState,
    onQueryChanged: (String) -> Unit,
    onConfirmPlace: (PlaceSuggestion) -> Unit,
    goToUserLocation: () -> Unit,
) {
    var isSearchActive by remember { mutableStateOf(state.isNotEmpty()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LimpOnSearchBar(
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
            containerColor = Color.Transparent
        ) {
            SuggestionsContent(
                places = state,
                onPlaceClick = {
                    onConfirmPlace(it)
                    isSearchActive = false
                }
            )
        }


        /*SearchBar(
            modifier = Modifier.fillMaxWidth(),
            expanded = isSearchActive,
            onExpandedChange = { isSearchActive = it },
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = onQueryChanged,
                    onSearch = {},
                    expanded = isSearchActive,
                    onExpandedChange = { isSearchActive = it },
                    placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                    leadingIcon = {
                        if (!isSearchActive) {
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
                        } else {
                            IconButton(onClick = { isSearchActive = false }) {
                                Icon(
                                    Icons.Default.ArrowBackIosNew,
                                    null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    trailingIcon = { *//* Seu código de menu e divider aqui *//* },

                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color.Transparent,
                dividerColor = Color.Transparent
            ),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
        ) {
            SuggestionsContent(
                places = state,
                onPlaceClick = {
                    onConfirmPlace(it)
                    isSearchActive = false
                }
            )
        }*/

        SmallFloatingActionButton(
            onClick = {
                scope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.zoomIn()
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                Icons.Default.Add, null,
                modifier = Modifier
                    .padding(14.dp)
                    .size(24.dp)
            )
        }

        SmallFloatingActionButton(
            onClick = {
                scope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.zoomOut()
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                Icons.Default.Remove,
                null,
                modifier = Modifier
                    .padding(14.dp)
                    .size(24.dp)
            )
        }

        SmallFloatingActionButton(
            onClick = goToUserLocation,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.onSecondary.copy(1f),
            contentColor = MaterialTheme.colorScheme.background
        ) {
            Icon(
                painter = painterResource(R.drawable.img),
                contentDescription = stringResource(R.string.center_location_icon),
                modifier = Modifier
                    .padding(14.dp)
                    .size(26.dp),
                tint =
                    MaterialTheme.colorScheme.background
                    /*if (hasLocationPermission)
                else
                    MaterialTheme.colorScheme.background.copy(0.4f)*/
            )
        }
    }
}

@Composable
fun ConfirmLocationCard(
    modifier: Modifier = Modifier,
    //userLocation: LatLng,
    place: PlaceSuggestion,
    onConfirmClick: (PlaceSuggestion) -> Unit,
    closeCard: () -> Unit,
) {
    Card(
        modifier = modifier.padding(20.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SELECTED LOCATION",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(blue = 1f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = place.primaryText.toAnnotatedString(predictionsHighlightStyle),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = place.secondaryText.toAnnotatedString(predictionsHighlightStyle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                IconButton(
                   onClick = closeCard,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(0.2f),
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_component)
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { onConfirmClick(place) },
                modifier = Modifier.fillMaxWidth()
                    .clip(
                        RoundedCornerShape(20.dp),
                    ).background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary.copy(0.4f)
                            ),
                            startX = 100f
                        )
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text("Confirmar Localização")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.confirm_location),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun SuggestionsContent(
    places: List<PlaceSuggestion>,
    onPlaceClick: (PlaceSuggestion) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {

            // 🔹 HEADER
            Text(
                text = "SUGGESTIONS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 🔹 LISTA
            places.forEach { place ->
                AutocompletePlacesItem(
                    place = place,
                    onClick = { onPlaceClick(place) }
                )
            }
        }
    }
}

@Composable
fun AutocompletePlacesItem(
    place: PlaceSuggestion,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🔹 ÍCONE
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 🔹 TEXTOS
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = place.primaryText.toAnnotatedString(predictionsHighlightStyle),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = place.secondaryText.toAnnotatedString(predictionsHighlightStyle),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // 🔹 DISTÂNCIA (opcional)
        place.distance?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MapPreview() {
    MapScreen(
    )
}