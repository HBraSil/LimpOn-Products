package com.example.produtosdelimpeza.store.profile.edit_profile

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnDescriptionTextField
import com.example.produtosdelimpeza.core.component.LoadingOverlay
import com.example.produtosdelimpeza.store.component.SuccessRegistrationOverlay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    editStoreProfileViewModel: EditStoreProfileViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit
) {
    val uiState by editStoreProfileViewModel.uiState.collectAsStateWithLifecycle()

    Log.d("EditProfileScreen", "uiState: ${uiState.editableStore?.name ?: "no name"}")

    EditProfileContent(
        uiState = uiState,
        updateName = editStoreProfileViewModel::updateStoreName,
        saveName = editStoreProfileViewModel::saveName,
        resetViewModel = editStoreProfileViewModel::reset,
        onBackNavigation = onBackNavigation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    uiState: EditStoreProfileUiState,
    updateName: (String) -> Unit = {} ,
    saveName: (String) -> Unit = {},
    resetViewModel: () -> Unit = {},
    onBackNavigation: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
        ) {
            HeaderAvatarSection(
                originalName = uiState.originalStore?.name ?: "",
                editedName = uiState.editableStore?.name ?: "no name",
                onSaveName = { novoNome ->
                    saveName(novoNome)
                },
                onImageEditClick = { /* Abrir Galeria */ },
                onEditName = updateName
            )

            StoreDescriptionOutlinedCard(
                description = uiState.originalStore?.description,
                onDescriptionChange = { /*TODO: change the store description*/}
            )

            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.editableStore?.category ?: "",
                    onValueChange = { },
                    label = { Text(stringResource(R.string.category)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, stringResource(R.string.categories)) }
                )
            }

            EditableContactAndAddressCard(
                phone = uiState.originalStore?.phone,
                email = uiState.originalStore?.email,
                address = uiState.originalStore?.address,
                onPhoneChange = {},
                onEmailChange = {},
                onAddressChange = {}
            )
        }
    }


    if (uiState.success) {
        SuccessRegistrationOverlay(
            message = stringResource(R.string.profile_updated),
            visible = true
        ) {
            resetViewModel()
        }
    }

    if (uiState.isLoading) {
        LoadingOverlay()
    }
}

@Composable
fun HeaderAvatarSection(
    originalName: String? = "",
    editedName: String? = "",
    onSaveName: (String) -> Unit = {},
    onImageEditClick: () -> Unit,
    onEditName: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()


    val isChanged = originalName != editedName

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                onClick = onImageEditClick // Permite clicar na foto toda para editar
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Badge (Botão de Câmera sobreposto)
            SmallFloatingActionButton(
                onClick = onImageEditClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = 4.dp, y = 4.dp) // Leve ajuste para fora do círculo
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "Mudar foto",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            BasicTextField(
                value = editedName ?: "",
                onValueChange = onEditName,
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = if (!isFocused) 24.sp else 20.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            )

            // Texto de apoio (Hint)
            if (!isFocused) {
                Text(
                    text = "Toque no nome para editar",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            AnimatedVisibility(
                visible = isChanged,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Button(
                    onClick = { onSaveName(editedName ?: "") },
                    modifier = Modifier.padding(top = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary, // Verde de sucesso
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Salvar Nome", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun StoreDescriptionOutlinedCard(
    description: String? = "",
    onDescriptionChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Descrição da Loja",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Esse texto aparecerá para todos os usuários",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LimpOnDescriptionTextField(description = description ?: ""){
                onDescriptionChange(it)
            }
        }
    }
}


@Composable
fun EditableContactAndAddressCard(
    phone: String? = "",
    email: String? = "",
    address: String? = "",
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
    ){
        Text(
            "Contato e Endereço",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Toque nos campos para editar",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontStyle = FontStyle.Italic
                ),
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(20.dp))
        InlineEditableText(
            icon = Icons.Default.Phone,
            label = "Telefone",
            value = phone?.ifBlank { "Nenhum telefone cadastrado" } ?: "",
            onValueChange = onPhoneChange,
            keyboardType = KeyboardType.Phone
        )
        Spacer(Modifier.height(20.dp))
        InlineEditableText(
            icon = Icons.Default.Email,
            label = "E-mail",
            value = email ?: "",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(20.dp))
        InlineEditableText(
            icon = Icons.Default.LocationOn,
            label = "Endereço",
            value = address ?: "",
            onValueChange = onAddressChange,
            singleLine = false
        )
    }
}


@Composable
fun InlineEditableText(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    var text by remember { mutableStateOf(value) }
    var isFocused by remember { mutableStateOf(false) }
    val hasChanged = text != value

    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(12.dp))

            BasicTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = singleLine,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(
                    if (isFocused) MaterialTheme.colorScheme.primary
                    else Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isFocused)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused }
            )

            if (hasChanged) {
                IconButton(
                    onClick = { onValueChange(text) },
                    modifier = Modifier.size(26.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Salvar",
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEditProfile() {
    EditProfileContent(
        uiState = EditStoreProfileUiState()
    )
}