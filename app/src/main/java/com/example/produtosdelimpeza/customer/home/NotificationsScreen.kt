@file:OptIn(ExperimentalAnimationApi::class)

package com.example.notifications

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/* ---------------------------------------------------------------------------
   MODELOS
--------------------------------------------------------------------------- */

enum class NotificationType {
    PROMOTION,
    COUPON,
    ORDER_STATUS,
    NEW_ORDER,
    WARNING,
    SYSTEM
}

data class AppNotification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean
)

/* ---------------------------------------------------------------------------
   VIEWMODEL
--------------------------------------------------------------------------- */

open class NotificationsViewModel : ViewModel() {

    // Em produção isso virá do repositório / Firestore / API
    private val _notifications = mutableStateOf<List<AppNotification>>(emptyList())
    val notifications: State<List<AppNotification>> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        _notifications.value = sampleCustomerNotifications()
    }

    fun markAsRead(notificationId: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
    }
}

/* ---------------------------------------------------------------------------
   TELA (SEM PARÂMETROS – IDEAL PARA NAVIGATION)
--------------------------------------------------------------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = viewModel()
) {
    val notifications by viewModel.notifications

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notificações",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = notifications,
                key = { it.id }
            ) { notification ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300)) +
                            slideInVertically(
                                initialOffsetY = { it / 4 },
                                animationSpec = tween(300)
                            )
                ) {
                    NotificationItem(
                        notification = notification,
                        onClick = {
                            viewModel.markAsRead(notification.id)
                        }
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------------------------
   ITEM
--------------------------------------------------------------------------- */

@Composable
fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (notification.isRead)
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = if (notification.isRead) 1.dp else 3.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            NotificationIcon(notification.type)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                UnreadIndicator()
            }
        }
    }
}

/* ---------------------------------------------------------------------------
   ÍCONE
--------------------------------------------------------------------------- */

@Composable
fun NotificationIcon(type: NotificationType) {
    val (icon, tint) = when (type) {
        NotificationType.PROMOTION ->
            Icons.Outlined.LocalOffer to Color(0xFF6A1B9A)

        NotificationType.COUPON ->
            Icons.Outlined.ConfirmationNumber to Color(0xFF2E7D32)

        NotificationType.ORDER_STATUS ->
            Icons.Outlined.DeliveryDining to Color(0xFF0277BD)

        NotificationType.NEW_ORDER ->
            Icons.Outlined.ReceiptLong to Color(0xFFEF6C00)

        NotificationType.WARNING ->
            Icons.Outlined.WarningAmber to Color(0xFFD32F2F)

        NotificationType.SYSTEM ->
            Icons.Outlined.Info to Color(0xFF455A64)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = tint.copy(alpha = 0.15f)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.padding(10.dp)
        )
    }
}

/* ---------------------------------------------------------------------------
   INDICADOR
--------------------------------------------------------------------------- */

@Composable
fun UnreadIndicator() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
    )
}

/* ---------------------------------------------------------------------------
   PREVIEWS (ViewModel Fake)
--------------------------------------------------------------------------- */

@Preview(showBackground = true, name = "Cliente")
@Composable
fun NotificationsCustomerPreview() {
    MaterialTheme {
        NotificationsScreen(
            viewModel = object : NotificationsViewModel() {
                init {
                    val field = NotificationsViewModel::class
                        .java.getDeclaredField("_notifications")
                    field.isAccessible = true
                    field.set(this, mutableStateOf(sampleCustomerNotifications()))
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "Loja")
@Composable
fun NotificationsStorePreview() {
    MaterialTheme {
        NotificationsScreen(
            viewModel = object : NotificationsViewModel() {
                init {
                    val field = NotificationsViewModel::class
                        .java.getDeclaredField("_notifications")
                    field.isAccessible = true
                    field.set(this, mutableStateOf(sampleStoreNotifications()))
                }
            }
        )
    }
}

/* ---------------------------------------------------------------------------
   DADOS FAKE (SÓ PARA PREVIEW)
--------------------------------------------------------------------------- */

fun sampleCustomerNotifications() = listOf(
    AppNotification(
        id = "1",
        type = NotificationType.COUPON,
        title = "Cupom disponível 🎉",
        message = "Use o cupom APP20 e ganhe 20% OFF",
        time = "Agora",
        isRead = false
    ),
    AppNotification(
        id = "2",
        type = NotificationType.PROMOTION,
        title = "Promoção por tempo limitado",
        message = "A promoção acaba em 2 dias",
        time = "Há 1 hora",
        isRead = true
    ),
    AppNotification(
        id = "3",
        type = NotificationType.ORDER_STATUS,
        title = "Pedido saiu para entrega",
        message = "Seu pedido está a caminho",
        time = "Há 10 min",
        isRead = false
    )
)

fun sampleStoreNotifications() = listOf(
    AppNotification(
        id = "10",
        type = NotificationType.NEW_ORDER,
        title = "Novo pedido aguardando aceite",
        message = "Pedido #4532 precisa ser aceito",
        time = "Agora",
        isRead = false
    ),
    AppNotification(
        id = "11",
        type = NotificationType.WARNING,
        title = "Pedido próximo do atraso",
        message = "Pedido #4519 está atrasando",
        time = "Há 20 min",
        isRead = false
    ),
    AppNotification(
        id = "12",
        type = NotificationType.SYSTEM,
        title = "Você está offline",
        message = "Fique online para receber pedidos",
        time = "Hoje",
        isRead = true
    )
)
