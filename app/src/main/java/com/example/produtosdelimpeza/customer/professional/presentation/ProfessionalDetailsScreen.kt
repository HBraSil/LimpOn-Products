import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.SectionHeader


@Composable
fun ProfessionalProfileScreen(
    onBackNaviagtion: () -> Unit = {},
) {
    val professionalName = "João Silva"
    val profession = "Eletricista Residencial"

    ProfessionalProfileContent(
        name = professionalName,
        profession = profession,
        onBackClick = onBackNaviagtion,
        onShareClick = { /* Abrir compartilhamento */ },
        onWhatsAppClick = { /* Abrir link do Zap */ },
    )
}


@Composable
fun ProfessionalProfileContent(
    name: String,
    profession: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
) {
    Scaffold(
        topBar = { ProfileTopBar(onBackClick, onShareClick) }
    ) { padding ->
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(name, profession)

                Spacer(modifier = Modifier.height(24.dp))

                SectionCard(title = "Preços", icon = Icons.Outlined.Payments) {
                    PriceRow("Diária", "R$ 250,00")
                    PriceRow("Hora", "R$ 60,00")
                }

                SectionCard(title = "Atendimento", icon = Icons.Outlined.Schedule) {
                    ScheduleRow("Segunda a Sexta", "08:00 - 18:00")
                    ScheduleRow("Sábado", "08:00 - 12:00")
                    ScheduleRow("Domingo", "Fechado", isClosed = true)
                }

                Spacer(modifier = Modifier.height(10.dp))

                SectionHeader(title = R.string.services, ) {}
                ServiceItem("Instalações elétricas completas")
                ServiceItem("Manutenção e reforma de quadros de luz")
                ServiceItem("Troca de fiação e aterramento")
                ServiceItem("Instalação de luminárias e ventiladores")

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = R.string.images_gallery, actionLabel = R.string.see_all) {}
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkImage(Modifier.weight(1f))
                    WorkImage(Modifier.weight(1f))
                    WorkImage(Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = R.string.reviews, actionLabel = R.string.see_all) {}
                ReviewItem(
                    reviewer = "Mariana Costa",
                    comment = "\"João foi extremamente pontual e resolveu o problema no meu quadro de luz rapidamente. Muito profissional e limpo.\""
                )
                ReviewItem(
                    reviewer = "Ricardo Alves",
                    comment = "\"Excelente trabalho na instalação das novas luminárias da sala. Recomendo com certeza!\""
                )
            }

            WhatsAppButton(
                onClick = onWhatsAppClick,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(onBack: () -> Unit, onShare: () -> Unit) {
    TopAppBar(
        title = { Text("Professional Profile", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = {
            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.icon_button_share)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}


@Composable
fun ProfileHeader(name: String, profession: String) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
                .background(Color.Gray)
        )

        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF0D47A1),
            modifier = Modifier
                .size(24.dp)
                .background(Color.White, CircleShape)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B34))
    Text(profession, fontSize = 14.sp, color = Color(0xFF0D47A1), fontWeight = FontWeight.Medium)

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
        Text(" 4.8 (124 avaliações)  |  ", fontSize = 12.sp, color = Color.Gray)
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Text("2.4 km de você", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun SectionCard(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun PriceRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
    }
}

@Composable
fun ScheduleRow(day: String, time: String, isClosed: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(day, color = MaterialTheme.colorScheme.onSurface)
        Text(time, color = if (isClosed) Color.Red else Color.Gray)
    }
}


@Composable
fun ServiceItem(text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray)
    }
}

@Composable
fun WorkImage(modifier: Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray) // Placeholder
    )
}

@Composable
fun ReviewItem(reviewer: String, comment: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(Color.Gray))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(reviewer, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row { repeat(5) { Icon(Icons.Default.Star, null, tint = Color(0xFFFF9800), modifier = Modifier.size(12.dp)) } }
            }
        }
        Text(comment, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun WhatsAppButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text("CHAMAR NO WHATSAPP", fontWeight = FontWeight.Bold)
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MaterialTheme {
        ProfessionalProfileContent(
            name = "João Silva",
            profession = "Eletricista Residencial",
            onBackClick = {},
            onShareClick = {},
            onWhatsAppClick = {},
        )
    }
}