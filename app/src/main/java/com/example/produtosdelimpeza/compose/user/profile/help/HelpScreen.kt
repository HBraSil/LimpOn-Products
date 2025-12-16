package com.example.produtosdelimpeza.compose.user.profile.help

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

// -----------------------------------------------------------
// 1. MOCK DATA E ESTRUTURAS
// -----------------------------------------------------------

data class Faq(
    val id: String,
    val question: String,
    val answer: String,
    val category: String
)

object FaqData {
    val categories = listOf("Todos", "Pedidos", "Pagamento", "Entregas", "Conta", "Segurança")

    val allFaqs = listOf(
        Faq("f1", "Como acompanho meu pedido?", "Para acompanhar, vá em 'Meus Pedidos' e selecione o pedido ativo. Lá você verá o mapa de rastreamento em tempo real.", "Pedidos"),
        Faq("f2", "Quais métodos de pagamento são aceitos?", "Aceitamos Cartão de Crédito/Débito (Visa, Mastercard, Elo) e Pix. Cartões de débito exigem verificação adicional no app do seu banco.", "Pagamento"),
        Faq("f3", "O que fazer se o entregador não chegar?", "Verifique o rastreamento. Se o status for 'Entregue' e você não recebeu, clique em 'Reportar Problema' no app.", "Entregas"),
        Faq("f4", "Como faço para alterar meu e-mail?", "Vá em 'Meu Perfil' > 'Dados Pessoais'. Será necessário um código de verificação para confirmar a mudança.", "Conta"),
        Faq("f5", "Como solicitar um reembolso?", "Reembolsos são processados automaticamente em caso de cancelamento. Use a opção 'Reportar Problema' para iniciar a solicitação.", "Pagamento"),
        Faq("f6", "Minha conta foi suspensa, o que fazer?", "Contas são suspensas por violação dos termos. Entre em contato via chat para iniciar a análise.", "Segurança"),
    )
}

// -----------------------------------------------------------
// 2. COMPOSABLE RAIZ: HELPSCREEN
// -----------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit = {}) {
    // 1. Estados da Tela
    var searchTerm by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Todos") }
    var showTroubleshooterSheet by rememberSaveable { mutableStateOf(false) }

    // 2. Lógica de Filtragem (MOCK)
    val filteredFaqs = remember(searchTerm, selectedCategory) {
        FaqData.allFaqs
            .filter { faq ->
                if (selectedCategory != "Todos" && faq.category != selectedCategory) return@filter false
                if (searchTerm.isBlank()) return@filter true

                val normalizedTerm = searchTerm.lowercase(Locale.getDefault())
                faq.question.lowercase(Locale.getDefault()).contains(normalizedTerm) ||
                        faq.answer.lowercase(Locale.getDefault()).contains(normalizedTerm)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajuda & Suporte", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header e Search Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp) // Padding generoso
            ) {
                Text(
                    "Encontre respostas rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
                SearchBar(
                    searchTerm = searchTerm,
                    onSearchTermChange = { searchTerm = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Categorias Rápidas
            CategoryChips(
                categories = FaqData.categories,
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    searchTerm = "" // Limpa a busca ao trocar de categoria
                },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Lista Principal (FAQs ou Empty State)
            if (filteredFaqs.isEmpty() && searchTerm.isNotBlank()) {
                EmptyState(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    searchTerm = searchTerm,
                    onContactSupport = { showTroubleshooterSheet = true }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento consistente
                ) {
                    if (searchTerm.isBlank() && selectedCategory == "Todos") {
                        item { FeaturedTopicsRow() }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            TroubleshootFlowCard(onClick = { showTroubleshooterSheet = true })
                        }
                        item {
                            Text(
                                "Perguntas Frequentes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                            )
                        }
                    }

                    items(filteredFaqs, key = { it.id }) { faq ->
                        FaqItemExpandable(
                            faq = faq,
                            highlightTerm = searchTerm
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        ContactOptions()
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }
        }
    }

    if (showTroubleshooterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTroubleshooterSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            TroubleshooterSheet(onClose = { showTroubleshooterSheet = false })
        }
    }
}

// -----------------------------------------------------------
// 3. COMPOSABLES MODULARES
// -----------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchBar(
    searchTerm: String,
    onSearchTermChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val animatedElevation by animateDpAsState(
        targetValue = if (isFocused) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = 150), label = "SearchElevationAnim"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = animatedElevation,
            tonalElevation = animatedElevation,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = onSearchTermChange,
                placeholder = { Text("O que você precisa encontrar?") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = if (searchTerm.isNotEmpty()) {
                    { IconButton(onClick = { onSearchTermChange("") }) { Icon(Icons.Default.Clear, contentDescription = "Limpar busca") } }
                } else null,
                singleLine = true,
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (isFocused) 1f else 0f), // Borda esconde quando não focado
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Sugestões Rápidas (Pílulas)
        AnimatedVisibility(
            visible = isFocused && searchTerm.isBlank(),
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {
            FlowRow(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Rastreamento", "Reembolso", "Conta suspensa").forEach { term ->
                    AssistChip(
                        onClick = { onSearchTermChange(term) },
                        label = { Text(term) },
                        leadingIcon = { Icon(Icons.Default.FlashOn, null, Modifier.size(18.dp)) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category, fontWeight = if (category == selectedCategory) FontWeight.SemiBold else FontWeight.Normal) },
                leadingIcon = if (category == selectedCategory) {
                    { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = true,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun FeaturedTopicsRow() {
    Text(
        "Artigos em Destaque",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(end = 24.dp)
    ) {
        val features = listOf(
            "Rastrear pedido" to Icons.Default.LocationOn,
            "Reembolsos" to Icons.Default.MonetizationOn,
            "Alterar Senha" to Icons.Default.Lock
        )
        items(features) { (title, icon) ->
            ElevatedCard(
                modifier = Modifier.width(180.dp).heightIn(min = 100.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                onClick = { /* Navegar para artigo */ }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("Ver instruções", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FaqItemExpandable(
    faq: Faq,
    highlightTerm: String
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var usefulFeedback by remember { mutableStateOf<Boolean?>(null) }

    // Animação da cor do card ao expandir
    val cardColor by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainerLow,
        label = "FaqCardColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Pergunta (Header Clicável)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { expanded = !expanded }
                    )
                    .padding(20.dp), // Espaçamento interno grande
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AnnotatedTextHighlight(faq.question, highlightTerm),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val rotationAngle by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    label = "RotationAnim",
                    animationSpec = tween(300)
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Fechar resposta" else "Abrir resposta",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            // Resposta (Conteúdo Expansível)
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = AnnotatedTextHighlight(faq.answer, highlightTerm),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Feedback de Utilidade e CTA
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Esta resposta foi útil?", style = MaterialTheme.typography.labelLarge)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FeedbackButton(Icons.Default.ThumbUp, usefulFeedback == true) { usefulFeedback = true }
                            FeedbackButton(Icons.Default.ThumbDown, usefulFeedback == false) { usefulFeedback = false }
                        }
                    }

                    // CTA Continuação (SÓ aparece após feedback)
                    AnimatedVisibility(
                        visible = usefulFeedback != true, // Se não foi útil (False ou Null/Não clicado)
                        enter = fadeIn(tween(400)),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(onClick = { /* Abre o Modal de Contato/Troubleshooter */ }) {
                            Text("Ainda preciso de ajuda")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackButton(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f, label = "FeedbackScale")

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp) // Área de toque de 48dp
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp * scale))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TroubleshooterSheet(onClose: () -> Unit) {
    var step by rememberSaveable { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            "Solução Guiada: ${when(step) { 1 -> "Categoria"; 2 -> "Problema"; else -> "Instruções" }}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        AnimatedContent(targetState = step, label = "TroubleshooterStep") { targetStep ->
            Column(modifier = Modifier.fillMaxWidth()) {
                when (targetStep) {
                    1 -> StepOneUI { step = 2 }
                    2 -> StepTwoUI { step = 3 }
                    3 -> StepThreeUI(onClose)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (step > 1) {
                TextButton(onClick = { step-- }) { Text("Voltar") }
            }
        }
    }
}

@Composable
private fun StepOneUI(onNext: () -> Unit) {
    Text("1. Selecione a categoria que melhor se encaixa no seu problema:", modifier = Modifier.padding(bottom = 12.dp))
    FaqData.categories.filter { it != "Todos" }.chunked(2).forEach { row ->
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            row.forEach { category ->
                OutlinedButton(
                    onClick = onNext,
                    modifier = Modifier.weight(1f)
                ) { Text(category) }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun StepTwoUI(onNext: () -> Unit) {
    Text("2. Identifique a situação exata:", modifier = Modifier.padding(bottom = 12.dp))
    listOf("Pedido Atrasado", "Erro no Pix", "Cupom Não Aplicado").forEach { issue ->
        ListItem(
            headlineContent = { Text(issue) },
            leadingContent = { Icon(Icons.Default.QuestionMark, null) },
            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) },
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(onClick = onNext)
        )
        Divider(color = MaterialTheme.colorScheme.surfaceContainerHigh)
    }
}

@Composable
private fun StepThreeUI(onClose: () -> Unit) {
    Text("3. Aqui está sua solução imediata:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
    Text("Siga o passo a passo detalhado abaixo para resolver o problema de '[Problema Selecionado]'. Se o problema persistir por 15 minutos, o chat será liberado.", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
        Text("Entendi, fechar")
    }
}


@Composable
fun TroubleshootFlowCard(onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Support, contentDescription = null, tint = MaterialTheme.colorScheme.background, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Solução Guiada", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background)
                Text("Encontre o passo-a-passo para problemas comuns.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.surface)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.surface)
        }
    }
}

@Composable
fun ContactOptions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(20.dp)
    ) {
        Text("Precisa de Contato Humano?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Chat
        ContactOptionItem(
            icon = Icons.AutoMirrored.Filled.Chat,
            title = "Chat ao Vivo",
            subtitle = "Resposta esperada: < 1 min",
            color = MaterialTheme.colorScheme.primary,
            onClick = { /* Ação de iniciar chat */ }
        )
        // Linha divisória fina, utilizando SurfaceContainer para contraste sutil
        Divider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        // Ligação
        ContactOptionItem(
            icon = Icons.Default.Call,
            title = "Ligar para Suporte (24/7)",
            subtitle = "Tempo de espera: ~ 3 min",
            color = MaterialTheme.colorScheme.secondary,
            onClick = { /* Ação de ligação */ }
        )
    }
}

@Composable
fun ContactOptionItem(icon: ImageVector, title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone maior para destaque
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier, searchTerm: String, onContactSupport: () -> Unit) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SentimentDissatisfied,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Nenhum artigo encontrado para \"$searchTerm\".",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            "Tente simplificar a busca ou use nossa Solução Guiada para encontrar a ajuda certa.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(onClick = onContactSupport, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Iniciar Solução Guiada")
        }
    }
}

// -----------------------------------------------------------
// 4. FUNÇÃO AUXILIAR DE DESTAQUE DE TEXTO
// -----------------------------------------------------------

@Composable
fun AnnotatedTextHighlight(fullText: String, highlightTerm: String) = buildAnnotatedString {
    if (highlightTerm.isBlank()) {
        append(fullText)
        return@buildAnnotatedString
    }

    val lowerText = fullText.lowercase(Locale.getDefault())
    val lowerTerm = highlightTerm.lowercase(Locale.getDefault())

    var currentStart = 0
    var nextMatch = lowerText.indexOf(lowerTerm, currentStart)

    val highlightStyle = SpanStyle(
        background = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
        color = MaterialTheme.colorScheme.onSurface
    )

    while (nextMatch != -1) {
        append(fullText.substring(currentStart, nextMatch))

        val endMatch = nextMatch + highlightTerm.length
        withStyle(highlightStyle) {
            append(fullText.substring(nextMatch, endMatch))
        }
        currentStart = endMatch
        nextMatch = lowerText.indexOf(lowerTerm, currentStart)
    }
    append(fullText.substring(currentStart))
}

// -----------------------------------------------------------
// 5. PREVIEWS
// -----------------------------------------------------------

@Preview(showBackground = true, name = "Help UserScreen - Light")
@Composable
fun PreviewHelpScreenLight() {
    MaterialTheme {
        HelpScreen()
    }
}