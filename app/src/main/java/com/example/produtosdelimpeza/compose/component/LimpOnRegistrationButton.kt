package com.example.produtosdelimpeza.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LimpOnRegistrationButton(text: String, isValid: Boolean, onRegistrationClick: () -> Unit) {
    Button(
        onClick = { onRegistrationClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        enabled = isValid,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}