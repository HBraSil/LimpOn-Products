package com.example.produtosdelimpeza.store.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.core.component.LimpOnDescriptionTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupStoreScreen(
    onBackNavigation: () -> Unit,
    onSuccess: () -> Unit,
    signUpStoreViewModel: SignUpStoreViewModel = hiltViewModel()
) {
    var storeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val formState = signUpStoreViewModel.formState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Criar sua loja", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // LOGO / IDENTIDADE
            item {
                StoreLogoPicker()
            }

            // IDENTIDADE
            item {
                Section(title = "Identidade da loja") {
                    ModernTextField(
                        value = formState.storeName.field,
                        onValueChange = {
                            signUpStoreViewModel.updateName(it)
                        },
                        label = "Nome da loja",
                        icon = Icons.Default.Storefront
                    )
                    Spacer(Modifier.height(8.dp))
                    ModernTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = "Categoria",
                        icon = Icons.Default.Category
                    )
                }
            }

            // DESCRIÇÃO
            item {
                Section(title = "Sobre a loja") {
                    LimpOnDescriptionTextField(description = description) {
                         description = it
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${description.length}/150",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // CONTATO
            item {
                Section(title = "Contato") {
                    ModernTextField(
                        value = formState.email.field,
                        onValueChange = {
                            signUpStoreViewModel.updateEmail(it)
                        },
                        label = "E-mail",
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = true,
                            onCheckedChange = { /* Handle checkbox state change */ }
                        )
                        Text(
                            text = "Usar o mesmo email desta conta",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    ModernTextField(
                        value = formState.phone.field,
                        onValueChange = {
                            signUpStoreViewModel.updatePhone(it)
                        },
                        errorMessage = formState.phone.error,
                        label = "WhatsApp",
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                }
            }

            // CTA
            item {
                Button(
                    onClick = onSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        "Finalizar cadastro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun StoreLogoPicker() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Adicionar logo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "Logotipo da loja",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun Section(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = minLines,
        isError = errorMessage != null,
        supportingText = {
            errorMessage?.let { msg ->
                Text(msg)
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun RequestInvitePreview() {
    MaterialTheme {
        SignupStoreScreen(onBackNavigation = {}, onSuccess = {})
    }
}