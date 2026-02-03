package com.example.produtosdelimpeza.store.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

enum class SellerType {
    STORE,
    AUTONOMOUS
}

@Composable
fun PartnerRequestEntryScreen(
    onContinue: (SellerType) -> Unit
) {
    var selectedType by remember { mutableStateOf<SellerType?>(null) }

    Scaffold { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {

            // Ícone decorativo de fundo (bem suave)
            Icon(
                imageVector = Icons.Rounded.Storefront,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
                    .size(120.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(72.dp))

                Text(
                    text = "Solicitar acesso para vender",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Selecione como você deseja vender. Sua solicitação será analisada antes da liberação",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                SellerTypeOptionTile(
                    title = "Tenho uma loja física ou comercial",
                    description = "Possuo um estabelecimento e quero vender meus produtos pelo app",
                    icon = Icons.Rounded.Store,
                    selected = selectedType == SellerType.STORE,
                    onClick = { selectedType = SellerType.STORE }
                )

                Spacer(modifier = Modifier.height(18.dp))

                SellerTypeOptionTile(
                    title = "Sou autônomo",
                    description = "Vendo por conta própria, sem loja física",
                    icon = Icons.Rounded.PersonOutline,
                    selected = selectedType == SellerType.AUTONOMOUS,
                    onClick = { selectedType = SellerType.AUTONOMOUS }
                )

                Spacer(modifier = Modifier.weight(1f))

                val enabled = selectedType != null

                val buttonScale by animateFloatAsState(
                    targetValue = if (enabled) 1f else 0.96f,
                    animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
                    label = "buttonScale"
                )

                val buttonAlpha by animateFloatAsState(
                    targetValue = if (enabled) 1f else 0.6f,
                    animationSpec = tween(300),
                    label = "buttonAlpha"
                )

                FilledTonalButton(
                    onClick = { selectedType?.let { onContinue(it) } },
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .graphicsLayer {
                            scaleX = buttonScale
                            scaleY = buttonScale
                            alpha = buttonAlpha
                        },
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Continuar")
                }


                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}


@Composable
fun SellerTypeOptionTile(
    title: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "cardScale"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) 6.dp else 0.dp,
        animationSpec = tween(250),
        label = "cardElevation"
    )

    val containerColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.secondaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        label = "cardColor"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        tonalElevation = elevation,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = selected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@Preview
@Composable
fun Teste2() {
    PartnerRequestEntryScreen{}
}
