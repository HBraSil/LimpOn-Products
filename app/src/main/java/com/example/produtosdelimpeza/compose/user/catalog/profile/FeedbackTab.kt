package com.example.produtosdelimpeza.compose.user.catalog.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R


@Composable
fun FeedbackTab() {

    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(4) }
    var validade by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .navigationBarsPadding()
    ) {

        item {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Avalie o vendedor",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp, Alignment.CenterHorizontally
                )
            ) {
                (1..5).forEach { index ->
                    IconButton(
                        onClick = {
                            rating = index
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = if (index <= rating) {
                                Icons.Outlined.Star // estrela preenchida
                            } else {
                                Icons.Outlined.StarBorder   // estrela vazia
                            },
                            contentDescription = "$index estrelas"
                        )
                    }
                }
            }

            OutlinedTextField(
                value = comment,
                onValueChange = {
                    if (it.length <= 150) {
                        comment = it
                    }
                },
                label = { Text("Escreva uma avaliação") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(8.dp),
                supportingText = {
                    Text(
                        text = "${150 - comment.length}",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        textAlign = TextAlign.End
                    )
                }
            )


            ElevatedButton(
                onClick = {
                    if (comment != "") {
                        validade = true
                        comment = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 14.dp, bottom = 40.dp)
            ) {
                Text(
                    text = "Comentar",
                )
            }


            RatingSummary(
                average = 4.5,
                totalReviews = 123,
                ratings = mapOf(
                    1 to 10,
                    2 to 20,
                    3 to 30,
                    4 to 40,
                    5 to 40
                )
            )

            FeedbackMessages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                rating = rating,
                comment = if (validade) comment else "Escreva uma avaliação"
            )


            // TODO: Um campo de resposta para a resposta do vendedor
        }
    }
}


@Composable
fun RatingSummary(
    average: Double,
    totalReviews: Int,
    ratings: Map<Int, Int>, // chave = estrelas (1 a 5), valor = quantidade
    modifier: Modifier = Modifier
) {
    val maxCount = ratings.values.maxOrNull() ?: 1

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título
            Text(
                text = "Estrelas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Média à esquerda
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = String.format("%.1f", average),
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(start = 4.dp)
                        )
                    }
                    Text(
                        text = "$totalReviews avaliações",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                VerticalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .height(120.dp)
                        .padding(vertical = 16.dp)
                )

                // Barras à direita
                Column(modifier = Modifier.weight(2f)) {
                    (5 downTo 1).forEach { stars ->
                        val count = ratings[stars] ?: 0
                        val progress = count.toFloat() / maxCount.toFloat()

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "$stars",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(12.dp)
                                    .padding(horizontal = 2.dp)
                            )
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedbackMessages(modifier: Modifier = Modifier, rating: Int, comment: String) {

    val totalStars by remember { mutableIntStateOf(rating) }
    //val options = listOf("Muito bom", "Bom", "Regular", "Ruim", "Muito ruim") podemos testar isso em algum lugar nesta tela de feedback no futuro
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val options = listOf("Sim", "Não")

    Column(
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.highlight  ),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Mensagem de feedback"
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            (1..5) .forEach {
                Icon(
                    imageVector = if (it <= totalStars) {
                        Icons.Default.Star
                    } else {
                        Icons.Outlined.StarBorder
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(horizontal = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "•",
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "07/08/2025",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = comment,
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "102 pessoas acharam isso útil",
            fontSize = 12.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Este avaliação foi útil?",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            selectedIndex = if (selectedIndex == index) {
                                null
                            } else {
                                index
                            }
                        },
                        selected = index == selectedIndex,
                        enabled = true,
                        modifier = Modifier.height(34.dp) // altura fixa menor
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun FeedbackPreview() {
    FeedbackTab()
}


@Preview
@Composable
private fun FeedbackMenPreview() {
    FeedbackMessages(rating = 2, comment = "comment")
}

