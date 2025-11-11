package com.example.produtosdelimpeza.compose.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.produtosdelimpeza.R
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.compose.main.MainBottomNavigation

// Definição de cores personalizadas para o tema (minimalista)
val LightGrayBackground = Color(0xFFF5F5F5)
val DarkText = Color(0xFF1E1E1E)
val PrimaryColor = Color(0xFF007AFF) // Azul vibrante para destaque

@Composable
fun ProfileScreen(
    navController: NavHostController?,
    onClickNotificationsScreen: () -> Unit = {},
    onClickEditUserProfile: () -> Unit = {},
    onClickPaymentMethods: () -> Unit = {}
) {
    // Usando Scaffold para a estrutura básica da tela, incluindo a barra de navegação inferior
    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController!!)
        },
        containerColor = LightGrayBackground // Fundo levemente cinza para a tela
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Torna o conteúdo scrollável
        ) {
            // Adiciona um espaçamento superior para telas sem TopAppBar
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Cabeçalho
            HeaderSection(
                userName = "Hilquias Brasil",
                userInitials = "HB",
                onClickEditUserProfile = onClickEditUserProfile
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Ações Rápidas (Cards)
            QuickActionsSection(
                onClickNotificationsScreen = onClickNotificationsScreen,
                onClickPaymentMethods = onClickPaymentMethods
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Seções Informativas (Listas)
            // Seção 1: Benefícios/Créditos
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
                    onClick = { /* Ação */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Seção 2: Minha Conta
            InfoSection(title = "Minha Conta") {
                InfoItem(
                    icon = Icons.Default.LocationOn,
                    text = "Meus Endereços",
                    onClick = { /* Ação */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.CreditCard,
                    text = "Formas de Pagamento",
                    onClick = { /* Ação */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                InfoItem(
                    icon = Icons.Default.History,
                    text = "Histórico de Pedidos",
                    onClick = { /* Ação */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Seção 3: Suporte e Informações
            InfoSection(title = "Suporte e Informações") {
                InfoItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    text = "Ajuda e FAQ",
                    onClick = { /* Ação */ }
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
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


// --- 1. Cabeçalho ---
@Composable
fun HeaderSection(userName: String, userInitials: String, onClickEditUserProfile: () -> Unit = {}) {

    // Ações no topo (simulando TopAppBar)
    Box(
        modifier = Modifier.fillMaxWidth(),
    ){
        Column(
            modifier = Modifier
                .height(70.dp)
                .align(Alignment.TopCenter)
                .background(
                    color = Color.Gray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
        ) {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Encerrar Sessão",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = DarkText,
                    modifier = Modifier.padding(start = 10.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                ElevatedButton(
                    onClick = {},
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    Text("Sair")
                }
            }
        }


        // Bloco de Destaque do Usuário (Avatar + Nome)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp)
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
}


// --- 2. Ações Rápidas (Cards) ---
@Composable
fun QuickActionsSection(
    onClickNotificationsScreen: () -> Unit,
    onClickPaymentMethods: () -> Unit = {}
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
            modifier = Modifier.weight(1f)
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


/*
Scaffold(

) { contentPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Card(
            onClick = {},
            modifier = Modifier
                .padding(top = 20.dp)
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            elevation = CardDefaults.elevatedCardElevation(3.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.image_profile),
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        PersonalData()
        Configurations(navController)
        GeneralInformation(navController)
    }
}*/

