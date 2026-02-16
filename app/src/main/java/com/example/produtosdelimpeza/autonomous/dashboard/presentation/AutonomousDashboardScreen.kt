package com.example.produtosdelimpeza.autonomous.dashboard.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke



val Navy = Color(0xFF0F172A)
val Slate = Color(0xFF64748B)
val Ice = Color(0xFFF8FAFC)
val AccentViolet = Color(0xFF8E44AD)
val SoftEmerald = Color(0xFF10B981)
val NavyBlue = Color(0xFF0A3D62)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutonomousDashboardScreen(onNavigateToServiceSettings: () -> Unit) {
    var servicesDesc by remember { mutableStateOf("Especialista em automação residencial, instalações elétricas de alto padrão e manutenção preventiva.") }
    var expLevelMoreThanOne by remember { mutableStateOf(false) }
    var yearsOfExp by remember { mutableFloatStateOf(1f) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {}

    val scrollState = rememberScrollState()
    val isExpanded by remember { derivedStateOf { scrollState.value == 0 } }

    var showPriceSheet by remember { mutableStateOf(false) }
    var selectedPriceToEdit by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem-vindo, Roberto Silva!", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Ice,
                    titleContentColor = Navy

                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToServiceSettings,
                containerColor = Navy,
                contentColor = Color.White,
                expanded = isExpanded,
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                text = { Text("Definições de Atendimento") },
                shape = RoundedCornerShape(20.dp)
            )
        },
        containerColor = Ice
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                bottom = padding.calculateBottomPadding() + 80.dp,
            )
        ) {
            item {
                HeaderSection()

                Spacer(modifier = Modifier.height(32.dp))

                SectionTitle("Serviços Oferecidos")
                OutlinedTextField(
                    value = servicesDesc,
                    onValueChange = { servicesDesc = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                SettingsGrid(onWeekendChange = { showPriceSheet = it })

                Spacer(modifier = Modifier.height(24.dp))

                ExperienceInteractiveSection(
                    isMoreThanOne = expLevelMoreThanOne,
                    years = yearsOfExp,
                    onSelect = { expLevelMoreThanOne = it },
                    onYearChange = { yearsOfExp = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PortfolioSection { galleryLauncher.launch("image/*") }
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }

    if (showPriceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPriceSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Editar valor: $selectedPriceToEdit", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    prefix = { Text("R$ ") },
                    placeholder = { Text("0,00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Button(
                    onClick = { showPriceSheet = false },
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Salvar Novo Valor")
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1: Foto de Perfil
        Box(modifier = Modifier.size(80.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) // Simulação de imagem
            // 3: Avaliação
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd),
                shape = CircleShape,
                color = SoftEmerald,
                shadowElevation = 4.dp
            ) {
                Text(
                    "4.9 ★",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            }
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text("Roberto Silva", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Eletricista Premium", color = AccentViolet, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = Slate)
                Text(" 08h - 19h • ", fontSize = 12.sp, color = Slate)
                Text("Disponível até 19:00", fontSize = 12.sp, color = SoftEmerald, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun SettingsGrid(
    onWeekendChange: (Boolean) -> Unit
) {
    SectionTitle("Tabela de Preços")
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PriceCard(
            modifier = Modifier.weight(1f).clickable { onWeekendChange(true) },
            label = "Diária",
            value = "R$ 450"
        )
        PriceCard(
            modifier = Modifier.weight(1f).clickable { onWeekendChange(true) },
            label = "Visita",
            value =  "R$ 80"
        )
        PriceCard(
            modifier =  Modifier.weight(1f).clickable { onWeekendChange(true) },
            label = "Hora",
            value = "R$ 120"
        )
    }
}


@Composable
fun PriceCard(label: String, value: String, modifier: Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold, color = NavyBlue)
        }
    }
}


@Composable
fun ExperienceInteractiveSection(
    isMoreThanOne: Boolean,
    years: Float,
    onSelect: (Boolean) -> Unit,
    onYearChange: (Float) -> Unit
) {
    SectionTitle("Tempo de Experiência")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
    ) {
        val modifierBtn = Modifier.weight(1f).height(48.dp)
        ExperienceToggleBtn("Menos de 1 ano", !isMoreThanOne, modifierBtn) { onSelect(false) }
        ExperienceToggleBtn("Mais de 1 ano", isMoreThanOne, modifierBtn) { onSelect(true) }
    }

    AnimatedVisibility(
        visible = isMoreThanOne,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Arraste para ajustar:", fontSize = 14.sp, color = Slate)
                Text("${years.toInt()} anos", fontWeight = FontWeight.Bold, color = AccentViolet)
            }
            Slider(
                value = years,
                onValueChange = onYearChange,
                valueRange = 1f..40f,
                colors = SliderDefaults.colors(thumbColor = AccentViolet, activeTrackColor = AccentViolet)
            )
        }
    }
}


@Composable
fun ExperienceToggleBtn(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) NavyBlue else Color.Transparent,
            contentColor = if (selected) Color.White else Color.Gray
        ),
        elevation = null
    ) {
        Text(text, fontSize = 13.sp)
    }
}


@Composable
fun PortfolioSection(onAddMedia: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        SectionTitle("Fotos/Vídeos Reais")
        TextButton(onClick = onAddMedia) { Text("+ Adicionar", color = AccentViolet) }
    }

    LazyRow(
        modifier = Modifier.padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listOf(Color.DarkGray, Color.Gray, Color.LightGray)) { color ->
            Box(Modifier.size(130.dp, 150.dp).clip(RoundedCornerShape(24.dp)).background(color))
        }
        item {
            Box(
                modifier = Modifier.size(130.dp, 150.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(
                        color = AccentViolet,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(34.dp.toPx())
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = AccentViolet,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Adicionar Mídia", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}


@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Slate,
        modifier = Modifier.padding(bottom = 12.dp, start = 16.dp)
    )
}