package com.example.produtosdelimpeza.autonomous.service_settings.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ServiceSettingsScreen() {
    val haptic = LocalHapticFeedback.current

    // Estados para Cidades
    var cityQuery by remember { mutableStateOf("") }
    val selectedCities = remember { mutableStateListOf("São Paulo", "Campinas") }
    val citySuggestions = listOf("São Bernardo", "São Caetano", "Santo André", "Santos", "Sorocaba")
        .filter { it.contains(cityQuery, ignoreCase = true) && !selectedCities.contains(it) }

    // Estados para Dias
    val daysOfWeek = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")
    val selectedDays = remember { mutableStateListOf("Seg", "Ter", "Qua", "Qui", "Sex") }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Definições de Atendimento", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- SEÇÃO 1: DIAS DE TRABALHO ---
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Dias de Disponibilidade",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { day ->
                        val isSelected = selectedDays.contains(day)
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFF0A3D62) else Color.White)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (isSelected) selectedDays.remove(day) else selectedDays.add(day)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                color = if (isSelected) Color.White else Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }


            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Cidades de Atendimento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                OutlinedTextField(
                    value = cityQuery,
                    onValueChange = { cityQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Digite o nome da cidade...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF00B894)) },
                    shape = RoundedCornerShape(32.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                // Sugestões Dinâmicas (Aparece enquanto digita)
                AnimatedVisibility(visible = cityQuery.isNotEmpty() && citySuggestions.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        tonalElevation = 2.dp,
                        shadowElevation = 4.dp
                    ) {
                        Column {
                            citySuggestions.forEach { city ->
                                ListItem(
                                    headlineContent = { Text(city) },
                                    modifier = Modifier.clickable {
                                        selectedCities.add(city)
                                        cityQuery = ""
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                )
                            }
                        }
                    }
                }


                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedCities.forEach { city ->
                        InputChip(
                            selected = true,
                            onClick = { selectedCities.remove(city) },
                            label = { Text(city) },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = null) },
                            shape = RoundedCornerShape(16.dp),
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = Color(0xFFE8F5E9),
                                selectedLabelColor = Color(0xFF00B894)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÃO SALVAR ---
            Button(
                onClick = { /* Salvar lógica */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B894))
            ) {
                Text("Confirmar Definições", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}