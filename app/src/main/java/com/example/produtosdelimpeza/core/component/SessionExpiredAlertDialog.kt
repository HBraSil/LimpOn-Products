package com.example.produtosdelimpeza.core.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SessionExpiredAlertDialog(onSessionExpiredConfirmed: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = onSessionExpiredConfirmed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("OK")
            }
        },
        text = {
            Text(
                text = "Sua sessão expirou. Faça login novamente.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}