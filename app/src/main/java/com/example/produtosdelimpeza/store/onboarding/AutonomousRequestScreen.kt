package com.example.produtosdelimpeza.store.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Handshake
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutonomousRequestScreen(
    onSubmit: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    val professions = listOf(
        "Cozinheiro(a)",
        "Confeiteiro(a)",
        "Eletricista",
        "Encanador(a)",
        "Diarista",
        "Personal Trainer",
        "Manicure/Pedicure",
        "Costureiro(a)",
        "Outro"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
                Column(
                    modifier = Modifier.padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Handshake,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )

                    Text(
                        text = "Solicitação para vendedor autônomo",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Preencha suas informações para que possamos analisar seu pedido de venda na plataforma.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Seus dados",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Surface(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {

                            LimpOnTextField(
                                value = "",
                                onValueChange = {},
                                label = R.string.full_name,
                                placeholder = R.string.name_example,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = stringResource(R.string.person_icon),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                            )

                            LimpOnTextField(
                                value = name,
                                onValueChange = { name = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = R.string.contact_email,
                                placeholder = R.string.hint_email,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = stringResource(R.string.email_icon),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )

                            LimpOnTextField(
                                value = "",
                                onValueChange = {},
                                label = R.string.business_whatsapp,
                                placeholder = R.string.cellphone_example,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.PhoneAndroid,
                                        contentDescription = stringResource(R.string.cellphone_icon),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            )

                            LimpOnTextField(
                                value = "",
                                onValueChange = {},
                                label = R.string.city_address,
                                placeholder = R.string.city_address_example,
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = stringResource(R.string.location_icon),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text(
                        "Sua Especialidade",
                        style = MaterialTheme.typography.titleMedium
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.padding(horizontal = 26.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedProfession,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Qual é sua principal atividade?") },
                            placeholder = { Text("Selecione sua profissão") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                            professions.forEach { profession ->
                                DropdownMenuItem(
                                    text = { Text(profession) },
                                    onClick = {
                                        selectedProfession = profession
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = selectedProfession == "Outro",
                        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
                    ) {

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Descreva sua especialidade abaixo:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = serviceDescription,
                                    onValueChange = { serviceDescription = it },
                                    label = { Text("Descreva seu serviço") },
                                    placeholder = { Text("Ex: Marmitas caseiras, manutenção elétrica, doces artesanais…") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 120.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        focusedContainerColor = MaterialTheme.colorScheme.background,
                                        unfocusedContainerColor = Color.Transparent,
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                ElevatedButton(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth().padding(24.dp).height(58.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = "Enviar Cadastro Comercial",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Rounded.Business,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

             Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Preview
@Composable
fun Teste3() {
    AutonomousRequestScreen {}
}