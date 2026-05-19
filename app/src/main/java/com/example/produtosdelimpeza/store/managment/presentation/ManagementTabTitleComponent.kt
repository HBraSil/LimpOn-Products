package com.example.produtosdelimpeza.store.managment.presentation


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.produtosdelimpeza.R


@Composable
fun ManagementTabTitleComponent(
    titleTab: String,
    label: String,
    @StringRes createPromotionLabel: Int,
    createPromotion: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(top = 12.dp, bottom = 4.dp, end = 4.dp).weight(1f)) {
            Text(
                text = titleTab,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
        Button(
            onClick = createPromotion,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            Spacer(Modifier.width(4.dp))
            Text(text = stringResource(createPromotionLabel))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagementTabTitleComponentPreview() {
    ManagementTabTitleComponent(
        titleTab = "Promoções",
        label = "Promoções inativas são exibidas apenas por 30 dias",
        createPromotionLabel = R.string.create_promotion
    )
}