package com.example.produtosdelimpeza.customer.profile.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.produtosdelimpeza.R
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.core.navigation.route.CustomerScreen
import com.example.produtosdelimpeza.core.navigation.route.StoreScreen

// Definição de cores personalizadas para o tema (minimalista)
val LightGrayBackground = Color(0xFFF5F5F5)
val DarkText = Color(0xFF1E1E1E)
val PrimaryColor = Color(0xFF007AFF) // Azul vibrante para destaque


@Composable
fun ProfileScreen(
    onNotificationsScreenClick: () -> Unit = {},
    onEditUserProfileScreenClick: () -> Unit = {},
    onPaymentMethodsScreenClick: () -> Unit = {},
    onCouponsScreenClick: () -> Unit = {},
    onSellInTheApp: () -> Unit = {},
    onMyAddressesScreenClick: () -> Unit = {},
    onAboutScreenClick: () -> Unit = {},
    onHelpScreenClick: () -> Unit = {},
    onOrderScreenClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onSwitchProfileClick: (String) -> Unit = {},
    profileViewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    // Usando Scaffold para a estrutura básica da tela, incluindo a barra de navegação inferior
    Scaffold(
        bottomBar = {
            //MainBottomNavigation(navController!!)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Torna o conteúdo scrollável
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderSection(
                userName = "Hilquias Brasil",
                userInitials = "HB",
                onClickEditUserProfile = onEditUserProfileScreenClick
            )
            Spacer(modifier = Modifier.height(24.dp))
            QuickActionsSection(
                onClickNotificationsScreen = onNotificationsScreenClick,
                onClickHelpScreen = onHelpScreenClick,
                onClickPaymentMethods = onPaymentMethodsScreenClick,
            )
            Spacer(modifier = Modifier.height(32.dp))
            InfoSection(title = "Benefícios") {
                InfoItem(
                    icon = Icons.Default.Star,
                    text = "Créditos e Fidelidade",
                    endText = "R$ 0,00",
                    onClick = { /* Ação */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.Discount,
                    text = "Cupons",
                    onClick = onCouponsScreenClick
                )
                InfoItem(
                    icon = Icons.Default.Discount,
                    text = "Quero vender no app",
                    onClick = onSellInTheApp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            InfoSection(title = "Minha Conta") {
                InfoItem(
                    icon = Icons.Default.LocationOn,
                    text = "Meus Endereços",
                    onClick = onMyAddressesScreenClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.CreditCard,
                    text = "Formas de Pagamento",
                    onClick = onPaymentMethodsScreenClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.History,
                    text = "Histórico de Pedidos",
                    onClick = onOrderScreenClick
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Seção 3: Suporte e Informações
            InfoSection(title = "Suporte e Informações") {
                InfoItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    text = "Ajuda e FAQ",
                    onClick = onHelpScreenClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.Policy,
                    text = "Termos de Uso e Privacidade",
                    onClick = { /* Ação */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.Policy,
                    text = stringResource(R.string.about),
                    onClick = onAboutScreenClick
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            AccountFooterSection(profileViewModel, onSignOutClick) { profileMode ->
                onSwitchProfileClick(profileMode)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFooterSection(
    profileViewModel: ProfileScreenViewModel,
    onSignOutClick: () -> Unit = {},
    onSwitchProfileClick: (String) -> Unit = {}
) {

    val profiles = mapOf(CustomerScreen.CUSTOMER_HOME.route to "Hilquias", StoreScreen.DASHBOARD.route to "Doceria")
    var isSheetOpen by remember { mutableStateOf(false) }
    var signOutClickAlertDialog by remember { mutableStateOf(false) }


    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            dragHandle = {  },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {

            // Header
            Text(
                text = "Escolher perfil",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 24.dp)
            )

            // Lista de perfis
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
                            if (profile.key != CustomerScreen.CUSTOMER_HOME.route) onSwitchProfileClick(profile.key) // DEPOIS TEREI Q MUDAR ESSA LÓGICA
                            isSheetOpen = false
                        }
                        .padding(horizontal = 8.dp)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant
            )

            // Botão de Logout em destaque
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

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {

            // Alternar modo
            Text(
                text = "Mudar de perfil",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isSheetOpen = true }
            )
            Spacer(Modifier.height(16.dp))

            // Logout
            Text(
                text = "Sair",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        signOutClickAlertDialog = true
                    }
            )

            if (signOutClickAlertDialog) {
                AlertDialog(
                    onDismissRequest = { signOutClickAlertDialog = false },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = "Deseja sair da conta?",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    text = {
                        Text(
                            text = "Você terá que fazer login novamente para acessar sua conta.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onSignOutClick()
                                profileViewModel.signOut()
                                signOutClickAlertDialog = false
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Sair")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { signOutClickAlertDialog = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}




// --- 1. Cabeçalho ---
@Composable
fun HeaderSection(userName: String, userInitials: String, onClickEditUserProfile: () -> Unit = {}) {
        // Bloco de Destaque do Usuário (Avatar + Nome)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickEditUserProfile),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Destaque com sombra
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (Círculo com Iniciais)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor.copy(alpha = 0.1f)), // Cor suave de fundo
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInitials,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = DarkText,
                    fontFamily = FontFamily(
                        Font(R.font.montserrat_medium_italic)
                    ),
                )
                Text(
                    text = "Você ainda não é um assinante",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue.copy(alpha = 0.7f)
                )
            }

            // Ícone de navegação para indicar que o card é clicável
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Editar Perfil",
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}


// --- 2. Ações Rápidas (Cards) ---
@Composable
fun QuickActionsSection(
    onClickNotificationsScreen: () -> Unit,
    onClickHelpScreen: () -> Unit,
    onClickPaymentMethods: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionCard(
            icon = Icons.Default.Notifications,
            text = "Notificações",
            modifier = Modifier.weight(1f),
            onClickCardSection = onClickNotificationsScreen
        )
        Spacer(modifier = Modifier.width(12.dp))
        QuickActionCard(
            icon = Icons.Default.HeadsetMic,
            text = "Ajuda",
            modifier = Modifier.weight(1f),
            onClickCardSection = onClickHelpScreen
        )
        Spacer(modifier = Modifier.width(12.dp))
        QuickActionCard(
            icon = Icons.Default.Payment,
            text = "Pagamento",
            modifier = Modifier.weight(1f),
            onClickCardSection = onClickPaymentMethods
        )
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector, text: String,
    onClickCardSection: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClickCardSection),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra leve
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = PrimaryColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = DarkText
            )
        }
    }
}

// --- 3. Seções Informativas (Listas) ---
@Composable
fun InfoSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = DarkText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Container para agrupar os itens e aplicar um fundo/elevação se necessário
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp // Sombra sutil para destacar a seção
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    text: String,
    endText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DarkText.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = DarkText,
            modifier = Modifier.weight(1f)
        )

        if (endText != null) {
            Text(
                text = endText,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Avançar",
            tint = Color.LightGray,
            modifier = Modifier.size(24.dp)
        )
    }
}