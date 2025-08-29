package com.example.produtosdelimpeza.compose.notifications

import android.app.Notification
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotificationScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = contentPadding.calculateTopPadding())
        ) {
            Text("Olá mundo")
        }
    }
}