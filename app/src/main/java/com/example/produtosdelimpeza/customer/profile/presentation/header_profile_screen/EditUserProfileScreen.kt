package com.example.produtosdelimpeza.customer.profile.presentation.header_profile_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.core.domain.model.User
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    editUserProfileScreenViewModel: EditUserProfileScreenViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var showEditPasswordModal by remember { mutableStateOf(false) }
    var showEditEmailModal by remember { mutableStateOf(false) }

    val currUser by editUserProfileScreenViewModel.originalUser.collectAsState()
    val currForm by editUserProfileScreenViewModel.formState.collectAsState()
    val hasChanges by editUserProfileScreenViewModel.hasChanges.collectAsState()



    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ProfileHeader(user = currUser)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column {
                    Text(
                        text = "Informações Pessoais",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Nome (Edição Direta)
                    EditableProfileField(
                        icon = Icons.Default.Person,
                        label = "Nome Completo",
                        value = currForm.name,
                        onValueChange = { editUserProfileScreenViewModel.onNameChanged(it) }
                    )

                    // Telefone (Edição Direta)
                    EditableProfileField(
                        icon = Icons.Default.Phone,
                        label = "Telefone",
                        value = currForm.phone,
                        onValueChange = { editUserProfileScreenViewModel.onPhoneChanged(it) },
                        keyboardType = KeyboardType.Phone
                    )

                    // Divisor antes das ações complexas
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // E-mail (Ação Complexa)
                    ActionItem(
                        icon = Icons.Default.Email,
                        title = "E-mail",
                        subtitle = currForm.email,
                        onClick = { editUserProfileScreenViewModel.onEmailChanged(currForm.email) }
                    )

                    // Senha (Ação Complexa)
                    ActionItem(
                        icon = Icons.Default.Lock,
                        title = "Senha",
                        subtitle = "********",
                        onClick = { showEditPasswordModal = true }
                    )

                    // Botão de Salvar Condicional
                    if (hasChanges) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ElevatedButton(
                            onClick = {
                                editUserProfileScreenViewModel.saveUser(currForm)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(bottom = 24.dp)
                        ) {
                            Text("Salvar Alterações")
                        }
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Modal para Edição de Senha
    if (showEditPasswordModal) {
        ModalBottomSheet(
            onDismissRequest = { showEditPasswordModal = false },
            sheetState = sheetState
        ) {
            EditPasswordModal(
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showEditPasswordModal = false
                        }
                    }
                }
            )
        }
    }

    // Modal para Edição de E-mail
    if (showEditEmailModal) {
        ModalBottomSheet(
            onDismissRequest = { showEditEmailModal = false },
            sheetState = sheetState
        ) {
            EditEmailModal(
                userEmail = currForm.email,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showEditEmailModal = false
                        }
                    }
                }
            )
        }
    }
}


// --- Componentes Reutilizáveis ---

@Composable
fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder para a foto de perfil
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .clickable { /* Ação para mudar foto */ }
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de Perfil",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Avançar",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 24.dp),
        thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun EditableProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

// --- Modal Bottom Sheet para Edição de Senha ---

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordModal(onDismiss: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Alterar Senha",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PasswordField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = "Senha Atual"
        )

        PasswordField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = "Nova Senha"
        )

        PasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar Nova Senha"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                onClick = {
                    // Lógica de validação e salvamento (mock)
                    if (newPassword == confirmPassword && newPassword.isNotBlank()) {
                        // Sucesso (apenas mock)
                        onDismiss()
                    } else {
                        // Mostrar erro (apenas mock)
                    }
                },
                enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank()
            ) {
                Text("Confirmar")
            }
        }
    }
}

// --- Modal Bottom Sheet para Edição de E-mail ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmailModal(userEmail: String, onDismiss: () -> Unit) {
    // 0: Inserir novo e-mail, 1: Verificar código
    var step by remember { mutableIntStateOf(0) }
    var newEmail by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Alterar E-mail",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (step == 0) {
            // Passo 1: Inserir Novo E-mail
            Text(
                text = "Seu e-mail atual é: $userEmail",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Novo E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = {
                        // Mock: Simula o envio do código e avança para o próximo passo
                        if (newEmail.isNotBlank() && newEmail != userEmail) {
                            step = 1
                        }
                    },
                    enabled = newEmail.isNotBlank() && newEmail != userEmail
                ) {
                    Text("Enviar Código")
                }
            }
        } else {
            // Passo 2: Verificar Código
            Text(
                text = "Um código de verificação foi enviado para o seu e-mail atual ($userEmail).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Código de Verificação") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { /* Mock: Reenviar código */ }) {
                Text("Reenviar Código")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = {
                        // Mock: Simula a verificação e confirmação
                        if (verificationCode.isNotBlank()) {
                            // Sucesso (apenas mock)
                            onDismiss()
                        }
                    },
                    enabled = verificationCode.isNotBlank()
                ) {
                    Text("Confirmar Alteração")
                }
            }
        }
    }
}
