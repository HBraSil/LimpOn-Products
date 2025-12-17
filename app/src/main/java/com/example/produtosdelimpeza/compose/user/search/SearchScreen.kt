package com.example.produtosdelimpeza.compose.user.search

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.viewmodels.SearchHistoryViewModel
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign


@Composable
fun SearchScreen(
    searchScreenViewModel: SearchHistoryViewModel = viewModel(
        factory = SearchHistoryViewModel.Factory(LocalContext.current).factory
    ),
) {
    Scaffold(
       // bottomBar = { MainBottomNavigation(navControler) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.Top
        ) {

            SearchBarContainer(searchScreenViewModel)

            SearchContentBelowBar()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SearchBarContainer(searchScreenViewModel: SearchHistoryViewModel) {
    var query by remember { mutableStateOf(("")) }
    var expanded by remember { mutableStateOf(false) }
    var viewModel = searchScreenViewModel

    val products =
        listOf("Sabão", "Desinfetante", "Amaciante", "Detergente", "Limpa Alumínio", "Kiboa")
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(expanded) {
        if (!expanded) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    SearchBar(
        inputField = {
            Row(
                Modifier.background(Color.Transparent)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp), // Use a mesma forma da SearchBar
                    elevation = if (expanded) 4.dp else 4.dp, // Adicione a elevação aqui!
                ) {
                    TextField(
                        value = TextFieldValue(text = query, selection = TextRange(query.length)),
                        onValueChange = { query = it.text },
                        placeholder = {
                            Text(
                                text = "Busque por vendedor/produto",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    expanded = true
                                }
                            },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        searchScreenViewModel.addSearchItem(query)
                                        expanded = false
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Send,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        leadingIcon = {
                            if (!expanded && query.isEmpty()) {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            } else {
                                if (query.isNotEmpty()) {
                                    IconButton(onClick = { query = "" }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { expanded = false }) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardVoice,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        },
                        // ... outras propriedades do TextField ...
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        shape = RoundedCornerShape(20.dp),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                searchScreenViewModel.addSearchItem(query)
                                expanded = false
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                }
                if (expanded) {
                    Surface(
                        onClick = { expanded = false },
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp)
                            .height(25.dp)
                            .width(85.dp)
                            .align(Alignment.CenterVertically),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        shadowElevation = 1.dp,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.padding(horizontal = 6.dp).fillMaxWidth().background(Color.Transparent),
        colors = SearchBarDefaults.colors(
            dividerColor = Color.Transparent, // Remove o fundo do inputField

        ),
    ) {
        // Conteúdo dos resultados quando expandido
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .background(Color.Transparent)
                .padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            products.forEach { product ->
                Card(
                    onClick = {},
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = product,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onBackground

                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 30.dp, top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Buscas Recentes",
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            TextButton(
                onClick = {
                    viewModel.clearSearchHistory()
                },
                modifier = Modifier.padding(start = 40.dp, end = 10.dp),
            ) {
                Text(
                    text = "Limpar tudo",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (viewModel.history.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Sem histórico de navegação",
                        style = MaterialTheme.typography.displayMedium,
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                }
            } else {
                viewModel.history.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Button(
                            onClick = {
                                query = it
                                expanded = false // Fecha ao selecionar
                            },
                            modifier = Modifier
                                .weight(1f) // <-- O Button vai expandir para ocupar todo o espaço
                                .padding(start = 8.dp, end = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            // Conteúdo do botão em uma Row para alinhamento
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.History,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = it,
                                    color = Color.Black
                                )
                            }
                        }

                        IconButton(onClick = {
                            query = it
                            keyboardController?.show()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowOutward,
                                contentDescription = "Seta para a esquerda (rotacionada)",
                                // Aplica a rotação de 180 graus no ícone
                                modifier = Modifier.rotate(275f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------- Modelo simples de dados (substitua por seu modelo real) ----------
data class Seller(val id: String, val name: String, val initials: String, val color: Color)
data class Promo(val id: String, val title: String, val badge: String, val gradient: List<Color>)

// ---------- Tela principal que será colocada abaixo da SearchBar ----------
@Composable
fun SearchContentBelowBar(
    modifier: Modifier = Modifier,
    sellers: List<Seller> = sampleSellers(),
    promos: List<Promo> = samplePromos(),
    categories: List<Pair<String, Int>> = sampleCategories()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // Animated content area: shows either the "visual home" or the "typeahead results"
        if (searchQuery.isBlank()) {
            // When nothing is typed: show categories, sellers, promos
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                CategoryChipsSection(
                    categories = categories,
                    onCategoryClick = { cat ->
                        Toast.makeText(context, "Filtrar: $cat", Toast.LENGTH_SHORT).show()
                    }
                )
                SellerSection(
                    sellers = sellers,
                    onSellerClick = { seller ->
                        Toast.makeText(context, "Abrir: ${seller.name}", Toast.LENGTH_SHORT).show()
                    }
                )
                PromoSection(
                    promos = promos,
                    onPromoClick = { promo ->
                        Toast.makeText(context, "Promo: ${promo.title}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        } else {
            // When typing: show simulated search results and hide the visual sections
            SearchSuggestionsList(
                query = searchQuery,
                onItemClick = { item ->
                    Toast.makeText(context, "Selecionou: $item", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

// ---------- CategoryChipsSection ----------
@Composable
fun CategoryChipsSection(
    categories: List<Pair<String, Int>>,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 6.dp)
    ) {
        Text(
            text = "Categorias",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(start = 6.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // FlowRow permite quebrar linha automaticamente quando não cabe
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(PaddingValues(horizontal = 10.dp))
        ) {
            categories.forEach { (label) ->
                Chip(
                    label = label,
                    onClick = { onCategoryClick(label) }
                )
            }
        }
    }
}

@Composable
fun Chip(label: String, onClick: () -> Unit) {
    // Simple chip with soft background, rounded corners and slight elevation
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 0.dp),
        color = Color(0xFFF5F7F8),
        shadowElevation = 2.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            // optional icon placeholder
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDEAF6))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }
    }
}

// ---------- SellerSection ----------
@Composable
fun SellerSection(sellers: List<Seller>, onSellerClick: (Seller) -> Unit) {
    Column {
        Text(
            text = "Lojas próximas",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(sellers) { seller ->
                SellerItem(seller = seller, onClick = { onSellerClick(seller) })
            }
        }
    }
}

@Composable
fun SellerItem(seller: Seller, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(88.dp)
            .clickable { onClick() }
    ) {
        // Circular image placeholder with initials
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.linearGradient(listOf(seller.color, seller.color.copy(alpha = 0.85f)))
                )
                .shadow(6.dp, shape = RoundedCornerShape(18.dp))
        ) {
            Text(
                text = seller.initials,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = seller.name,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ---------- PromoSection ----------
@Composable
fun PromoSection(promos: List<Promo>, onPromoClick: (Promo) -> Unit) {
    Column {
        Text(
            text = "Destaques",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(promos) { promo ->
                PromoCard(promo = promo, onClick = { onPromoClick(promo) })
            }
        }
    }
}

@Composable
fun PromoCard(promo: Promo, onClick: () -> Unit) {
    val cardShape: Shape = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(140.dp)
            .clip(cardShape)
            .clickable { onClick() }
            .shadow(6.dp, shape = cardShape)
            .background(
                brush = Brush.linearGradient(colors = promo.gradient)
            )
            .padding(12.dp)
    ) {
        // Badge
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White.copy(alpha = 0.85f),
            tonalElevation = 4.dp,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(
                text = promo.badge,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Title
        Text(
            text = promo.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.align(Alignment.BottomStart),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

// ---------- Search suggestions when typing ----------
@Composable
fun SearchSuggestionsList(query: String, onItemClick: (String) -> Unit) {
    // Simulate a result list by filtering sample data
    val all = (sampleSellers().map { it.name } + samplePromos().map { it.title } + sampleCategories().map { it.first })
    val results = remember(query) {
        all.filter { it.contains(query, ignoreCase = true) }
            .ifEmpty { listOf("Nenhum resultado para \"$query\"") }
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(results) { _, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .clickable { onItemClick(item) },
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDEFF1))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

// ---------- Sample data for preview / testing ----------
fun sampleSellers() = listOf(
    Seller("s1", "Padaria do João", "PJ", Color(0xFFEF9A9A)),
    Seller("s2", "Mercado Verde", "MV", Color(0xFF80CBC4)),
    Seller("s3", "Açougue Silva", "AS", Color(0xFFFFCC80)),
    Seller("s4", "Loja Tech", "LT", Color(0xFF90CAF9)),
    Seller("s5", "Flor & Cia", "F&C", Color(0xFFD1C4E9))
)

fun samplePromos() = listOf(
    Promo("p1", "Desconto em bebidas", "Desconto 20%", listOf(Color(0xFFEF9A9A), Color(0xFFF48FB1))),
    Promo("p2", "Combo café da manhã", "Promo", listOf(Color(0xFF80CBC4), Color(0xFF4DB6AC))),
    Promo("p3", "Novo: carnes premium", "Novo", listOf(Color(0xFF90CAF9), Color(0xFF42A5F5)))
)

fun sampleCategories() = listOf(
    "Mercados" to 0,
    "Restaurantes" to 1,
    "Bebidas" to 2,
    "Padaria" to 3,
    "Flores" to 4,
    "Eletrônicos" to 5,
    "Pet Shop" to 6
)


@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}