package com.example.produtosdelimpeza.customer.catalog.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.produtosdelimpeza.core.component.DesignCardCoupon


@Composable
fun CouponsTab() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // TODO: Implementar um botão de filtro
        Text(
            text = "Cupons disponíveis",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        DesignCardCoupon()
    }
}


@Preview
@Composable
private fun CouponsTabPreview() {
    CouponsTab()
}
