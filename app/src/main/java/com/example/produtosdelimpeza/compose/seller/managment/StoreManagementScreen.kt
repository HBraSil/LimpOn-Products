package com.example.produtosdelimpeza.compose.seller.managment


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.produtosdelimpeza.compose.seller.managment.coupon_tab.CouponsTabContent
import com.example.produtosdelimpeza.compose.seller.managment.product_tab.ProductsTabContent
import com.example.produtosdelimpeza.compose.seller.managment.promotion_tab.PromotionsTabContent
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreManagementScreen(
    onNavigateToTabContentDetailScreenClick: (String) -> Unit,
    onNewProductClick: (String) -> Unit
) {
    val tabTitles = listOf("Produtos", "Cupons", "Promoções")

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabTitles.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        selectedTabIndex = pagerState.targetPage
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Central de Vendas",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Produtos, cupons e promoções",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                when (index) {
                    0 -> ProductsTabContent(
                        onProductClick = { onNavigateToTabContentDetailScreenClick(it)},
                        onNavigateToCreateProductScreenClick = { onNewProductClick(StoreScreen.PRODUCT_REGISTRATION.route) }
                    )
                    1 -> CouponsTabContent (
                        onCouponClick = { onNavigateToTabContentDetailScreenClick(it) },
                        onNavigateToCreateCouponScreenClick = { onNewProductClick(StoreScreen.COUPUN_REGISTRATION.route) }
                    )
                    2 -> PromotionsTabContent(
                        onPromotionClick = { onNavigateToTabContentDetailScreenClick(it) },
                        onNavigateToCreatePromotionScreenClick = { onNewProductClick(StoreScreen.PROMOTION_REGISTRATION.route)}
                    )
                }
            }
        }
    }
}