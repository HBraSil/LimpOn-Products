package com.example.produtosdelimpeza.professional.dashboard.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnCardProducts
import com.example.produtosdelimpeza.core.component.AddAndSubButton
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback



import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- DESIGN SYSTEM ---
val Navy = Color(0xFF0F172A)
val Slate = Color(0xFF64748B)
val Ice = Color(0xFFF8FAFC)
val AccentViolet = Color(0xFF6366F1)
val SoftEmerald = Color(0xFF10B981)


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FreelancerModernDashboard() {
    // ESTADOS (Dados Fixos e Interativos)
    var servicesDesc by remember { mutableStateOf("Especialista em automação residencial, instalações elétricas de alto padrão e manutenção preventiva.") }
    var meetsNeighbors by remember { mutableStateOf("Sim") }
    var worksWeekends by remember { mutableStateOf(false) }
    var expLevelMoreThanOne by remember { mutableStateOf<Boolean?>(null) }
    var yearsOfExp by remember { mutableFloatStateOf(1f) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {}

    Scaffold(
        containerColor = Ice,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // 1, 2, 3 (Avaliação) e 3 (Horário): HEADER INTEGRADO
            HeaderSection()

            Spacer(modifier = Modifier.height(32.dp))

            // 10: BOTÃO DE NAVEGAÇÃO (Sem BottomBar)
            NavigationActionSection()

            Spacer(modifier = Modifier.height(32.dp))

            // 4: CAMPO DE DESCRIÇÃO DOS SERVIÇOS (Sem Chips)
            SectionTitle("Serviços Oferecidos")
            OutlinedTextField(
                value = servicesDesc,
                onValueChange = { servicesDesc = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5, 6 e 7: GRID HARMÔNICO DE CONFIGURAÇÕES
            SettingsGrid(
                meetsNeighbors = meetsNeighbors,
                onNeighborsChange = { meetsNeighbors = it },
                worksWeekends = worksWeekends,
                onWeekendChange = { worksWeekends = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 8: LÓGICA DE EXPERIÊNCIA (Botão + Slider Animado)
            ExperienceInteractiveSection(
                isMoreThanOne = expLevelMoreThanOne,
                years = yearsOfExp,
                onSelect = { expLevelMoreThanOne = it },
                onYearChange = { yearsOfExp = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 9: PORTFÓLIO (Rolos Horizontais)
            PortfolioSection { galleryLauncher.launch("image/*") }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            // 2: Profissão
            Text("Roberto Silva", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Eletricista Premium", color = AccentViolet, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(4.dp))

            // 3: Horário de trabalho
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = Slate)
                Text(" 08h - 19h • ", fontSize = 12.sp, color = Slate)
                Text("Disponível até 19:00", fontSize = 12.sp, color = SoftEmerald, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NavigationActionSection() {
    Surface(
        onClick = { /* Navegar para solicitações */ },
        color = Navy,
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Central de Serviços", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Solicitações pendentes e confirmadas", color = Color.White.copy(0.6f), fontSize = 12.sp)
            }
            Box(
                modifier = Modifier.size(40.dp).background(Color.White.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun SettingsGrid(
    meetsNeighbors: String,
    onNeighborsChange: (String) -> Unit,
    worksWeekends: Boolean,
    onWeekendChange: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // 5: Cidades Vizinhas (Exposed Dropdown Like)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.weight(1f)) {
            Surface(
                onClick = { expanded = true },
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.4f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Cidades Vizinhas", fontSize = 11.sp, color = Slate)
                    Text(meetsNeighbors, fontWeight = FontWeight.Bold)
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Sim") }, onClick = { onNeighborsChange("Sim"); expanded = false })
                DropdownMenuItem(text = { Text("Não") }, onClick = { onNeighborsChange("Não"); expanded = false })
            }
        }

        // 7: Finais de Semana (Acesso Rápido)
        Surface(
            modifier = Modifier.weight(1f),
            color = if (worksWeekends) SoftEmerald.copy(0.1f) else Color.White,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, if (worksWeekends) SoftEmerald else Color.LightGray.copy(0.4f))
        ) {
            Row(
                Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sáb/Dom", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Switch(checked = worksWeekends, onCheckedChange = onWeekendChange, colors = SwitchDefaults.colors(checkedThumbColor = SoftEmerald))
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 6: Preços (Rolo Horizontal Criativo)
    SectionTitle("Tabela de Preços")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        val prices = listOf("Diária" to "R$ 450", "Visita" to "R$ 120", "Hora" to "R$ 85", "Urgência" to "R$ 200")
        items(prices) { price ->
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.3f))
            ) {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Text(price.first, fontSize = 10.sp, color = Slate)
                    Text(price.second, fontWeight = FontWeight.ExtraBold, color = Navy)
                }
            }
        }
    }
}

@Composable
fun ExperienceInteractiveSection(
    isMoreThanOne: Boolean?,
    years: Float,
    onSelect: (Boolean) -> Unit,
    onYearChange: (Float) -> Unit
) {
    SectionTitle("Tempo de Experiência")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ExperienceBtn("Menos de 1 ano", isMoreThanOne == false, Modifier.weight(1f)) { onSelect(false) }
        ExperienceBtn("Mais de 1 ano", isMoreThanOne == true, Modifier.weight(1f)) { onSelect(true) }
    }

    AnimatedVisibility(
        visible = isMoreThanOne == true,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Arraste para ajustar:", fontSize = 12.sp, color = Slate)
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
fun ExperienceBtn(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) AccentViolet else Color.White,
        border = BorderStroke(1.dp, if (selected) AccentViolet else Color.LightGray.copy(0.4f))
    ) {
        Text(
            label,
            modifier = Modifier.padding(14.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = if (selected) Color.White else Navy,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun PortfolioSection(onAddMedia: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        SectionTitle("Fotos/Vídeos Reais")
        TextButton(onClick = onAddMedia) { Text("+ Adicionar", color = AccentViolet) }
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Box(
                Modifier.size(100.dp, 130.dp).clip(RoundedCornerShape(24.dp)).background(Color.LightGray.copy(0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Slate)
            }
        }
        items(listOf(Color.DarkGray, Color.Gray, Color.LightGray)) { color ->
            Box(Modifier.size(100.dp, 130.dp).clip(RoundedCornerShape(24.dp)).background(color))
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = Slate,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}
