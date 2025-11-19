package com.example.produtosdelimpeza.compose.profile.coupons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// =================================================================================================
// 1. Data Model (CouponData.kt)
// =================================================================================================

data class Coupon(
    val id: Int,
    val title: String,
    val description: String,
    val discountValue: String,
    val merchant: String,
    val logoRes: Int, // Simulação de ID de recurso de logo
    val expiryDate: LocalDate,
    val isFeatured: Boolean = false,
    val status: CouponStatus = CouponStatus.ACTIVE,
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val category: String,
    val conditions: List<String> = emptyList()
) {
    val isExpired: Boolean
        @RequiresApi(Build.VERSION_CODES.O)
        get() = expiryDate.isBefore(LocalDate.now())

    val isUsed: Boolean
        get() = status == CouponStatus.USED

    val displayExpiryDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = expiryDate.format(DateTimeFormatter.ofPattern("dd/MM"))

    val statusText: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = when {
            isExpired -> "Expirado"
            isUsed -> "Utilizado"
            else -> "Válido"
        }
}

enum class CouponStatus {
    ACTIVE,
    USED,
    EXPIRED
}

enum class CouponSortOption(val label: String) {
    RECENT("Mais recentes"),
    EXPIRY_TIME("Válidos por (tempo)"),
    DISCOUNT_VALUE("Maior desconto")
}

object CouponDataSource {
    val categories = listOf("Todos", "Restaurantes", "Mercado", "Bebidas", "Próximos")

    @RequiresApi(Build.VERSION_CODES.O)
    val sampleCoupons = listOf(
        Coupon(
            id = 1,
            title = "50% OFF em pedidos acima de R$ 40",
            description = "Válido até 15/12 • Entrega grátis",
            discountValue = "50% OFF",
            merchant = "Restaurante Gourmet",
            logoRes = 1,
            expiryDate = LocalDate.now().plusDays(5),
            isFeatured = true,
            category = "Restaurantes",
            conditions = listOf("Pedido mínimo R$ 40", "Válido apenas para novos clientes", "Não cumulativo com outras promoções")
        ),
        Coupon(
            id = 2,
            title = "R$ 20 de desconto em compras acima de R$ 100",
            description = "Válido até 20/12 • 2km de distância",
            discountValue = "R$ 20",
            merchant = "Supermercado Econômico",
            logoRes = 2,
            expiryDate = LocalDate.now().plusDays(10),
            category = "Mercado",
            conditions = listOf("Pedido mínimo R$ 100", "Apenas para a primeira compra no mês")
        ),
        Coupon(
            id = 3,
            title = "30% OFF em todas as bebidas",
            description = "Válido até 01/12 • Expira em breve!",
            discountValue = "30% OFF",
            merchant = "Bar do Zé",
            logoRes = 3,
            expiryDate = LocalDate.now().plusDays(1),
            category = "Bebidas",
            conditions = listOf("Desconto máximo R$ 15", "Não cumulativo com outras promoções")
        ),
        Coupon(
            id = 4,
            title = "Frete Grátis em qualquer pedido",
            description = "Válido até 30/11 • 5km de distância",
            discountValue = "Frete Grátis",
            merchant = "Lanchonete Rápida",
            logoRes = 4,
            expiryDate = LocalDate.now().plusDays(20),
            category = "Restaurantes",
            conditions = listOf("Válido para pedidos acima de R$ 20")
        ),
        Coupon(
            id = 5,
            title = "15% OFF no seu primeiro pedido",
            description = "Válido até 01/01/2026",
            discountValue = "15% OFF",
            merchant = "Padaria Artesanal",
            logoRes = 5,
            expiryDate = LocalDate.now().plusDays(50),
            category = "Restaurantes",
            conditions = listOf("Apenas para novos usuários")
        ),
        // Cupom Expirado
        Coupon(
            id = 6,
            title = "25% OFF no seu lanche",
            description = "Expirou em 01/11",
            discountValue = "25% OFF",
            merchant = "Cafeteria Central",
            logoRes = 6,
            expiryDate = LocalDate.now().minusDays(10),
            status = CouponStatus.EXPIRED,
            category = "Restaurantes"
        ),
        // Cupom Utilizado
        Coupon(
            id = 7,
            title = "R$ 10 de desconto",
            description = "Utilizado em 05/11",
            discountValue = "R$ 10",
            merchant = "Farmácia Popular",
            logoRes = 7,
            expiryDate = LocalDate.now().plusDays(10),
            status = CouponStatus.USED,
            category = "Mercado"
        )
    )
}


// =================================================================================================
// 4. Main Screen (CouponsScreen.kt)
// =================================================================================================

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen() {
    // Estado de Carregamento Simulado
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000) // Simula o carregamento de dados
        isLoading = false
    }

    // Estado de UI
    val allCoupons = remember { CouponDataSource.sampleCoupons }
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedSortOption by remember { mutableStateOf(CouponSortOption.RECENT) }
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }

    // Lógica de Filtragem e Ordenação
    val filteredCoupons by remember(allCoupons, searchText, selectedCategory, selectedSortOption) {
        derivedStateOf {
            allCoupons
                .filter { !it.isExpired && !it.isUsed } // Apenas cupons ativos
                .filter {
                    // Filtro de busca
                    it.title.contains(searchText, ignoreCase = true) ||
                            it.merchant.contains(searchText, ignoreCase = true)
                }
                .filter {
                    // Filtro de categoria
                    if (selectedCategory == "Todos") true
                    else if (selectedCategory == "Próximos") it.expiryDate.isBefore(LocalDate.now().plusDays(3))
                    else it.category == selectedCategory
                }
                .sortedWith(
                    // Ordenação
                    when (selectedSortOption) {
                        CouponSortOption.RECENT -> compareByDescending { it.id }
                        CouponSortOption.EXPIRY_TIME -> compareBy { it.expiryDate }
                        CouponSortOption.DISCOUNT_VALUE -> compareByDescending { it.discountValue.length } // Simulação simples
                    }
                )
        }
    }

    val featuredCoupons = allCoupons.filter { it.isFeatured }
    val activeCouponCount = filteredCoupons.size

    Scaffold(
        topBar = { CouponsHeader(couponCount = activeCouponCount) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchAndFilterBar(
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                selectedSortOption = selectedSortOption,
                onSortOptionSelected = { selectedSortOption = it }
            )

            ShimmerEffect(
                isLoading = isLoading,
                contentAfterLoading = {
                    if (filteredCoupons.isEmpty()) {
                        EmptyState(
                            message = if (searchText.isNotEmpty() || selectedCategory != "Todos") "Nenhum cupom corresponde aos seus filtros." else "Você não tem cupons ativos no momento."
                        )
                    } else {
                        // Carrossel de Destaques
                        if (featuredCoupons.isNotEmpty()) {
                            FeaturedCarousel(
                                featuredCoupons = featuredCoupons,
                                onClick = { selectedCoupon = it }
                            )
                        }

                        // Lista/Grid Principal de Cupons
                        CouponGrid(
                            coupons = filteredCoupons,
                            onCouponClick = { selectedCoupon = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                },
                loadingContent = {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Simulação de Skeleton para o Carrossel
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(2) {
                                Spacer(
                                    modifier = Modifier
                                        .width(280.dp)
                                        .height(150.dp)
                                        .background(ShimmerBrush(), RoundedCornerShape(20.dp))
                                )
                            }
                        }
                        // Skeleton para a Grid
                        CouponGridSkeleton(count = 4)
                    }
                }
            )
        }
    }

    // Bottom Sheet para Detalhes do Cupom
    if (selectedCoupon != null) {
        CouponDetailsSheet(
            coupon = selectedCoupon!!,
            onDismiss = { selectedCoupon = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsHeader(couponCount: Int) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Cupons", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Você tem $couponCount cupons disponíveis",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Ação de voltar */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Voltar"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Ação de notificação */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun SearchAndFilterBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    selectedSortOption: CouponSortOption,
    onSortOptionSelected: (CouponSortOption) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text("Buscar cupons...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { onSearchTextChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpar busca")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Filter Chips
            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 4.dp)
                    .background(Color.Gray.copy(0.2f), RoundedCornerShape(12.dp)),
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(CouponDataSource.categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            }

            // Sort Dropdown
            SortDropdown(selectedSortOption, onSortOptionSelected)
        }
    }
}

@Composable
fun SortDropdown(
    selectedSortOption: CouponSortOption,
    onSortOptionSelected: (CouponSortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .padding(end = 6.dp)
    ) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(selectedSortOption.label) },
            leadingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Opções de ordenação"
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CouponSortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSortOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// --- Coupon Components ---

// Simulação de ícones para logos
@Composable
fun MerchantLogo(logoRes: Int, modifier: Modifier = Modifier) {
    val icon: ImageVector = when (logoRes) {
        1 -> Icons.Default.Restaurant
        2 -> Icons.Default.LocalGroceryStore
        3 -> Icons.Default.LocalBar
        4 -> Icons.Default.Fastfood
        5 -> Icons.Default.BakeryDining
        6 -> Icons.Default.LocalCafe
        7 -> Icons.Default.LocalPharmacy
        else -> Icons.Default.Info
    }
    Icon(
        imageVector = icon,
        contentDescription = "Logo do Estabelecimento",
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CouponCard(coupon: Coupon, onClick: (Coupon) -> Unit) {
    val isInactive = coupon.isExpired || coupon.isUsed
    val alphaValue = if (isInactive) 0.5f else 1f

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaValue)
            .clickable(enabled = !isInactive) { onClick(coupon) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(6.dp),
            ) {
                MerchantLogo(logoRes = coupon.logoRes)
                Spacer(modifier = Modifier.weight(1f))
                // Badge de Desconto
                AssistChip(
                    onClick = { /* Ignorar */ },
                    label = {
                        Text(
                            text = coupon.discountValue,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }

            // Logo e Nome do Estabelecimento
            Text(
                text = coupon.merchant,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Título do Cupom
            Text(
                text = coupon.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtexto: Validade / Condição
            val subtext = if (isInactive) coupon.statusText else "Válido até ${coupon.displayExpiryDate} • ${coupon.description.substringAfter("• ")}"
            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = if (coupon.expiryDate.isBefore(LocalDate.now().plusDays(3)) && !isInactive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botão de Ação
            Button(
                onClick = { onClick(coupon) },
                enabled = !isInactive,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(if (coupon.isUsed) "Utilizado" else if (coupon.isExpired) "Expirado" else "Usar Agora")
            }

            // Overlay de Status (para maior clareza em inativos)
            if (isInactive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = coupon.statusText.uppercase(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedCarousel(featuredCoupons: List<Coupon>, onClick: (Coupon) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "Destaques da Semana",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(featuredCoupons, key = { it.id }) { coupon ->
                FeaturedCouponCard(coupon = coupon, onClick = onClick)
            }
        }
    }
}

@Composable
fun FeaturedCouponCard(coupon: Coupon, onClick: (Coupon) -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick(coupon) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Destaque do Desconto
            Column {
                Text(
                    text = coupon.discountValue,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coupon.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedButton(onClick = { onClick(coupon) }) {
                    Text("Pegar Cupom")
                }
            }

            Text(
                text = "🎉",
                modifier = Modifier.padding(start = 10.dp, top = 4.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            // Ilustração/Emoji (Simulação)
        }
    }
}

// --- Layout and States ---

@Composable
fun EmptyState(
    title: String = "Nenhum cupom encontrado",
    message: String = "Parece que não há cupons disponíveis para os filtros selecionados. Tente ajustar sua busca.",
    icon: ImageVector = Icons.Default.SentimentDissatisfied,
    ctaText: String = "Ver restaurantes com ofertas",
    onCtaClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCtaClick) {
            Text(ctaText)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CouponGrid(
    coupons: List<Coupon>,
    onCouponClick: (Coupon) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determina o número de colunas baseado no tamanho da tela (simulação de responsividade)
    val columns = if (LocalWindowInfo.current.containerSize.height > 600) 2 else 1

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(coupons, key = { it.id }) { coupon ->
            CouponCard(coupon = coupon, onClick = onCouponClick)
        }
    }
}

// --- Coupon Details Sheet ---

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailsSheet(
    coupon: Coupon,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Text(
                text = coupon.merchant,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 4.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Destaque do Desconto
            Text(
                text = coupon.discountValue,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = coupon.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Validade
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Válido até: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = coupon.displayExpiryDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (coupon.isExpired) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Código do Cupom (Simulado)
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.8f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CUPOM12345", // Código mockado
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.background
                    )
                    ElevatedButton(
                        onClick = { /* Ação de copiar */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copiar Código", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar")
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Condições
            Text(
                text = "Condições de Uso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.heightIn(max = 150.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(coupon.conditions) { condition ->
                    Text(
                        text = "• $condition",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Ação Principal
            Button(
                onClick = { /* Ação de aplicar */ },
                enabled = !coupon.isExpired && !coupon.isUsed,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(

                    containerColor = MaterialTheme.colorScheme.secondary.copy(blue = 0.9f),
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(if (coupon.isUsed) "Cupom Utilizado" else if (coupon.isExpired) "Cupom Expirado" else "Aplicar Cupom", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// --- Shimmer Effect ---

@Composable
fun ShimmerEffect(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit
) {
    contentAfterLoading()
    /*if (isLoading) { QUANDO QUISER USAR O SHIMMER É SÓ VOLTAR A ATIVAR ISSO
        loadingContent()
    } else {
    }*/
}

@Composable
fun ShimmerBrush(showShimmer: Boolean = true, targetColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            targetColor.copy(alpha = 0.9f),
            targetColor.copy(alpha = 0.4f),
            targetColor.copy(alpha = 0.9f),
        )

        val transition = rememberInfiniteTransition(label = "Shimmer Transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = "Shimmer Animation"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@Composable
fun CouponCardSkeleton() {
    val brush = ShimmerBrush()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), // Altura aproximada do CouponCard real
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo e Nome do Estabelecimento Skeleton
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Spacer(
                        modifier = Modifier
                            .width(100.dp)
                            .height(16.dp)
                            .background(brush, RoundedCornerShape(4.dp))
                    )
                }

                // Badge de Desconto Skeleton
                Spacer(
                    modifier = Modifier
                        .width(80.dp)
                        .height(24.dp)
                        .background(brush, RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título do Cupom Skeleton
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Subtexto Skeleton
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
                    .background(brush, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de Ação Skeleton
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(brush, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun CouponGridSkeleton(count: Int = 4) {
    // Determina o número de colunas baseado no tamanho da tela (simulação de responsividade)
    val columns = if ( LocalWindowInfo.current.containerSize.width > 600) 2 else 1

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count) {
            CouponCardSkeleton()
        }
    }
}


// =================================================================================================
// 6. Previews
// =================================================================================================
