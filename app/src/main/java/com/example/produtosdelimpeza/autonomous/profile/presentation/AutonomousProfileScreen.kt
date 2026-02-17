package com.example.produtosdelimpeza.autonomous.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Brush
import com.example.produtosdelimpeza.core.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutonomousProfileScreen(onNavigateToOtherUser: (String) -> Unit) {
    var email by remember { mutableStateOf("bruno.silva@email.com") }
    var whatsapp by remember { mutableStateOf("(11) 98888-7777") }
    var isEditing by remember { mutableStateOf(false) }
    var hasChanges by remember { mutableStateOf(false) }
    val professions = remember { mutableStateListOf("Eletricista Residencial") }
    var showInput by remember { mutableStateOf(false) }
    var newProfession by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meu Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    TextButton(onClick = {
                        if (isEditing) isEditing = false else isEditing = true
                    }) {
                        Text(
                            text = if (isEditing) "Cancelar" else "Editar",
                            color = if (isEditing) Color.Red else Color(0xFF0A3D62),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ProfileHeaderSection()

            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    professions.forEach { job ->
                        SuggestionChip(
                            onClick = { if(professions.size > 1) professions.remove(job) },
                            label = { Text(job, fontSize = 14.sp) },
                            shape = RoundedCornerShape(16.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                labelColor = Color(0xFF0A3D62)
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    IconButton(
                        onClick = { showInput = !showInput },
                        modifier = Modifier.background(Color(0xFF0A3D62), CircleShape)
                    ) {
                        Icon(
                            if(showInput) Icons.Default.Close else Icons.Default.Add,
                            null,
                            tint = Color.White,
                        )
                    }
                }

                AnimatedVisibility(visible = showInput) {
                    OutlinedTextField(
                        value = newProfession,
                        onValueChange = { newProfession = it },
                        modifier = Modifier.padding(top = 12.dp).fillMaxWidth(0.8f),
                        placeholder = { Text("Ex: Encanador", fontSize = 14.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            if (newProfession.isNotBlank()) {
                                IconButton(onClick = {
                                    professions.add(newProfession)
                                    newProfession = ""
                                    showInput = false
                                }) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFF00B894))
                                }
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            SwitchProfileCard(onSwitchProfileClick = {screen ->
                onNavigateToOtherUser(screen)
            })
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White)
                    .animateContentSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditableProfileItem(
                    icon = Icons.Default.Email,
                    label = "E-mail",
                    value = email,
                    isEditing = isEditing,
                    onValueChange = {
                        email = it
                        hasChanges = true
                    }
                )

                HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 1.dp)

                EditableProfileItem(
                    icon = Icons.Default.Phone,
                    label = "WhatsApp",
                    value = whatsapp,
                    isEditing = isEditing,
                    onValueChange = {
                        whatsapp = it
                        hasChanges = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTÃO SALVAR DINÂMICO ---
            AnimatedVisibility(
                visible = isEditing && hasChanges,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Button(
                    onClick = {
                        isEditing = false
                        hasChanges = false
                        // Aqui entraria sua lógica de Firebase/API
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B894))
                ) {
                    Icon(Icons.Default.Done, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Salvar Alterações", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botão de Sair mais discreto
            if (!isEditing) {
                TextButton(onClick = { }) {
                    Text("Sair da conta", color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchProfileCard(onSwitchProfileClick: (String) -> Unit, onSignOutClick: () -> Unit = {}) {
    var isSheetOpen by remember { mutableStateOf(false) }
    val profiles = mapOf(CustomerScreen.CUSTOMER_HOME.route to "Hilquias", StoreScreen.DASHBOARD.route to "Doceria")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { isSheetOpen = true },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSurface)
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo Cliente", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Compre em suas lojas favoritas", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
            Icon(
                Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            dragHandle = {  },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Text(
                text = "Escolher perfil",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 24.dp)
            )

            profiles.forEach { profile ->
                ListItem(
                    headlineContent = {
                        Text(profile.value)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            if (profile.key != StoreScreen.DASHBOARD.route) {
                                onSwitchProfileClick(profile.key)
                            }
                            isSheetOpen = false
                        }
                        .padding(horizontal = 8.dp)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = "Adicionar uma nova conta",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier
                    .clickable {
                        onSignOutClick()
                        isSheetOpen = false
                    }
                    .padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun EditableProfileItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF0A3D62), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)

            if (isEditing) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF00B894),
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
            } else {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0A3D62),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileHeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Icon(
                    Icons.Default.Person,
                    null,
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    tint = Color(0xFF0A3D62)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Bruno Silva", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
        Text("Membro desde 2026", color = Color.Gray, fontSize = 12.sp)
    }
}