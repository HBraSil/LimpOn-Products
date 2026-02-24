package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.AppliesToCategorySelector
import com.example.produtosdelimpeza.core.component.DiscountTypeSection
import com.example.produtosdelimpeza.core.component.DurationSelector
import com.example.produtosdelimpeza.core.component.LimpOnRegistrationButton
import com.example.produtosdelimpeza.core.component.OperationResultOverlay
import com.example.produtosdelimpeza.core.component.SessionExpiredAlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionRegistrationScreen(
    onBackNavigation: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    promotionRegistrationViewModel: PromotionRegistrationViewModel = hiltViewModel()
) {

    val formState = promotionRegistrationViewModel.promotionFormState
    val isValid by promotionRegistrationViewModel.isValid.collectAsState()
    val state by promotionRegistrationViewModel.uiState.collectAsState()


    var selectedCategory by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }


    val context = LocalContext.current
    LaunchedEffect(state.showNoInternet) {
        if (state.showNoInternet) {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }
    if (state.showSessionExpired) {
        SessionExpiredAlertDialog(onNavigateToLogin)
    }
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackNavigation) {
                            Icon(Icons.AutoMirrored.Default.ArrowBackIos, null)
                        }
                    },
                    title = { Text("Criar Promoção") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                item { ImpactHeader() }
                item {
                    DiscountTypeSection(
                        //currentDiscountValue = formState.discountValueField.field,
                        onDiscountTypeAndValueChange = { discountType, discountValue ->
                            /*promotionRegistrationViewModel.updateDiscountType(
                                discountType
                            )*/

                            //promotionRegistrationViewModel.updateDiscountValue(
                                discountValue

                        }
                    )
                }
                item {
                    AppliesToCategorySelector(
                        options = listOf(
                            "Todos os produtos",
                            "Bebidas",
                            "Combos",
                            "Lanches",
                            "Sobremesas"
                        ),
                        selectedOption = selectedCategory,
                        onOptionSelected = {
                            //promotionRegistrationViewModel.updateCategory(it)
                            selectedCategory = it
                        }
                    )
                }
                item {
                    DurationSelector {
                        promotionRegistrationViewModel.updateDuration(it)
                    }
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider()
                }
                item {
                    PromotionBannerSection(
                        bannerBitmap = null,
                        onPickBannerClick = {}
                    )
                }
                item {
                    LimpOnRegistrationButton(
                        text = "Criar promoção",
                        isValid = true
                    ) {
                        //promotionRegistrationViewModel.createPromotion(formState)
                        showSuccess = true
                    }
                }
            }
        }

        if (showSuccess) {
            OperationResultOverlay(
                message = stringResource(R.string.promotion_created),
                onDismiss = { showSuccess = false }
            )
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


@Preview(showBackground = true)
@Composable
private fun PromotionRegistrationScreenPreview() {
    MaterialTheme {
        PromotionRegistrationScreen()
    }
}
