package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color,
    searchBarContent: @Composable () -> Unit
) {
    SearchBar(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
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
                                null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = containerColor,
            dividerColor = Color.Transparent
        )
    ) {
        searchBarContent()
    }
}
