package com.example.produtosdelimpeza.compose.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LimpOn",
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = FontFamily(
                    Font(R.font.montserrat_extrabold_italic)
                )
            )

            Text(
                text = stringResource(R.string.about_main_text),
                modifier = Modifier.padding(vertical = 30.dp)
                )
        }
    }
}


@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen()
}