package com.example.produtosdelimpeza.store.registration_coupon.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.produtosdelimpeza.core.component.SessionExpiredAlertDialog
import com.example.produtosdelimpeza.core.ui.util.asString
import com.example.produtosdelimpeza.store.component.SuccessRegistrationOverlay
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponRegistrationScreen(
    onBackNavigation: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    couponRegistrationViewModel: CouponRegistrationViewModel = hiltViewModel()
) {
    val formState = couponRegistrationViewModel.couponFormState
    val uiState by couponRegistrationViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current


    if (uiState.showSessionExpired) {
        SessionExpiredAlertDialog {
            couponRegistrationViewModel.signOut()
            onNavigateToLogin()
        }
    }

    LaunchedEffect(uiState.showNoInternet) {
        if (uiState.showNoInternet) {
            Toast.makeText(context, "Sem conexão com a internet", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            delay(1500)
            listState.animateScrollToItem(0)
            couponRegistrationViewModel.reset()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = stringResource(R.string.icon_navigation_back)
                        )
                    }
                },
                title = {
                    Column {
                        Text("Criar cupom")
                        Text(
                            "Atraia mais clientes com desconto",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 10.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { CouponInsightCard() }
                item {
                    CouponTextField(
                        label = "Código do cupom",
                        placeholder = "EX: PRIMEIRA10",
                        supporting =
                            if (formState.codeField.error != null)
                                formState.codeField.error.asString()
                            else
                                stringResource(R.string.coupon_code_information),
                        error = formState.codeField.error?.asString(),
                        currentCouponCode = formState.codeField.field
                    ) {
                        couponRegistrationViewModel.updateCouponCode(it)
                    }
                }
                item {
                    DiscountTypeSection(
                        currentDiscountValue = formState.discountValueField.field,
                        errorMessage = formState.discountValueField.error?.asString(),
                        onDiscountTypeAndValueChange = { discountType, discountValue ->
                            couponRegistrationViewModel.updateDiscountType(discountType)
                            couponRegistrationViewModel.updateDiscountValue(discountValue)
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
                        selectedOption = formState.categoryField.field,
                        onOptionSelected = {
                            couponRegistrationViewModel.updateCategory(it)
                        }
                    )
                }
                item {
                    DurationSelector(
                        titleSection = "Validade",
                        selectedDuration = formState.durationField.field.toIntOrNull() ?: 7
                    ) {
                        couponRegistrationViewModel.updateDuration(it)
                    }
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider()
                }
                //item { SuccessToast(visible = showToast, onComplete = { showToast = false }) }
                item { CouponPreviewSection() }
                item {
                    LimpOnRegistrationButton(
                        text = "Criar cupom",
                        loading = uiState.isLoading,
                        isValid = formState.formIsValid
                    ) {
                        couponRegistrationViewModel.createCoupon()
                    }
                }
            }
            SuccessRegistrationOverlay(
                message = stringResource(R.string.coupon_created),
                visible = uiState.success
            )
        }
    }
}


@Composable
fun CouponInsightCard() {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF2563EB).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = Color(0xFF2563EB)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    "Dica",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E40AF)
                )
                Text(
                    "Cupons de 10% aumentam a conversão em até 20% em dias de menor movimento.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun CouponTextField(
    label: String,
    placeholder: String,
    supporting: String,
    error: String? = null,
    currentCouponCode: String = "",
    onCouponCodeChange: (String) -> Unit
) {
    OutlinedTextField(
        value = currentCouponCode,
        onValueChange = onCouponCodeChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        supportingText = { Text(supporting) },
        isError = error != null,
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}



@Composable
fun CouponPreviewSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Preview do cupom",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        CouponPreviewCard()
    }
}


@Composable
fun CouponPreviewCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "DESCONTO ESPECIAL",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )

            Text(
                text = "20% OFF",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Válido até 30/06/2026",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp
            )
        }
    }
}


@Preview
@Composable
fun Test(){
    CouponRegistrationScreen()
}