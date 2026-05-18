package com.example.produtosdelimpeza.customer.catalog.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.AddAndSubButton
import com.example.produtosdelimpeza.core.component.LimpOnCardProducts
import com.example.produtosdelimpeza.core.component.LimpOnSectionHeader
import com.example.produtosdelimpeza.core.component.ProductPrice
import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.domain.model.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productDetail: Product,
    store: Store,
    allProducts: List<Product>,
    isSheetProductOpen: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            isSheetProductOpen(false)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFD3D5DC)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
            )
        },
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AddAndSubButton()

                    Spacer(Modifier.weight(1f))

                    Button(
                        onClick = {}
                    ) {
                        Text(
                            text = "Adicionar ao carrinho",
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    ) { contentPadding ->

        val rowScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rowScrollState)
                .padding(paddingValues = contentPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sabao_lava_roupa),
                contentDescription = stringResource(R.string.product_presentation_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = productDetail.name,
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "5kg - (R$ 5,00/kg)",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            ProductPrice(
                price = productDetail.price,
                promotionalPrice = productDetail.promotionalPrice
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.see_all_payment_methods),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .clickable {},
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = stringResource(R.string.back_button),
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            ProductDescription(productDetail = productDetail)

            LimpOnSectionHeader(
                title = R.string.related_products,
                complementaryText = store.name,
                modifier = Modifier.padding(bottom = 10.dp, start = 8.dp),
                fontSize = 20.sp
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rowScrollState)
                    .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                allProducts.forEachIndexed { _, product ->
                    LimpOnCardProducts(
                        modifier = Modifier
                            .width(150.dp)
                            .wrapContentHeight()
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        product = product,
                        //isProductScreen = false,
                        onClickProduct = {}
                    )
                }
            }
        }
    }
}



@Composable
fun ProductDescription(
    productDetail: Product,
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 250)
    )

    Column(
        modifier = Modifier
            .padding(top = 26.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = Color(0xFFDCDBDB),
            )
    ) {

        Row(
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 12.dp)
            )

            Text(
                text = stringResource(R.string.product_description),
                modifier = Modifier.padding(
                    start = 10.dp,
                    top = 6.dp,
                    bottom = 6.dp,
                    end = 6.dp
                ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .rotate(rotation)
            )

        }
        AnimatedVisibility(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
            visible = expanded,
            enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
        ) {

            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                text = productDetail.description,
                style = MaterialTheme.typography.labelLarge,
                color = Color.DarkGray,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 12.dp, start = 10.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    ProductDetailScreen(
        productDetail = Product(
            id = "1",
            storeId = "store_001",
            name = "Wireless Mouse",
            price = 89.90,
            promotionalPrice = 69.90,
            description = "Ergonomic wireless mouse with silent clicks.",
        ),
        store = Store(
            id = "store_001",
            ownerId = "user_001",
            name = "Limpeza On",
            description = "Store description",
            storeOperationTime = emptyMap(),
        ),
        allProducts = listOf(
            Product(
                id = "1",
                storeId = "store_001",
                name = "Wireless Mouse",
                price = 89.90,
                promotionalPrice = 69.90,
                description = "Ergonomic wireless mouse with silent clicks.",
                classification = "Electronics",
                category = "Accessories",
                stockCount = 25
            ),
            Product(
                id = "2",
                storeId = "store_001",
                name = "Mechanical Keyboard",
                price = 299.99,
                promotionalPrice = 249.99,
                description = "RGB mechanical keyboard with blue switches.",
                classification = "Electronics",
                category = "Accessories",
                stockCount = 12
            ),
            Product(
                id = "3",
                storeId = "store_002",
                name = "Gaming Headset",
                price = 199.90,
                promotionalPrice = 159.90,
                description = "Surround sound gaming headset with noise cancellation.",
                classification = "Electronics",
                category = "Audio",
                stockCount = 18
            ),
        ),
        isSheetProductOpen = {}
    )
}