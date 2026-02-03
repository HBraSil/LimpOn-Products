import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SellerEntryPointScreen(
    onHaveInvite: () -> Unit,
    onRequestInvite: () -> Unit,
    onBackNavigation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBFF))
            .padding(24.dp)
    ) {
        IconButton(onClick = onBackNavigation, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Voltar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Seja um parceiro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1C1B1F)
        )

        Text(
            text = "Escolha como deseja iniciar sua jornada no app.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF49454F)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // OPÇÃO 1: JÁ TENHO CONVITE (Design mais sóbrio)
        OptionCard(
            title = "Tenho um convite",
            description = "Insira sua chave de acesso para configurar sua loja agora.",
            icon = Icons.Default.Key,
            containerColor = Color.White,
            iconColor = Color(0xFF6750A4),
            onClick = onHaveInvite
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OPÇÃO 2: QUERO SOLICITAR (Destaque visual/Primário)
        OptionCard(
            title = "Quero solicitar um convite",
            description = "Ainda não faz parte? Envie seus dados para análise.",
            icon = Icons.Default.Storefront,
            containerColor = Color(0xFF6750A4),
            contentColor = Color.White,
            iconColor = Color.White,
            onClick = onRequestInvite
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Dúvidas sobre o processo? Central de Ajuda",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF6750A4),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color = Color(0xFF1C1B1F),
    iconColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = containerColor,
        shape = RoundedCornerShape(24.dp),
        border = if (containerColor == Color.White) BorderStroke(1.dp, Color(0xFFCAC4D0)) else null,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (contentColor == Color.White) Color.White.copy(alpha = 0.2f) else iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (contentColor == Color.White) Color.White else iconColor,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (contentColor == Color.White) Color.White.copy(alpha = 0.8f) else Color(0xFF49454F)
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (contentColor == Color.White) Color.White else Color(0xFFCAC4D0)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", name = "Tela de Convite - Light Mode")
@Composable
fun SellerEntryPointPreview() {
    // Aplicando um MaterialTheme básico para o preview renderizar corretamente as cores
    MaterialTheme {
        SellerEntryPointScreen(
            onHaveInvite = {},
            onRequestInvite = {},
            onBackNavigation = {}
        )
    }
}