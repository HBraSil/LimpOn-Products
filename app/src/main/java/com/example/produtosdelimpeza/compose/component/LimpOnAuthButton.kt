package com.example.produtosdelimpeza.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LimpOnAuthButton(
    modifier: Modifier = Modifier,
    text: Int,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = White,
    onClickNewBtn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = onClickNewBtn,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 100.dp),
            enabled = enabled && !loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(topStart = 15.dp, bottomEnd = 15.dp)
        ) {
            Text(stringResource(text))
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