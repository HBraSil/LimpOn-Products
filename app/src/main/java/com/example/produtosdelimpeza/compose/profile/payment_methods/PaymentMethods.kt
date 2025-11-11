package com.example.produtosdelimpeza.compose.profile.payment_methods

import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// -----------------------------------------------------------------------------
// 1. Estrutura Principal da Tela
// -----------------------------------------------------------------------------

@Composable
fun PaymentMethodsScreen() {
    Scaffold(
        topBar = { PaymentScreenTopAppBar() },
        floatingActionButton = { MainActionButton() },
        floatingActionButtonPosition = FabPosition.Center,
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Seu Cartão Principal",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    CreditCardItem(
                        cardNumber = "**** **** **** 1234",
                        cardHolder = "HILQUIAS BRASIL",
                        expiryDate = "12/27",
                        isPrimary = true
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                item {
                    Text(
                        text = "Outras Opções",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    PaymentOptionItem(
                        icon = Icons.Default.AddCircle,
                        title = "Adicionar novo cartão",
                        description = "Crédito, débito ou pré-pago"
                    )
                }
                item {
                    PaymentOptionItem(
                        icon = Icons.Default.QrCode, // Ícone simulando o Pix
                        title = "Gerenciar Pix",
                        description = "Chaves, limites e histórico"
                    )
                }
                item {
                    PaymentOptionItem(
                        icon = Icons.Default.History,
                        title = "Histórico de Transações",
                        description = "Veja todas as suas movimentações"
                    )
                }
            }
        }
    )
}

// -----------------------------------------------------------------------------
// 2. Componente TopAppBar (Cabeçalho)
// -----------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreenTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Métodos de Pagamento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Ação de voltar */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Voltar"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// -----------------------------------------------------------------------------
// 3. Componente CreditCardItem (Card de Pagamento)
// -----------------------------------------------------------------------------

@Composable
fun CreditCardItem(
    cardNumber: String,
    cardHolder: String,
    expiryDate: String,
    isPrimary: Boolean
) {
    // Cores vibrantes para o gradiente do cartão
    val cardColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .animateContentSize(animationSpec = tween(300)) // Animação sutil ao redimensionar
            .clickable { /* Ação ao clicar no cartão */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(cardColors)
                )
                .padding(20.dp)
        ) {
            // Conteúdo do Cartão
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Linha 1: Logo e Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo do Banco/Bandeira (Simulado)
                    Text(
                        text = "VISA",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        ),
                        color = Color.White
                    )

                    // Chip de Status
                    if (isPrimary) {
                        AssistChip(
                            onClick = { /* Ação do chip */ },
                            label = {
                                Text(
                                    text = "Principal",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Principal",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                // Linha 2: Número do Cartão
                Text(
                    text = cardNumber,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Linha 3: Titular e Validade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Titular",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = cardHolder,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Validade",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = expiryDate,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 4. Componente PaymentOptionItem (Item da Lista)
// -----------------------------------------------------------------------------

@Composable
fun PaymentOptionItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* Ação ao clicar na opção */ },
        headlineContent = {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Avançar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    )
}

// -----------------------------------------------------------------------------
// 5. Componente MainActionButton (Botão Principal)
// -----------------------------------------------------------------------------

@Composable
fun MainActionButton() {
    ExtendedFloatingActionButton(
        onClick = { /* Ação principal */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(12.dp),
        text = {
            Text(
                text = "Configurações de Pagamento",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.background
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configurações"
            )
        }
    )
}