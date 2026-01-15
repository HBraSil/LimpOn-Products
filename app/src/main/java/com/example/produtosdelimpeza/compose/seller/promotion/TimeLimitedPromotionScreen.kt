package com.example.produtosdelimpeza.compose.seller.promotion

import androidx.compose.animation.togetherWith

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private enum class DiscountType(val label: String) {
    PERCENTAGE("Porcentagem"),
    FIXED("Valor fixo")
}

private enum class PromotionDuration(val label: String) {
    ONE_HOUR("1 hora"),
    THREE_HOURS("3 horas"),
    TODAY("Hoje"),
    CUSTOM("Personalizado")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLimitedPromotionScreen(onBackNavigation: () -> Unit = {}) {

    var discountType by remember { mutableStateOf(DiscountType.PERCENTAGE) }
    var duration by remember { mutableStateOf(PromotionDuration.THREE_HOURS) }
    var selectedCategory by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(Icons.AutoMirrored.Default.ArrowBackIos, null)
                    }
                },
                title = { Text("Criar Promoção") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item{ ImpactHeader() }
            item{
                DiscountTypeSelector(
                    selected = discountType,
                    onSelect = { discountType = it }
                )
            }
            item{
                DurationSelector(
                    selected = duration,
                    onSelect = { duration = it }
                )
            }
            item{
                PromotionAppliesToSelector(
                    options = listOf(
                        "Todos os produtos",
                        "Bebidas",
                        "Combos",
                        "Lanches",
                        "Sobremesas"
                    ),
                    selectedOption = selectedCategory,
                    onOptionSelected = { selectedCategory = it }
                )
            }
            item{
                PromotionBannerSection(
                    bannerBitmap = null,
                    onPickBannerClick = {}
                )
            }
            item{
                Button(
                    onClick = { /* Criar cupom */ },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = "Criar promoção")
                }
            }
        }
    }
}


@Composable
private fun ImpactHeader() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Bolt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "Aumente as vendas agora",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "Promoções curtas criam senso de urgência",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/* ----------------------- DISCOUNT TYPE ----------------------- */

@Composable
private fun DiscountTypeSelector(
    selected: DiscountType,
    onSelect: (DiscountType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            "Tipo de desconto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        SingleChoiceSegmentedButtonRow {
            DiscountType.entries.forEach { type ->
                SegmentedButton(
                    selected = selected == type,
                    onClick = { onSelect(type) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = type.ordinal,
                        count = DiscountType.entries.size
                    ),
                    label = { Text(type.label) },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                        activeContentColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}

/* ----------------------- DURATION ----------------------- */

@Composable
private fun DurationSelector(
    selected: PromotionDuration,
    onSelect: (PromotionDuration) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            "Duração da promoção",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PromotionDuration.entries.forEach { duration ->
                AssistChip(
                    onClick = { onSelect(duration) },
                    label = { Text(duration.label) },
                    leadingIcon = {
                        if (selected == duration) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun PromotionBannerPicker(
    bannerBitmap: ImageBitmap?,
    onPickBannerClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (bannerBitmap != null) {
                Image(
                    bitmap = bannerBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                EmptyBannerState(onPickBannerClick)
            }
        }
    }
}

@Composable
fun PromotionBannerSection(
    bannerBitmap: ImageBitmap?,
    onPickBannerClick: () -> Unit
) {
    Column {
        Text(
            text = "Banner promocional (opcional)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Você também pode adicionar um banner para essa promoção. " +
                    "Promoções com imagens chamam mais atenção e costumam ter mais cliques.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        PromotionBannerPicker(
            bannerBitmap = bannerBitmap,
            onPickBannerClick = onPickBannerClick
        )
    }
}

@Composable
private fun EmptyBannerState(
    onPickBannerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.7f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.Image,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.background
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Adicione um banner",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.background
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Opcional, mas recomendado",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.background.copy(0.7f)

        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onPickBannerClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Text(
                text = "Selecionar imagem"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionAppliesToSelector(
    title: String = "Onde essa promoção vale",
    subtitle: String = "Selecione a categoria de produtos",
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text("Escolha uma categoria")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun TimeLimitedPromotionScreenPreview() {
    MaterialTheme {
        TimeLimitedPromotionScreen()
    }
}
