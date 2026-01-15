package com.example.produtosdelimpeza.compose.seller.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.ui.draw.shadow
import com.example.produtosdelimpeza.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.navigation.route.StoreScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreProfileScreen(onNavigateToOtherUser: (String) -> Unit, onItemProfileClick: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Conta",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Troca de Perfil (Destaque Visual)
            SwitchProfileCard(
                onSwitchProfileClick = {screen ->
                    onNavigateToOtherUser(screen)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Lista de Opções
            MenuSection(
                onItemProfileClick = { route ->
                    onItemProfileClick(route)
                }
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Placeholder para Logo da Loja
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = null,
                    modifier = Modifier.padding(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Pizzaria do Bairro", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            HorizontalDivider(
                modifier = Modifier
                    .width(20.dp)
                    .padding(start = 4.dp, end = 4.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape),
                thickness = 2.dp
            )
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
            Text(" 4.8", fontWeight = FontWeight.SemiBold)
            Text(" • 120 avaliações", color = MaterialTheme.colorScheme.outline)
        }

        Text("Faturamento este mês: R$ 12.450,00", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
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
fun MenuSection(onItemProfileClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text("Gerenciamento", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        MenuItem(icon = Icons.Default.Edit, title = "Editar Perfil e Endereço") {onItemProfileClick(StoreScreen.STORE_EDIT_PROFILE.route)}
        MenuItem(icon = Icons.Default.Assessment, title = "Relatórios de Vendas") {onItemProfileClick(StoreScreen.ANALYTICS.route)}
        MenuItem(icon = Icons.Default.Schedule, title = "Logística") {onItemProfileClick(StoreScreen.LOGISTIC.route)}
        //MenuItem(icon = Icons.Default.SupportAgent, title = "Suporte e Ajuda") {onItemProfileClick("")}

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(
            onClick = { /* Sair */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Sair da conta")
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, title: String, onItemProfileClick: () -> Unit) {
    Surface(
        onClick = onItemProfileClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}