package com.example.produtosdelimpeza.core.map.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch


data class LocationUiState(
    val query: String = "",
    val address: String = "",
    val subtitle: String = "",
    val distance: String = ""
)



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
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userLocation by mapViewModel.userLocation.collectAsStateWithLifecycle()
    var withoutPermission by remember { mutableStateOf(false) }
    val uiState by mapViewModel.uiState.collectAsState()
    var isBottomCardVisible by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(BottomNavItem.Explore) }
    val centerLocation = mapViewModel.events
    val cameraPositionState = rememberCameraPositionState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val coarseLocationGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (!fineLocationGranted || !coarseLocationGranted) {
            mapViewModel.fetchUserLocation()
        }
    }

    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_json)
    )

    LaunchedEffect(userLocation, centerLocation.collectAsStateWithLifecycle(0).value) {
        centerLocation.collect { event ->
            if (event) {
                userLocation?.let { latLng ->
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                    )
                }
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



/*
    val myLocationSource = object : LocationSource {
        var listener: LocationSource.OnLocationChangedListener? = null

        override fun activate(p0: LocationSource.OnLocationChangedListener) {
            this.listener = p0
        }

        override fun deactivate() {
            this.listener = null
        }

        fun onLocationChanged(location: Location) {
            listener?.onLocationChanged(location)
        }
    }*/


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
           // locationSource = myLocationSource
        ) {
            userLocation?.let { latLng ->
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
            state = LocationUiState(
                query = "",
                address = "Av. Paulista, 1234",
                subtitle = "Bela Vista, São Paulo - SP",
                distance = "2.5 km"
            ),
            cameraPositionState,
            onQueryChange = {},
            onMenuClick = {},
            goToUserLocation = {
                mapViewModel.fetchUserLocation()
            }
        )

        AnimatedSavedSheet(
            visible = isBottomCardVisible,
            sheetState = sheetState,
            onClose = { isSheetOpen = false },
            modifier = Modifier.zIndex(2f)
        )

        ModernBottomBar(
            selected = selected,
            onItemSelected = {
                if (selected != it && it == BottomNavItem.Saved) {
                    isBottomCardVisible = true
                    selected = it
                } else {
                    isBottomCardVisible = false
                    selected = it
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp).zIndex(3f)
        )
    }


    if (withoutPermission) {
        AlertDialog(
            onDismissRequest = {

            },
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

/*ElevatedCard(
    modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(16.dp)
        .onGloballyPositioned {
            cardHeight = with(density) {
                it.size.height.toDp()
            }
        },
    shape = RoundedCornerShape(28.dp)
) {

    Column(
        modifier = Modifier.padding(20.dp)
    ) {

        Text(
            text = "Local selecionado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = state.address,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AssistChip(
            onClick = {},
            label = { Text("Distância: ${state.distance}") },
            leadingIcon = {
                Icon(
                    Icons.Default.NearMe,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(36.dp))
                .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.background
                    ),
                    endX = 1200f
                )
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text("Confirmar localização")
        }
    }
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchSection(
    modifier: Modifier = Modifier,
    state: LocationUiState,
    cameraPositionState: CameraPositionState,
    onQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit,
    goToUserLocation: () -> Unit,
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    onSearch = {},
                    colors = SearchBarDefaults.inputFieldColors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                    ),
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
                                        color = MaterialTheme.colorScheme.surface.copy(
                                            0.4f
                                        ),
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
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            VerticalDivider(
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            IconButton(onClick = onMenuClick) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(
                                        R.string.icon_navigation_back
                                    )
                                )
                            }
                        }
                    }
                )
            },
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {}


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedSavedSheet(
    modifier: Modifier,
    visible: Boolean,
    sheetState: SheetState,
    onClose: () -> Unit
) {

    val favorites = listOf(
        Place("Home", "221B Baker Street, London", icon = Icons.Default.Home),
        Place("Work", "One Canary Wharf, Floor 42", icon = Icons.Default.Work)
    )

    val savedLists = listOf(
        SavedList("Hidden Cafes", 12, "2.4 km away"),
        SavedList("Weekend Spots", 8, "4.5 km away")
    )

    val suggestions = listOf(
        Place("Roast & Brew", "Soho, London", "Cafeteria"),
        Place("Sky Garden", "City of London", "Natureza"),
        Place("Tate Modern", "South Bank", "Cultura")
    )

    var containerHeight by remember { mutableStateOf(0) }
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else containerHeight.toFloat(),
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "sheetOffset"
    )

    val scrimAlpha by animateFloatAsState(
        targetValue = if (visible) 0.3f else 0f,
        animationSpec = tween(300),
        label = "scrim"
    )
    Box(
        modifier = modifier.fillMaxSize()
    ) {

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
                .onSizeChanged {
                    containerHeight = it.height
                }

                .background(MaterialTheme.colorScheme.surface)
        ) {
            SavedPlacesContent(
                favorites = favorites,
                savedLists = savedLists,
                suggestions = suggestions
            )
        }
    }
}

@Composable
fun ModernBottomBar(
    modifier: Modifier = Modifier,
    selected: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.background
    ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 6.dp),
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
fun SavedPlacesContent(
    favorites: List<Place>,
    savedLists: List<SavedList>,
    suggestions: List<Place>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
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

        if (savedLists.isNotEmpty()) {
            item { SectionTitle("Saved Lists") }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(savedLists) { list ->
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
fun SavedListCard(list: SavedList) {
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
                "${list.count} items",
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
            Text(list.name, style = MaterialTheme.typography.bodyLarge)
            Text(list.distance, style = MaterialTheme.typography.bodySmall)
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

