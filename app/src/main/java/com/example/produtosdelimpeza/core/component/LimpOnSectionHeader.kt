package com.example.produtosdelimpeza.core.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R


@Composable
fun LimpOnSectionHeader(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes actionLabel: Int? = null,
    complementaryText: String? = null,
    icon: ImageVector? = null,
    fontSize: TextUnit = 18.sp,
    onButtonClick: () -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text =  buildAnnotatedString {
                val variable = complementaryText.orEmpty()

                val fullText = stringResource(title, variable)

                append(fullText)

                if (variable.isNotEmpty()) {
                    val start = fullText.indexOf(variable)

                    if (start >= 0) {
                        addStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            start = start,
                            end = start + variable.length
                        )
                    }
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
        if (actionLabel != null) {
            TextButton(onClick = onButtonClick) {
                Text(
                    text = stringResource(actionLabel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary.copy(blue = 1f),
                )
            }
        }
        if (icon != null) {
            IconButton(
                onClick = onIconClick,
                modifier = Modifier.semantics { /*TODO*/ }
            ) {
                Icon(icon, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LimpOnSectionHeaderPreview() {
    LimpOnSectionHeader(
        title = R.string.highlitghs,
        actionLabel = R.string.see_all
    )
}

@Preview(showBackground = true)
@Composable
fun MoreProductsPreview() {
    LimpOnSectionHeader(
        title = R.string.related_products,
        complementaryText = "Drogaria brasil",
    )
}