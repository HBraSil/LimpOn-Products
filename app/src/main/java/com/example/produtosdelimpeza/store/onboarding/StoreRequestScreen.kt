package com.example.produtosdelimpeza.store.onboarding

import com.example.produtosdelimpeza.R
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Store
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.produtosdelimpeza.core.component.LimpOnTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRequestScreen(onBackNavigation: () -> Unit, onSubmit: () -> Unit) {
    var nomeLoja by remember { mutableStateOf("") }
    var proprietario by remember { mutableStateOf("") }

    val categories = listOf(
        "Restaurante",
        "Mercado",
        "Padaria",
        "Loja de roupas",
        "Loja de eletrônicos",
        "Farmácia",
        "Outro"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text("Parceria para\nLojas e Empresas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) { Icon(Icons.Rounded.ArrowBackIosNew, null) }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth().padding(24.dp).height(58.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Enviar Cadastro Comercial")
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Rounded.Business, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // SEÇÃO 1: IDENTIDADE DA EMPRESA
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionHeader(title = "Dados da Empresa", icon = Icons.Rounded.Store)

                LimpOnTextField(
                    value = nomeLoja,
                    onValueChange = { nomeLoja = it },
                    label = R.string.business_name,
                    placeholder = R.string.business_name_example,
                    leadingIcon = { Icon(Icons.Rounded.Label, contentDescription = null) }
                )
                LimpOnTextField(
                    value = proprietario,
                    onValueChange = { proprietario = it },
                    label = R.string.owner_name,
                    placeholder = R.string.name_example,
                    leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) }
                )
            }

            // SEÇÃO 2: CONTATO E LOCALIZAÇÃO
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionHeader(title = "Contato e Endereço", icon = Icons.Rounded.LocationCity)

                LimpOnTextField(
                    value = "",
                    onValueChange = {},
                    label = R.string.business_whatsapp,
                    placeholder = R.string.cellphone_example,
                    leadingIcon = { Icon(Icons.Rounded.PhoneAndroid, contentDescription = stringResource(R.string.cellphone_icon)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // Campo Opcional com Tag Visual
                LimpOnTextField(
                    value = "",
                    onValueChange = {},
                    label = R.string.city_address,
                    placeholder = R.string.city_address_example,
                    leadingIcon = { Icon(Icons.Rounded.Map, contentDescription = null) }
                )
            }


            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Categoria do negócio", style = MaterialTheme.typography.titleMedium)

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de estabelecimento") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedCategory = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = selectedCategory == "Outro") {
                    OutlinedTextField(
                        value = customCategory,
                        onValueChange = { customCategory = it },
                        label = { Text("Descreva o tipo de loja") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }


            // SEÇÃO 3: FOTOS DA LOJA (O GRANDE DIFERENCIAL)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SectionHeader(title = "Fotos da Loja", icon = Icons.Rounded.PhotoCamera)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "(Opcional)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    "Fotos ajudam na aprovação do seu cadastro.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Slot de Upload Criativo
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // ... dentro da Column do Scaffold ...

                    PhotoSection(
                        onAddPhoto = { /* Abre o seletor de galeria do Android */ },
                        onRemovePhoto = { uri -> /* Remove da lista */ }
                    )

                }
            }
        }
    }
}

@Composable
fun PhotoSection(
    fotosSelecionadas: List<Uri> = emptyList(), // Lista de URIs das fotos
    onAddPhoto: () -> Unit,
    onRemovePhoto: (Uri) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${fotosSelecionadas.size} selecionadas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Carrossel de Fotos
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botão Adicionar (Sempre no início)
            item {
                Surface(
                    onClick = onAddPhoto,
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.size(100.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Rounded.AddAPhoto, null, tint = MaterialTheme.colorScheme.primary)
                        Text("Adicionar", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // Lista de fotos selecionadas
            items(fotosSelecionadas) { uri ->
                Box(modifier = Modifier.size(100.dp)) {
                    // Foto (Aqui usamos o AsyncImage do Coil)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Simulando a imagem com um placeholder colorido enquanto você não instala o Coil
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
                            Icon(Icons.Rounded.Image, null, Modifier.align(Alignment.Center))
                        }
                    }

                    // Botão para Remover
                    IconButton(
                        onClick = { onRemovePhoto(uri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-8).dp)
                            .size(28.dp)
                            .background(MaterialTheme.colorScheme.error, CircleShape)
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview
@Composable
fun teste() {
    StoreRequestScreen({}, {})
}