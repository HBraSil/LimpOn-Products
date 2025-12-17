package com.example.produtosdelimpeza.compose.user.home

import SwipeableCardOne
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.South
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.model.Product
import com.example.produtosdelimpeza.utils.toBrazilianCurrency
import com.example.produtosdelimpeza.viewmodels.CartViewModel
import com.example.produtosdelimpeza.viewmodels.NavigationLastUserModeViewModel

data class ItemInitialCard(
        val id: Int,
        val name: String,
        val image: Int,
        val city: String,
        val isPhysicalStore: Boolean,
        val sellerPassesByYourCity: Boolean = false
    )

    data class Category(
        val id: Int,
        val label: String,
        val icon: Int,
        val color: Color,
    )


// --- Sample data ---
private val sampleCategories = listOf(
    Category(1, "Limpeza", R.drawable.cleaning_icon, Color(0xFFBEECC8)),
    Category(2, "Restaurant", R.drawable.restaurant_icon, Color(0xFF9F4141)),
    Category(3, "Super", R.drawable.supermarket_icon, Color(0xFFFFF3C4)),
    Category(4, "Bebidas", R.drawable.drink_icon, Color(0xFFD6EEFF)),
    Category(5, "Pet", R.drawable.pet_icon, Color(0xFFFFE6E6)),
)

data class AddressItem(
    val id: String,
    val name: String? = null, // e.g., "Casa", "Trabalho"
    val fullAddress: String,
    val isDefault: Boolean = false,
    val eta: String? = null, // e.g., "25-35 min"
    val distance: String? = null // e.g., "1.2 km"
)

private val itemsLista = listOf(
    ItemInitialCard(
        1,
        "Raimundo",
        R.drawable.sabao_lava_roupa,
        "Tuntum - MA",
        true,
    ),
    ItemInitialCard(
        2,
        "Iran",
        R.drawable.highlight,
        "Barra do Corda - MA",
        false,
        true
    ),
    ItemInitialCard(
        3,
        "Francialdo",
        R.drawable.highlight,
        "Tuntum - MA",
        false,
        false
    ),
)

private val sampleProducts = listOf(
    Product(1, "Detergente Líquido", 62.71),
    Product(2, "Sabao Líquido", 6.49),
    Product(2, "Kiboa", 2.39),
    Product(2, "Brilho", 1.69),
    Product(2, "Amaciante Líquidp", 10.00),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    appLayoutViewModel: NavigationLastUserModeViewModel,
    navController: NavHostController,
    onCardSellerClick: (String) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    val totalQuantity by cartViewModel.totalQuantity.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    var expandedCard by remember { mutableStateOf(false) } // Assumindo que expanded é uma variável de estado
    var shortcutSelected by remember { mutableStateOf("1") }

    LaunchedEffect(Unit) {
        navigationLastUserModeViewModel.saveLastUserMode(profileMode = ProfileMode.CUSTOMER.mode)
    }
    LaunchedEffect(Unit) {
        appLayoutViewModel.setLayout(ProfileMode.CUSTOMER)

    }


    Scaffold(
        bottomBar = {
            Column {
                CartBottomBarScaffoldStyle(
                    items = totalQuantity,
                    total = totalPrice,
                    onOpenCart = { navController.navigate("cart") }
                )
            }
        },
    ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    //.verticalScroll(rememberScrollState())
                    .padding(paddingValues),
            ) {
                item {
                    Box(
                        modifier = Modifier
                        //.padding(paddingValues) // Aplica o padding do Scaffold
                    ) {

                        Column(
                            modifier = Modifier
                                // Aqui está a chave: Adicionamos um padding TOP DINÂMICO
                                // Ele garante que o conteúdo da Column SEMPRE comece logo
                                // abaixo da altura MINIMIZADA do CardDeLocalizacao.
                                .padding(top = 100.dp)
                        ) {
                            // Se o usuário puder rolar, use LazyColumn.

                            // --- Banner de Ofertas ---
                            BannerDeOfertas(
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Resto do conteúdo da tela abaixo do banner...
                            Spacer(modifier = Modifier.height(16.dp))
                        }


                        CardDeLocalizacao(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter),
                            address = AddressItem(
                                id = shortcutSelected,
                                name = "Casa",
                                fullAddress = "Rua Arsenio Da Silva 1",
                                eta = "25-35 min",
                                distance = "1.2 km"
                            ),
                            savedAddresses = listOf(
                                AddressItem(
                                    id = "1",
                                    name = "Casa",
                                    fullAddress = "Rua Arsenio Da Silva 1"
                                ),
                                AddressItem(
                                    id = "2",
                                    name = "Trabalho",
                                    fullAddress = "Rua Arsenio Da Silva 2"
                                )
                            ),
                            isExpanded = expandedCard,
                            onSelectShortcut = { shortcutSelected = it }
                        ) { expandedCard = !expandedCard } // Fixa no topo

                    }
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }

                item { SectionHeader(title = "Categorias") { } }
                item { CategoriesRow(categories = sampleCategories, onClick = {}) }

                item { Spacer(modifier = Modifier.height(30.dp)) }

                item { SectionHeader(
                    title = "Destaques para você",
                    actionLabel = "Ver todos",
                    onAction = onSeeAllClick,
                    textColor = MaterialTheme.colorScheme.secondary.copy(blue = 1.5f)
                ) }
                item { FeaturedProductsRow(products = sampleProducts, onAdd = {}) }

                item { Spacer(modifier = Modifier.height(30.dp)) }


                item { SectionHeader(title = "Vendedores", actionLabel = "") { } }

                items(itemsLista) {item ->
                    ItemCard(seller = item) {
                        onCardSellerClick(item.name)
                    }

                    Spacer(Modifier.height(16.dp))
                }
        }
    }

}

@Composable
fun FeaturedProductsRow(products: List<Product>, onAdd: (Product) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(start = 12.dp)
    ) {
        items(products) { productCart ->
            SampleFeaturedProducts(
                modifier = Modifier.padding(end = 12.dp),
                productCart = productCart,
                onAdd = onAdd
            )
        }
    }
}

@Composable
fun SampleFeaturedProducts(
    modifier: Modifier = Modifier,
    productCart: Product,
    onAdd: (Product) -> Unit
) {
    Card(
        modifier = modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.South,
                    contentDescription = productCart.name,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            Column {
                Text(
                    text = productCart.name, style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "R$ ${productCart.price.toBrazilianCurrency()}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    onClick = { onAdd(productCart) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Text(
                        text = "Adicionar",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


// --- Section header (title + action) ---
@Composable
fun SectionHeader(title: String, actionLabel: String = "", textColor: Color = MaterialTheme.colorScheme.onSurface, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        TextButton(onClick = onAction) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@Composable
fun CardDeLocalizacao(
    modifier: Modifier = Modifier,
    address: AddressItem? = null,
    savedAddresses: List<AddressItem> = emptyList(),
    isExpanded: Boolean,
    onEditAddress: (AddressItem?) -> Unit = {},
    onChangeAddress: () -> Unit = {},
    onSelectShortcut: (String) -> Unit = {},
    onToggleExpand: () -> Unit,
) {

    val roundedCornerShapeONne = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp, // Use a forma desejada para o card superior
        bottomEnd = 16.dp
    )
    val titleSaved = "Endereço salvo"
    val addAddressCTA = "Adicionar endereço"
    val etaPrefix = "Entrega ~ "


    Surface(
        shadowElevation = 12.dp,
        shape = roundedCornerShapeONne,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(1f) // Aplica o Z-Index animado
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onToggleExpand() }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* perfil */ }) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = "Olá, Hilquias!",
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )


                Spacer(Modifier.weight(1f))

                IconButton(onClick = { /* perfil */ }) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "notification",
                            tint = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rua Arsenio Da Silva 1",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
                Spacer(Modifier.width(20.dp))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onBackground

                )
            }


            // Animação para expandir/colapsar o conteúdo extra
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                // Transição de Saída (Ao Retrair)
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(500)) // Efeito de retração
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription =
                                if (address != null) "Painel de endereço expandido" else "Painel de adicionar endereço"
                        },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left: content column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            if (address != null) {
                                // Title row: icon + title + optional default star
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Ícone de endereço",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = address.name ?: titleSaved,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1
                                    )
                                    Spacer(Modifier.weight(1f))
                                    if (address.isDefault) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Endereço padrão",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                // Address body (selectable optional)
                                Text(
                                    text = address.fullAddress,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .semantics {
                                            contentDescription =
                                                "Endereço completo: ${address.fullAddress}"
                                        }
                                )

                                Spacer(Modifier.height(10.dp))

                                // ETA and distance row
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (!address.eta.isNullOrBlank()) {
                                        Text(
                                            text = "$etaPrefix${address.eta}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    if (!address.distance.isNullOrBlank()) {
                                        Text(
                                            text = address.distance,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                // Action buttons: Edit + Change
                                AddressActions(
                                    onEdit = { onEditAddress(address) },
                                    onChange = { onChangeAddress() }
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )

                                Spacer(Modifier.height(10.dp))

                                // Shortcuts chips (optional)
                                if (savedAddresses.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Local de entrega",
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        Row {
                                            savedAddresses.forEach { item ->
                                                AddressShortcutChip(
                                                    name = item.name ?: "Endereço",
                                                    isSelected = item.id == address.id,
                                                    onClick = {
                                                        onSelectShortcut(
                                                            item.id
                                                        )
                                                    }
                                                )

                                                Spacer(Modifier.width(18.dp))
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Empty state UI
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(text = "Nenhum endereço salvo", style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = "Adicione um endereço para ver informações de entrega, ETA e selecionar rapidamente em futuras compras.",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Button(
                                        onClick = { /* wiring: open add address flow */ },
                                        modifier = Modifier
                                            .semantics { contentDescription = "Adicionar endereço" }
                                    ) {
                                        Text(text = addAddressCTA)
                                    }
                                }
                            }
                        } // end Column

                        // Right: mini-map thumbnail or placeholder
                        MapThumbnail(
                            modifier = Modifier
                                .size(width = 170.dp, height = 120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentDescription = address?.fullAddress ?: "Mini mapa do endereço",
                            hasAddress = address != null
                        )
                    } // end Row
                }
            }
        }
    }
}



@Composable
private fun MapThumbnail(
    modifier: Modifier = Modifier,
    contentDescription: String,
    hasAddress: Boolean
) {
    Box(
        modifier = modifier
            .then(Modifier)
            .background(MaterialTheme.colorScheme.surface.copy(0.6f), shape = RoundedCornerShape(8.dp))
            .semantics { this.contentDescription = contentDescription }
    ) {
        if (hasAddress) {
            // Placeholder for a map preview. Replace with real image or Map snapshot.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // A fake thumbnail using icons/text to simulate real map preview
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Pin do mapa",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Mapa",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        } else {
            // Empty placeholder
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Placeholder mapa",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(6.dp))
                Text(text = "Sem mapa", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
private fun AddressShortcutChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tonalColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    AssistChip(
        onClick = onClick,
        label = { Text(text = name, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Atalho $name",
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.background.copy(0.7f) else MaterialTheme.colorScheme.background
            )
        },
        colors = AssistChipDefaults.assistChipColors(containerColor = tonalColor)
    )
}


@Composable
private fun AddressActions(
    onEdit: () -> Unit,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        // Edit button (primary)
        ElevatedButton(
            onClick = onEdit,
            modifier = Modifier.height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(0.2f)
            )
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar endereço", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.width(8.dp))
            Text(text = "Editar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun BannerDeOfertas(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // Aqui você pode adicionar um pequeno paddingTop negativo
            // se quiser uma leve sobreposição inicial
            //.offset(y = (-16).dp)
            .padding(top = 16.dp) // Exemplo: Adiciona um espaçamento para o banner
    ) {
        SwipeableCardOne(
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun CartBottomBarScaffoldStyle(
    items: Int,
    total: Double,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // anima entrada/saída vertical
    AnimatedVisibility(
        visible = items > 0,
        enter = slideInVertically(
            initialOffsetY = { it /* começa abaixo */ },
            animationSpec = tween(400)
        ) + fadeIn(animationSpec = tween(200)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(250)) + fadeOut(),
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(), // evita área da nav bar
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ícone com badge
                BadgedBox(
                    badge = {
                        if (items > 0) Badge { Text("$items") }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrinho"
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Total: R$ ${"%.2f".format(total)}", fontWeight = FontWeight.Bold)
                    Text("$items item(s)", style = MaterialTheme.typography.bodySmall)
                }

                ElevatedButton(
                    onClick = onOpenCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Ver sacola")
                }
            }
        }
    }
}

// --- Categories row ---
@Composable
fun CategoriesRow(categories: List<Category>, onClick: (Category) -> Unit) {
    LazyRow(contentPadding = PaddingValues(start = 12.dp)) {
        items(categories) { category ->
            CategoryCards(item = category, modifier = Modifier.padding(end = 12.dp)) {
                onClick(category)
            }
        }
    }
}

@Composable
fun CategoryCards(
    item: Category,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
     Card(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    item.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
     }
}


@Composable
fun ItemCard(modifier: Modifier = Modifier, seller: ItemInitialCard, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Avatar B1 Style
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(4.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painterResource(seller.image),
                    contentDescription = "Foto do vendedor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = seller.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = seller.city,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Badge de tipo do vendedor + status
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val typeEmoji = if (seller.isPhysicalStore) "🏪 Loja física" else "🧍 Ambulante"
                    Text(
                        text = typeEmoji,
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    val passColor by animateColorAsState(
                        if (seller.sellerPassesByYourCity) Color(0xFF0FA958)
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val passText = if (seller.sellerPassesByYourCity) {
                        "Passa na sua cidade ✅"
                    } else if (!seller.isPhysicalStore) {
                        "Não passa 🚫"
                    } else {
                        ""
                    }

                    Text(
                        text = passText,
                        style = MaterialTheme.typography.labelSmall,
                        color = passColor,
                        maxLines = 1
                    )
                }
            }

            // Botão favorito
            FavoriteButton()
        }
    }
}
@Composable
private fun FavoriteButton() {

    var isFavorite by remember { mutableStateOf(false) }
    val tint by animateColorAsState(
        if (isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
    )

    IconButton(
        onClick = {
            isFavorite = !isFavorite
        },
        modifier = Modifier
            .semantics { contentDescription = "Favoritar vendedor" }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = tint
        )
    }
}