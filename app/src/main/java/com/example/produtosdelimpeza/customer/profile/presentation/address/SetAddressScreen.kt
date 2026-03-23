package com.example.produtosdelimpeza.customer.profile.presentation.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class AddressUiState(
    val cep: String = "",
    val street: String = "",
    val number: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "SP"
)


@Preview(showBackground = true)
@Composable
fun PreviewSetLocation() {
    MaterialTheme {
        SetLocationScreenContainer()
    }
}


@Composable
fun SetLocationScreenContainer(
    onSelectMapClick: () -> Unit = {}
) {
    var state by remember {
        mutableStateOf(AddressUiState())
    }

    AddNewAddressScreen(
        state = state,
        onCepChange = { state = state.copy(cep = it) },
        onStreetChange = { state = state.copy(street = it) },
        onNumberChange = { state = state.copy(number = it) },
        onNeighborhoodChange = { state = state.copy(neighborhood = it) },
        onCityChange = { state = state.copy(city = it) },
        onStateChange = { state = state.copy(state = it) },
        onSearchCep = { /* buscar CEP */ },
        onSelectMap = { onSelectMapClick() },
        onConfirm = { /* salvar */ },
        onBack = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAddressScreen(
    state: AddressUiState,
    onCepChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onNeighborhoodChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onSearchCep: () -> Unit,
    onSelectMap: () -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onSelectMap,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Select Location on Map")
                    }
                }
            }

            // Divider text
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    "  OR ENTER MANUALLY  ",
                    style = MaterialTheme.typography.labelMedium
                )
                Divider(modifier = Modifier.weight(1f))
            }

            Text(
                "Manual Address",
                style = MaterialTheme.typography.titleLarge
            )

            // CEP + Search
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.cep,
                    onValueChange = onCepChange,
                    label = { Text("CEP") },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                )

                IconButton(
                    onClick = onSearchCep,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            OutlinedTextField(
                value = state.street,
                onValueChange = onStreetChange,
                label = { Text("Street") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.number,
                    onValueChange = onNumberChange,
                    label = { Text("Number") },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                )

                OutlinedTextField(
                    value = state.neighborhood,
                    onValueChange = onNeighborhoodChange,
                    label = { Text("Neighborhood") },
                    modifier = Modifier.weight(2f),
                    shape = CircleShape
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.city,
                    onValueChange = onCityChange,
                    label = { Text("City") },
                    modifier = Modifier.weight(2f),
                    shape = CircleShape
                )

                OutlinedTextField(
                    value = state.state,
                    onValueChange = onStateChange,
                    label = { Text("State") },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape
            ) {
                Text("Confirm Address")
            }
        }
    }
}