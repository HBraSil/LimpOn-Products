package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LoadingOverlay() {


    var dotsCount by remember {
        mutableIntStateOf(1)
    }

    LaunchedEffect(Unit) {
        while (true) {

            delay(300)

            dotsCount++

            if (dotsCount > 3) {
                dotsCount = 1
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(alpha = 0.45f)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = ".".repeat(dotsCount),
            fontSize = 42.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview
@Composable
fun LoadingPreview1 () {
    Surface(modifier = Modifier.fillMaxSize()) {
        LoadingOverlay()
    }
}