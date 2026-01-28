package com.example.produtosdelimpeza.compose.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation.DateBox


@Composable
fun DurationSelector(onValidityChange: (ExpirationOffer) -> Unit) {
    var expiration by remember { mutableStateOf(ExpirationOffer.DAYS_7) }
    val expirationOffersFiltered = ExpirationOffer.entries.filter { it != ExpirationOffer.NONE }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Duração da promoção",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            expirationOffersFiltered.forEach { duration ->
                AssistChip(
                    onClick = {
                        expiration = duration
                        onValidityChange(duration)
                    },
                    label = { Text(duration.name) },
                    leadingIcon = {
                        if (expiration == duration) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }

    AnimatedVisibility(
        visible = expiration == ExpirationOffer.CUSTOM,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        CustomValiditySection()
    }
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
