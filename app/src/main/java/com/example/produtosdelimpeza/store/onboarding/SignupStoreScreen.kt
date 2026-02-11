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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnDescriptionTextField
import com.example.produtosdelimpeza.core.component.LimpOnTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupStoreScreen(
    onBackNavigation: () -> Unit,
    signUpStoreViewModel: SignUpStoreViewModel = hiltViewModel()
) {

    val formState = signUpStoreViewModel.formState
    val isConfirmationButtonValid by signUpStoreViewModel.isButtonValid.collectAsState()


    val categories = listOf(
        "Restaurante",
        "Mercado",
        "Padaria",
        "Loja de roupas",
        "Loja de eletrônicos",
        "Farmácia",
        "Outro"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }

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
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // LOGO / IDENTIDADE
            item {
                StoreLogoPicker()
            }

            // IDENTIDADE
            item {
                Section(title = "Identidade da loja") {
                    LimpOnTextField(
                        value = formState.storeName.field,
                        onValueChange = {
                            signUpStoreViewModel.updateName(it)
                        },
                        errorMessage = formState.storeName.error,
                        label = R.string.business_name,
                        placeholder = R.string.business_name_example,
                        leadingIcon = { Icon(Icons.Default.Store, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de estabelecimento") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        selectedCategory = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // DESCRIÇÃO
            item {
                Section(title = "Sobre a loja") {
                    LimpOnDescriptionTextField(formState.description.field) {
                        signUpStoreViewModel.updateDescription(it)
                    }
                }
            }

            // CONTATO
            item {
                Section(title = "Contato") {
                    LimpOnTextField(
                        value = formState.email.field,
                        onValueChange = {
                            signUpStoreViewModel.updateEmail(it)
                        },
                        label = R.string.email,
                        placeholder = R.string.hint_email,
                        errorMessage = formState.email.error,
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().offset(y = (-20).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Usar o mesmo email desta conta",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Checkbox(
                            checked = true,
                            onCheckedChange = {},
                            modifier = Modifier.align(Alignment.Top)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    LimpOnTextField(
                        value = formState.phone.field,
                        onValueChange = {
                            signUpStoreViewModel.updatePhone(it)
                        },
                        errorMessage = formState.phone.error,
                        label = R.string.whatsapp,
                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                        placeholder = R.string.cellphone_example,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            // CTA
            item {
                Button(
                    onClick = { signUpStoreViewModel.createStore() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    enabled = isConfirmationButtonValid,
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun RequestInvitePreview() {
    MaterialTheme {
        SignupStoreScreen(onBackNavigation = {})
    }
}