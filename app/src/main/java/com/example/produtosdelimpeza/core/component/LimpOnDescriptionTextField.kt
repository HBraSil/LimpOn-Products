package com.example.produtosdelimpeza.core.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LimpOnDescriptionTextField(
    maxChars: Int = 200,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = {
            if (it.length <= maxChars) {
                onDescriptionChange(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        placeholder = {
            Text("Ex: Hamburgueria artesanal com foco em qualidade e rapidez")
        },
        supportingText = {
            Text("${description.length}/$maxChars")
        },
        singleLine = false,
        maxLines = 6,
        shape = RoundedCornerShape(16.dp)
    )
}