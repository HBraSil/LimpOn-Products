@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.produtosdelimpeza.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun ProfessionalDetailsScreen() {
    val scrollState = rememberLazyListState()

    val headerHeight = 350.dp
    val toolbarHeight = 64.dp
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }


    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            ContractOptionsContent { showSheet = false }
        }
    }


    val scrollProgress = remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex > 0) 1f
            else (scrollState.firstVisibleItemScrollOffset / (headerHeightPx - toolbarHeightPx)).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        bottomBar = { HireBottomBar() }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Hero Section (Header Dinâmico)
                item { HeroSection(scrollProgress.value) }

                // 4. Tabela de Preços Dinâmica
                item { QuickInfoBar{} }

                // 2. Quick Info Bar (Glassmorphism)
                item { PricingTable() }

                item { FullWidthSkillsAndAgenda() }

                item { SpecialtiesSectionListStyle() }

                // 5. Logística e Horários
                item { LogisticsSection() }

                // 6. Prova Social e Certificações
                item { SocialProofSection() }
            }

            // Toolbar Colapsável Superior (Fica por cima de tudo)
            ProfileToolbar(scrollProgress.value)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireBottomBar() {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        HireBottomSheet(
            sheetState = sheetState,
            onDismiss = { showSheet = false }
        )
    }

    BottomAppBar(
        modifier = Modifier.border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = {
                    // Aqui você pode abrir intent para WhatsApp
                },
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Whatsapp,
                    contentDescription = "WhatsApp"
                )
            }

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = { showSheet = true },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    "Contratar agora",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {

    var selectedType by remember { mutableStateOf("Hora") }
    var selectedDate by remember { mutableStateOf("Hoje") }
    var note by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        tonalElevation = 6.dp,
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {

            Text(
                "Agendar Serviço",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Quando?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                listOf("Hoje", "Amanhã", "Escolher data")
                    .forEach { date ->
                        FilterChip(
                            selected = selectedDate == date,
                            onClick = { selectedDate = date },
                            label = { Text(date) }
                        )
                    }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Tipo de contratação",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                listOf("Hora", "Diária", "Visita Técnica")
                    .forEach { type ->

                        Surface(
                            onClick = { selectedType = type },
                            shape = RoundedCornerShape(24.dp),
                            tonalElevation = if (selectedType == type) 4.dp else 0.dp,
                            border = if (selectedType == type)
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary
                                )
                            else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column {
                                    Text(
                                        type,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        when (type) {
                                            "Hora" -> "R$ 120 por hora"
                                            "Diária" -> "R$ 600 por dia"
                                            else -> "R$ 150 visita técnica"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (selectedType == type) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Observações (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                maxLines = 3
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Confirmar Solicitação")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}


@Composable
fun ProfileToolbar(progress: Float) {
    val backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = progress)
    val contentColor = if (progress > 0.5f) MaterialTheme.colorScheme.onSurface else Color.White

    TopAppBar(
        title = {
            if (progress > 0.7f) {
                Text("Ricardo Silva", style = MaterialTheme.typography.titleMedium)
            }
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBackIosNew, stringResource(R.string.icon_navigation_back), tint = contentColor)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Share, stringResource(R.string.icon_button_share), tint = contentColor)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
    )
}


@Composable
fun HeroSection(progress: Float) {
    Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
        Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
            Text("Galeria de Fotos (Pager)", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }

        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                0f to Color.Black.copy(alpha = 0.6f),
                0.3f to Color.Transparent,
                0.7f to Color.Transparent,
                1f to Color.Black.copy(alpha = 0.8f)
            )
        ))

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 40.dp)
                .graphicsLayer {
                    alpha = 1f - progress
                    scaleX = 1f - (progress * 0.2f)
                    scaleY = 1f - (progress * 0.2f)
                    translationY = progress * 100f
                }
        ) {
            Surface(
                shape = CircleShape,
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(100.dp)
            ) {
                Box(Modifier.background(Color.LightGray)) // Simulação de imagem
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ricardo Silva", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Verified, "Verificado", tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            }
        }
    }
}
@Composable
fun SpecialtiesSectionListStyle() {

    val specialties = listOf(
        "Quadros Elétricos",
        "Instalação Tri-Phase",
        "Iluminação LED",
        "Manutenção Preventiva",
        "Automação"
    )

    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = ""
    )


    Surface(
        onClick = { expanded = !expanded },
        shadowElevation = 10.dp,
        tonalElevation = if (expanded) 3.dp else 1.dp,
        color = if (expanded)
            Color.Gray
        else
            Color.Gray
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(24.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Especialidades",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).rotate(rotation)
                        .background(
                            Color.DarkGray,
                            CircleShape
                    ),
                    tint = Color.White
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = tween(300)
                ) + fadeIn(),
                exit = shrinkVertically(
                    animationSpec = tween(250)
                ) + fadeOut()
            ) {

                Column {

                    Spacer(Modifier.height(20.dp))

                    specialties.forEachIndexed { index, item ->

                        SpecialtyListItem(
                            title = item,
                            isLast = index == specialties.lastIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecialtyListItem(
    title: String,
    isLast: Boolean
) {

    Column(modifier = Modifier.padding(start = 20.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (!isLast) {
            Spacer(Modifier.height(18.dp))
        }
    }
}


@Composable
fun QuickInfoBar(onInfoClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .offset(y = (-50).dp)
            .fillMaxWidth()
            .clickable { onInfoClick() },
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .height(IntrinsicSize.Min), // Garante que o Divider preencha a altura correta
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                InfoItem("Eletricista", Icons.Default.Engineering)
            }

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(0.6f), // Altura controlada para não subir
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                InfoItem("4.9 (120)", Icons.Default.Star, Color(0xFFFFB300))
            }

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(0.6f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                InfoItem("8 anos", Icons.Default.History)
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FullWidthSkillsAndAgenda() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(8.dp))
                    Text("Agenda Disponível", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))

                val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    dias.forEach { dia ->
                        val isWeekend = dia == "Sáb"
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(dia, style = MaterialTheme.typography.labelSmall)
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (isWeekend) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.tertiary,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isWeekend) Icons.Default.Close else Icons.Default.Check,
                                    null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isWeekend) Color.Gray else MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Atendimento padrão das 08h às 19h. Emergências 24h via chat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun InfoItem(text: String, icon: ImageVector, color: Color = MaterialTheme.colorScheme.primary) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        Text(text, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun ContractOptionsContent(onOptionSelected: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding()
    ) {
        Text("Como deseja contratar?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        val options = listOf(
            Triple("Visita Técnica", "R$ 80,00", "Diagnóstico e orçamento no local"),
            Triple("Valor por Hora", "R$ 120,00", "Serviços rápidos e pontuais"),
            Triple("Diária Completa", "R$ 450,00", "Projetos maiores e manutenção")
        )

        options.forEach { (title, price, desc) ->
            Card(
                onClick = onOptionSelected,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(title, fontWeight = FontWeight.Bold)
                        Text(desc, style = MaterialTheme.typography.bodySmall)
                    }
                    Text(price, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onOptionSelected, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Text("Continuar para Agendamento")
        }
    }
}

@Composable
fun PricingTable() {
    OutlinedCard(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(Modifier.padding(24.dp)) {
            Text("Tabela de Preços Estimados", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PriceColumn("Visita", "R$ 80")
                PriceColumn("Hora", "R$ 120", highlight = true)
                PriceColumn("Diária", "R$ 450")
            }
        }
    }
}

@Composable
fun PriceColumn(label: String, value: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value,
            style = if(highlight) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if(highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LogisticsSection() {
    Column(modifier = Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Default.Map, null, modifier = Modifier.padding(12.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Área de Atendimento", fontWeight = FontWeight.Bold)
                Text("Até 15km de Moema, SP", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Color(0xFFE8F5E9), modifier = Modifier.size(48.dp)) {
                Icon(Icons.Default.Schedule, null, tint = Color(0xFF2E7D32), modifier = Modifier.padding(12.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Aberto agora", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(8.dp).background(Color(0xFF2E7D32), CircleShape))
                }
                Text("Seg à Sex: 08h às 19h", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


@Composable
fun SocialProofSection() {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text("Certificações", modifier = Modifier.padding(horizontal = 20.dp), fontWeight = FontWeight.Bold)
        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(4) {
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.size(80.dp)) {
                    Icon(Icons.Default.WorkspacePremium, null, modifier = Modifier.padding(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Text("Avaliações Recentes", modifier = Modifier.padding(horizontal = 20.dp), fontWeight = FontWeight.Bold)
        repeat(2) {
            Card(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(32.dp).background(Color.Gray, CircleShape))
                        Spacer(Modifier.width(8.dp))
                        Text("Maria Oliveira", fontWeight = FontWeight.Bold)
                    }
                    Text("Excelente profissional, resolveu o curto-circuito em 20 minutos. Recomendo muito!",
                        modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
