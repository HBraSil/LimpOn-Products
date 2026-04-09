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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
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
/*    state: LocationUiState,
    onQueryChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onMenuClick: () -> Unit,*/
    mapViewModel: MapViewModel = hiltViewModel()
) {
    MapContent(/*
        state = state,
        onQueryChange = onQueryChange,
        onConfirmClick = onConfirmClick,
        onMenuClick = onMenuClick*/
        mapViewModel
    )
}

enum class BottomNavItem {
    Explore,
    Saved
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    val cameraPositionState = rememberCameraPositionState()

    var selected by remember { mutableStateOf(BottomNavItem.Explore) }
    var isSheetOpen by remember { mutableStateOf(false) }
    var isLocationSelected by remember { mutableStateOf(false) }
    var withoutPermission by remember { mutableStateOf(false) }
    var isBottomCardVisible by remember { mutableStateOf(false) }

    val events = mapViewModel.mapUiEvent
    val state by mapViewModel.mapState.collectAsStateWithLifecycle()
    val text by mapViewModel.searchText.collectAsStateWithLifecycle()
    val searchState by mapViewModel.searchState.collectAsStateWithLifecycle()
    val savedPlaces by mapViewModel.savedPlaces.collectAsStateWithLifecycle()

    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_json)
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
            Log.d("ATENÇAO", "CAIU AQUI resolutionLauncher")
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
        ) {
            state.userLocation?.let { latLng ->
                Marker(
                    state = rememberUpdatedMarkerState(position = latLng),
                    title = "Minha localização",
                    snippet = "This is where you are currently located."
                )
            }
        }

        TopSearchSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 10.dp)
                .zIndex(1f),
            state = searchState,
            searchText = text,
            cameraPositionState,
            onQueryChanged = { mapViewModel.onQueryChange(it) },
            onConfirmPlace = { mapViewModel.onPlaceSelected(it) },
            onMenuClick = {},
            goToUserLocation = {
                mapViewModel.onCenterLocationClick()
            }
        )

        if (state.place != null) {
            LocationCard(
                place = state.place!!,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 90.dp),
                onConfirmClick = {},
                savePlace = { mapViewModel.savePlace(it) }
            )
        }

        AnimatedSavedSheet(
            visible = isBottomCardVisible,
            onClose = { isSheetOpen = false },
            placeList = savedPlaces,
            modifier = Modifier.zIndex(2f)
        )


        MapBottomNavigationBar(
            selected = selected,
            onItemSelected = {
                if (selected != it && it == BottomNavItem.Saved) {
                    isBottomCardVisible = true
                    selected = it
                } else {
                    selected = it
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
        )
    }


    if (withoutPermission) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = {
                        withoutPermission = false
                    }
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
    onMenuClick: () -> Unit,
    goToUserLocation: () -> Unit,
) {
    var isSearchActive by remember { mutableStateOf(state.isNotEmpty()) }
    val scope = rememberCoroutineScope()


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchBar(
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
                    trailingIcon = { /* Seu código de menu e divider aqui */ },

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
        }

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
                contentDescription = "Location",
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
fun LocationCard(
    place: PlaceSuggestion,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    savePlace: (PlaceSuggestion) -> Unit
) {
    Card(
        modifier = modifier.padding(20.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "SELECTED LOCATION",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary.copy(blue = 1f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = place.primaryText.toAnnotatedString(predictionsHighlightStyle),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = place.secondaryText.toAnnotatedString(predictionsHighlightStyle),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                IconButton(
                   onClick = {},
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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DistanceBadge(distanceKm = place.distance?.toDouble() ?: 0.0)
                EtaCard(etaMinutes = place.etaMinutes)

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.LightGray)
                ) {
                    Text(
                        text = "Street View",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier.weight(1f)
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

                IconButton(
                    onClick = { savePlace(place) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(0.2f),
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = stringResource(R.string.close_component)
                    )
                }
            }
        }
    }
}

@Composable
private fun EtaCard(etaMinutes: String) {
    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE7EBFF))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = Color(0xFF4A4AE6)
        )
        Text(
            text = "ETA",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = "$etaMinutes min",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4AE6)
        )
    }
}

@Composable
private fun DistanceBadge(distanceKm: Double) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE7EBFF))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Straighten,
            contentDescription = null,
            tint = Color(0xFF4A4AE6)
        )
        Text(
            text = String.format("%.1f", distanceKm),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4AE6)
        )
        Text(
            text = "KM",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF4A4AE6)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedSavedSheet(
    modifier: Modifier,
    visible: Boolean,
    placeList: List<PlaceSuggestion>,
    onClose: () -> Unit
) {
    val favorites = listOf(
        Place("Home", "221B Baker Street, London", icon = Icons.Default.Home),
        Place("Work", "One Canary Wharf, Floor 42", icon = Icons.Default.Work)
    )

    val suggestions = listOf(
        Place("Roast & Brew", "Soho, London", "Cafeteria"),
        Place("Sky Garden", "City of London", "Natureza"),
        Place("Tate Modern", "South Bank", "Cultura")
    )


    val density = LocalDensity.current
    val navBarHeight = WindowInsets.navigationBars.getBottom(density)
    var containerHeight by remember { mutableIntStateOf(0) }

    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else (containerHeight + navBarHeight).toFloat(),
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "sheetOffset"
    )


    val scrimAlpha by animateFloatAsState(
        targetValue = if (visible) 0.3f else 0f,
        animationSpec = tween(300),
        label = "scrim"
    )

    Box(modifier = modifier.fillMaxSize()) {
        if (scrimAlpha > 0f) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
                    .clickable { onClose() }
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, offsetY.toInt()) }
                .fillMaxWidth()
                .fillMaxHeight()
                .onSizeChanged { containerHeight = it.height }
                .background(MaterialTheme.colorScheme.surface)
        ) {
            SavedPlacesContent(
                favorites = favorites,
                savedPlaces = placeList,
                suggestions = suggestions
            )
        }
    }
}


@Composable
fun SavedPlacesContent(
    modifier: Modifier = Modifier,
    favorites: List<Place>,
    savedPlaces: List<PlaceSuggestion>,
    suggestions: List<Place>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Header()
        }

        if (favorites.isNotEmpty()) {
            item { SectionTitle("Favorites") }
            items(favorites) { place ->
                FavoriteItem(place)
            }
        }

        if (savedPlaces.isNotEmpty()) {
            item { SectionTitle("Saved Lists") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(savedPlaces) { list ->
                        SavedListCard(list)
                    }
                }
            }
        }

        if (suggestions.isNotEmpty()) {
            item { SectionTitle("Suggestions for You") }
            items(suggestions) { place ->
                SuggestionItem(place)
            }
        }
    }
}

@Composable
fun MapBottomNavigationBar(
    modifier: Modifier = Modifier,
    selected: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(bottom = 20.dp)
            .zIndex(3f),
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.background
    ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BottomItem(
                    selected = selected == BottomNavItem.Explore,
                    icon = Icons.Default.Explore,
                    label = "Explore",
                    onClick = { onItemSelected(BottomNavItem.Explore) }
                )

                BottomItem(
                    selected = selected == BottomNavItem.Saved,
                    icon = Icons.Default.Bookmark,
                    label = "Salvos",
                    onClick = { onItemSelected(BottomNavItem.Saved) }
                )
            }
        }

}

@Composable
private fun BottomItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {

    val backgroundColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onSecondary.copy(1f)
        else Color.Transparent,
        label = ""
    )

    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = ""
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 30.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = label,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun Header() {
    Column {
        Text(
            "Saved\nPlaces",
            style = MaterialTheme.typography.displayMedium,
            lineHeight = 40.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Your personal map collection",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun FavoriteItem(place: Place) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(place.icon, null)
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(place.name, style = MaterialTheme.typography.bodyLarge)
            Text(place.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SavedListCard(place: PlaceSuggestion) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        Box(
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "something goes here items",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Column(Modifier.padding(12.dp)) {
            Text(place.primaryText.toAnnotatedString(predictionsHighlightStyle), style = MaterialTheme.typography.bodyLarge)
            Text(place.distance ?: "Distância não calculada", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SuggestionItem(place: Place) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(place.category, style = MaterialTheme.typography.labelSmall)
            Text(place.name, style = MaterialTheme.typography.bodyLarge)
            Text(place.address, style = MaterialTheme.typography.bodySmall)
        }

        Icon(Icons.Default.ChevronRight, null)
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



// MODELS

data class Place(
    val name: String,
    val address: String,
    val category: String = "",
    val icon: ImageVector = Icons.Default.Place
)

data class SavedList(
    val name: String,
    val count: Int,
    val distance: String
)

