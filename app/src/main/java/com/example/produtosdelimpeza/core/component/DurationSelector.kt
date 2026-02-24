package com.example.produtosdelimpeza.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.core.domain.model.Expiration


@Composable
fun DurationSelector(
    onValidityChange: (Int?) -> Unit,
) {
    var expiration by remember { mutableStateOf(Expiration.NONE) }
    val expirationOffersFiltereds = Expiration.entries.filter { it != Expiration.NONE }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Duração da promoção",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            expirationOffersFiltereds.forEach { duration ->

                AssistChip(
                    onClick = {
                        expiration = duration
                        onValidityChange(duration.day)
                    },
                    label = { Text(duration.name) },
                    leadingIcon = {
                        if (duration == expiration) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    },
                    elevation = AssistChipDefaults.assistChipElevation(
                        elevation = 6.dp
                    ),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        labelColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }

    AnimatedVisibility(
        visible = expiration == Expiration.CUSTOM,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        CustomValiditySection{
            onValidityChange(it)
        }
    }
}


@Composable
fun CustomValiditySection(onDayChange: (Int) -> Unit) {

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

            OutlinedTextField(
                value = "",
                onValueChange = { onDayChange(it.toInt()) },
                label = { Text("") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Text(
                        text = "Dias",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}
