package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.map.presentation.AutocompletePlacesItem
import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimpOnSearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    isSearchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onQueryChanged: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color,
    focusedContainerColor: Color,
    mapState: List<PlaceSuggestion>,
    onConfirmPlace: (PlaceSuggestion) -> Unit,
) {
    SearchBar(
        modifier = modifier.fillMaxWidth(),
        expanded = isSearchActive,
        onExpandedChange = { onActiveChange(it) },
        windowInsets = WindowInsets(0),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText,
                onQueryChange = onQueryChanged,
                onSearch = {},
                expanded = isSearchActive,
                onExpandedChange = { onActiveChange(it) },
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = {
                    if (isSearchActive) {
                        IconButton(onClick = { onActiveChange(false) }) {
                            Icon(
                                Icons.Default.Close,
                                stringResource(R.string.close_component),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = SearchBarDefaults.inputFieldColors(
                    focusedContainerColor = focusedContainerColor,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = containerColor,
            dividerColor = Color.Transparent
        )
    ) {
        SuggestionsContent(
            places = mapState,
            onPlaceClick = {
                onConfirmPlace(it)
                onActiveChange(false)
            },
            color = MaterialTheme.colorScheme.background
        )
    }
}


@Composable
fun SuggestionsContent(
    modifier: Modifier = Modifier,
    places: List<PlaceSuggestion>,
    onPlaceClick: (PlaceSuggestion) -> Unit,
    color: Color,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = color,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
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
