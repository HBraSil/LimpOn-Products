package com.example.produtosdelimpeza.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.core.domain.model.DiscountType


@Composable
fun DiscountTypeSection(
    currentDiscountValue: String = "",
    errorMessage: String? = "",
    onDiscountTypeAndValueChange: (DiscountType, String) -> Unit
) {
    val options = DiscountType.entries.filter { it != DiscountType.NONE }
    var selected by remember { mutableStateOf(DiscountType.NONE) }
    val focusRequester = remember { FocusRequester() }


    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Tipo de desconto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = selected == type,
                    onClick = {
                        selected = type
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    label = { Text(type.name) },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                        activeContentColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }

    AnimatedVisibility(
        visible = selected != DiscountType.NONE,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        DiscountValueField(
            value = currentDiscountValue,
            type = selected,
            errorMessage = errorMessage,
            focusRequester = focusRequester
        ) { discount ->
            onDiscountTypeAndValueChange(selected, discount)
        }
    }

    LaunchedEffect(selected) {
        if (selected != DiscountType.NONE) {
            focusRequester.requestFocus()
        }
    }
}


@Composable
private fun DiscountValueField(
    value: String,
    type: DiscountType?,
    errorMessage: String? = null,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
) {
    val label = if (type == DiscountType.PERCENTAGE)
        "Porcentagem de desconto. Ex: 10%"
    else
        "Valor do desconto. Ex: R$15,00"


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        trailingIcon = {
            if (type != null) {
                Text(
                    text = when (type) {
                        DiscountType.PERCENTAGE -> "%"
                        else -> "R$"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        isError = errorMessage != null,
        supportingText = {
            errorMessage?.let {
                Text(text = it)
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}