package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp


@Composable
fun LimpOnRegistrationButton(
    text: String,
    isValid: Boolean,
    loading: Boolean = false,
    onRegistrationClick: () -> Unit
) {
    val containerColor = if (loading) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.secondary
    }

    val contentColor = if (loading) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.background
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { onRegistrationClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            enabled = isValid,
            colors = ButtonDefaults.buttonColors(
                contentColor = contentColor,
                containerColor = containerColor
            )
        ) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .scale(0.7f),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}