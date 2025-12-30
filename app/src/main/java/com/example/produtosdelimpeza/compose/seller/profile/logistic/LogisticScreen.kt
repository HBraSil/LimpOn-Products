package com.example.produtosdelimpeza.compose.seller.profile.logistic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogisticScreen() {
    var isOpen by remember { mutableStateOf(true) }
    var prepTime by remember { mutableStateOf("30-45") }
    var minOrder by remember { mutableStateOf("20,00") }
    var deliveryRadius by remember { mutableFloatStateOf(5f) }
    var isDeliveryEnabled by remember { mutableStateOf(true) }
    var isPickupEnabled by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = stringResource(R.string.icon_navigate_back)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn {
            item {
                OperationCard(
                    isOpen = isOpen,
                    onOpenChange = { isOpen = it },
                    prepTime = prepTime,
                    onPrepTimeChange = { prepTime = it },
                    minOrder = minOrder,
                    onMinOrderChange = { minOrder = it }
                )
            }

            item {
                LogisticsCard(
                    radius = deliveryRadius,
                    onRadiusChange = { deliveryRadius = it },
                    isDelivery = isDeliveryEnabled,
                    onDeliveryChange = { isDeliveryEnabled = it },
                    isPickup = isPickupEnabled,
                    onPickupChange = { isPickupEnabled = it }
                )
            }
        }
    }
}

@Composable
fun OperationCard(
    isOpen: Boolean, onOpenChange: (Boolean) -> Unit,
    prepTime: String, onPrepTimeChange: (String) -> Unit,
    minOrder: String, onMinOrderChange: (String) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        // Header do Status
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Status da Loja", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    if (isOpen) "Recebendo pedidos" else "Loja fechada",
                    color = if (isOpen) Color(0xFF4CAF50) else Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = isOpen, onCheckedChange = onOpenChange)
        }

        HorizontalDivider(
            Modifier.padding(vertical = 16.dp),
            thickness = 0.5.dp,
            color = Color.LightGray
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo: Tempo de Preparo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Preparo Médio",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = prepTime,
                    onValueChange = onPrepTimeChange,
                    placeholder = { Text("Ex: 30-40") },
                    suffix = { Text("min", style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pedido Mínimo",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = minOrder,
                    onValueChange = onMinOrderChange,
                    prefix = { Text("R$ ", style = MaterialTheme.typography.bodySmall) },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}





@Composable
fun LogisticsCard(radius: Float, onRadiusChange: (Float) -> Unit, isDelivery: Boolean, onDeliveryChange: (Boolean) -> Unit, isPickup: Boolean, onPickupChange: (Boolean) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Logística", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Text("Raio de entrega: ${radius.toInt()} km", style = MaterialTheme.typography.bodyMedium)
            Slider(value = radius, onValueChange = onRadiusChange, valueRange = 1f..20f)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isDelivery, onCheckedChange = onDeliveryChange)
                Text("Oferecer Entrega Própria")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isPickup, onCheckedChange = onPickupChange)
                Text("Permitir Retirada no Local")
            }
        }
    }
}


@Composable
fun StoreVisualHeader() {
    Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
        // Banner
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color.LightGray)) {
            Icon(Icons.Default.Image, null, Modifier.align(Alignment.Center), tint = Color.White)
            IconButton(onClick = { /* Mudar Banner */ }, modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).background(Color.Black.copy(0.5f), CircleShape)) {
                Icon(Icons.Default.PhotoCamera, null, tint = Color.White)
            }
        }
        // Logo Overlay
        Box(modifier = Modifier.size(90.dp).align(Alignment.BottomStart).padding(start = 16.dp)) {
            Box(Modifier.fillMaxSize().clip(CircleShape).background(Color.White).border(2.dp, Color.White, CircleShape)) {
                Surface(Modifier.fillMaxSize().padding(4.dp), shape = CircleShape, color = Color(0xFFECE6F0)) {
                    Icon(Icons.Default.Store, null, Modifier.padding(16.dp), tint = Color.Gray)
                }
            }
            IconButton(onClick = { /* Mudar Logo */ }, modifier = Modifier.size(32.dp).align(Alignment.BottomEnd).background(MaterialTheme.colorScheme.primary, CircleShape)) {
                Icon(Icons.Default.Edit, null, Modifier.size(16.dp), tint = Color.White)
            }
        }
    }
}
