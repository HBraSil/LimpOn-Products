package com.example.produtosdelimpeza.customer.home.presentation

import SwipeableCardOne
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.South
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.SectionHeader
import com.example.produtosdelimpeza.core.data.entity.ProductEntity
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import com.example.produtosdelimpeza.core.ui.formatter.currencyFormatter
import com.example.produtosdelimpeza.customer.cart.presentation.CartViewModel


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


private val sampleProductEntities = listOf(
    ProductEntity(1, "Detergente Líquido"),
    ProductEntity(2, "Sabao Líquido"),
    ProductEntity(2, "Kiboa"),
    ProductEntity(2, "Brilho"),
    ProductEntity(2, "Amaciante Líquidp"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onCardSellerClick: (String) -> Unit = {},
    navigateToNotifications: () -> Unit = {},
    navigateToAddressScreen: () -> Unit = {},
    goToProfessionalProfile: () -> Unit
) {
    val totalPrice by cartViewModel.cartTotalPrice.collectAsState(initial = 0.0)
    val totalQtd by cartViewModel.cartQuantity.collectAsState(initial = 0)
    val listOfStores by homeViewModel.listOfStores.collectAsStateWithLifecycle()
    val mainAddres by homeViewModel.mainAddress.collectAsStateWithLifecycle()

    val user by homeViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        navigationLastUserModeViewModel.saveLastUserMode(ProfileMode.LoggedIn.CustomerSection)
    }

    Scaffold(
        bottomBar = {
            Column {
                CartBottomBarScaffoldStyle(
                    items = totalQtd,
                    total = totalPrice,
                    onNavigateToCart = { navController.navigate("cart") }
                )
            }
        },
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Box {
                    Column(
                        modifier = Modifier.padding(top = 100.dp)
                    ) {
                        BannerDeOfertas(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    CardDeLocalizacao(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        address = mainAddres,
                        userName = user.name,
                        onNavigateToNotifications = navigateToNotifications,
                        onGoToAddressScreen = navigateToAddressScreen
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            item { SectionHeader(title = R.string.categories) { } }
            item { CategoriesRow(categories = sampleCategories, onClick = {}) }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item { SectionHeader(title = R.string.highlitghs, actionLabel = R.string.see_all) {} }
            item { FeaturedProductsRow(productEntities = sampleProductEntities, onAdd = {}) }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item { SectionHeader(title = R.string.services, actionLabel = R.string.see_all) { } }
            item {
                val provider = ServiceProvider(
                    name = "João Silva",
                    category = "Eletricista",
                    rating = 4.8,
                    distanceKm = 2.4,
                    servicesDone = 12,
                    description = "Especialista em quadros de força, instalações residenciais e manutenção preventiva.",
                    isOnline = true
                )

                ServiceCard(
                    provider = provider,
                    onHireClick = { },
                    goToProfessionalProfile = goToProfessionalProfile
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item { SectionHeader(title = R.string.stores, actionLabel = R.string.see_all) { } }
            items(listOfStores) { item ->
                ItemCard(store = item) {
                    onCardSellerClick(item.id)
                }
            }
        }
    }
}


@Composable
fun FeaturedProductsRow(productEntities: List<ProductEntity>, onAdd: (ProductEntity) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(start = 12.dp)
    ) {
        items(productEntities) { productCart ->
            SampleFeaturedProducts(
                modifier = Modifier.padding(end = 12.dp),
                productEntityCart = productCart,
                onAdd = onAdd
            )
        }
    }
}

@Composable
fun SampleFeaturedProducts(
    modifier: Modifier = Modifier,
    productEntityCart: ProductEntity,
    onAdd: (ProductEntity) -> Unit,
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
                    contentDescription = productEntityCart.name,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))

            Column {
                Text(
                    text = productEntityCart.name, style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "R$ ${currencyFormatter.format(productEntityCart.price)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    onClick = { onAdd(productEntityCart) },
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




@Composable
fun CardDeLocalizacao(
    modifier: Modifier = Modifier,
    userName: String,
    address: Address? = null,
    onNavigateToNotifications: () -> Unit,
    onGoToAddressScreen: () -> Unit,
) {
    val roundedCornerShapeONne = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )


    Surface(
        shadowElevation = 12.dp,
        shape = roundedCornerShapeONne,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onGoToAddressScreen() }
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
                    text = "Olá, $userName!",
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )


                Spacer(Modifier.weight(1f))

                IconButton(onClick = onNavigateToNotifications) {
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
                Icon(
                    imageVector = address?.icon ?: Icons.Default.LocationOn,
                    contentDescription = "Localização",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = address?.street ?: address?.neighborhood ?: address?.city ?: "Endereço não definido",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(30.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface

                )
            }
        }
    }
}


@Composable
fun BannerDeOfertas(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
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
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = items > 0,
        enter = slideInVertically(
            initialOffsetY = { it },
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
                    onClick = onNavigateToCart,
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
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
fun ItemCard(
    modifier: Modifier = Modifier,
    store: Store,
    onClick: () -> Unit
) {
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
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(4.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painterResource(R.drawable.drink_icon),
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
                    text = store.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = store.category,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = store.address,
                        style = MaterialTheme.typography.labelSmall,
//                        color = passColor,
                        maxLines = 1
                    )
                }
            }

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


data class ServiceProvider(
    val name: String,
    val category: String,
    val rating: Double,
    val distanceKm: Double,
    val servicesDone: Int,
    val description: String,
    val isOnline: Boolean
)


@Composable
fun ServiceCard(
    provider: ServiceProvider,
    onHireClick: () -> Unit,
    goToProfessionalProfile: () -> Unit
) {
    Card(
        onClick = goToProfessionalProfile,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = provider.name,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = provider.category,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${provider.servicesDone} anos exp.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "⭐ ${provider.rating}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.location_icon),
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${provider.distanceKm} km",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = provider.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onHireClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text("Contratar Serviço")
            }
        }
    }
}