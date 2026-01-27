package com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.domain.DiscountType
import com.example.produtosdelimpeza.core.domain.ValidityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationCouponScreen(
    onBackNavigation: () -> Unit = {},
    registrationCouponViewModel: RegistrationCouponViewModel = hiltViewModel()
) {
    val formState by registrationCouponViewModel._couponFormState.collectAsState()
    val isValid by registrationCouponViewModel._isValid.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = stringResource(R.string.icon_navigate_back)
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
        LazyColumn (
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item{ CouponInsightCard() }
            item{
                CouponTextField(
                    label = "Código do cupom",
                    placeholder = "EX: PRIMEIRA10",
                    supporting = "O cliente digitará esse código no checkout",
                    currentCouponCode = formState.couponCode
                ) {
                    registrationCouponViewModel.onEvent(AddCouponField.CouponCodeField(it))
                }
            }
            item{
                DiscountTypeSection(
                    currentDiscountValue = formState.discountValue,
                    onDiscountTypeAndValueChange = { discountType, discountValue ->
                        registrationCouponViewModel.onEvent(AddCouponField.DiscountTypeField(discountType))
                        registrationCouponViewModel.onEvent(AddCouponField.DiscountValueField(discountValue))
                    }
                )
            }
            item{
                ValiditySection{
                    registrationCouponViewModel.onEvent(AddCouponField.ValidityField(it))
                }
                Spacer(Modifier.height(6.dp))
                HorizontalDivider()
            }
            item{ CouponPreviewSection() }
            item{
                Button(
                    onClick = { /* Criar cupom */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = "Criar cupom")
                }
            }
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
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
fun DiscountTypeSection(
    currentDiscountValue: String = "",
    onDiscountTypeAndValueChange: (DiscountType, String) -> Unit,
) {
    var selectedType by remember { mutableStateOf(DiscountType.NONE) }
    var hasError by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Tipo de desconto",
            fontWeight = FontWeight.SemiBold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DiscountTypeOption(
                title = "Percentual",
                subtitle = "Ex: 10%",
                selected = selectedType == DiscountType.PERCENTAGE,
                onClick = {
                    selectedType = DiscountType.PERCENTAGE
                    hasError = false
                }
            )

            DiscountTypeOption(
                title = "Valor fixo",
                subtitle = "Ex: R$ 15",
                selected = selectedType == DiscountType.FIXED,
                onClick = {
                    selectedType = DiscountType.FIXED
                    hasError = false
                }
            )
        }

        AnimatedVisibility(
            visible = selectedType != DiscountType.NONE,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            DiscountValueField(
                value = currentDiscountValue,
                onValueChange = {
                    onDiscountTypeAndValueChange(selectedType, it)
                    hasError = !isValidDiscount(it, selectedType)
                },
                type = selectedType,
                hasError = hasError
            )
        }
    }
}


@Composable
fun DiscountTypeOption(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        tonalElevation = if (selected) 4.dp else 1.dp,
        color = if (selected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else
            MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DiscountValueField(
    value: String,
    onValueChange: (String) -> Unit,
    type: DiscountType?,
    hasError: Boolean
) {
    val label = if (type == DiscountType.PERCENTAGE)
        "Porcentagem de desconto"
    else
        "Valor do desconto"

    val placeholder = if (type == DiscountType.PERCENTAGE)
        "Ex: 10"
    else
        "Ex: 15,00"

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = hasError,
        supportingText = {
            if (hasError) {
                Text(
                    text = if (type == DiscountType.PERCENTAGE)
                        "Use valores entre 1 e 90%"
                    else
                        "Informe um valor válido"
                )
            } else {
                Text(
                    text = if (type == DiscountType.PERCENTAGE)
                        "O desconto será aplicado em porcentagem"
                    else
                        "O valor será descontado do total do pedido"
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}

fun isValidDiscount(
    value: String,
    type: DiscountType?
): Boolean {
    val number = value.toFloatOrNull() ?: return false

    return when (type) {
        DiscountType.PERCENTAGE -> number in 1f..90f
        DiscountType.FIXED -> number > 0f
        else -> false
    }
}


@Composable
fun ValiditySection(onValidityChange: (ValidityType) -> Unit) {

    var selectedValidity by remember { mutableStateOf<ValidityType?>(ValidityType.DAYS_7) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            text = "Validade",
            fontWeight = FontWeight.SemiBold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ValidityChip(
                label = "7 dias",
                selected = selectedValidity == ValidityType.DAYS_7
            ) { onValidityChange(ValidityType.DAYS_7) }

            ValidityChip(
                label = "15 dias",
                selected = selectedValidity == ValidityType.DAYS_15
            ) { onValidityChange(ValidityType.DAYS_15) }

            ValidityChip(
                label = "30 dias",
                selected = selectedValidity == ValidityType.DAYS_30
            ) { onValidityChange(ValidityType.DAYS_30) }

            ValidityChip(
                label = "Personalizado",
                selected = selectedValidity == ValidityType.CUSTOM
            ) { selectedValidity = ValidityType.CUSTOM }
        }

        AnimatedVisibility(
            visible = selectedValidity == ValidityType.CUSTOM,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            CustomValiditySection()
        }
    }
}

@Composable
fun ValidityChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            else
                MaterialTheme.colorScheme.surface,
            labelColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
fun CustomValiditySection() {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {

        Text(
            text = "Período personalizado",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            DateBox(
                label = "Início",
                value = "Hoje"
            )

            DateBox(
                label = "Fim",
                value = "30/06/2026"
            )
        }
    }
}

@Composable
fun DateBox(
    label: String,
    value: String
) {
    Surface(
        modifier = Modifier
            //.weight(1f)
            .clickable { /* Abrir DatePicker futuramente */ },
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
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
    RegistrationCouponScreen()
}