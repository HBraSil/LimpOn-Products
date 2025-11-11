package com.example.produtosdelimpeza.compose.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(onNavigateBack: () -> Unit = {}) {
    var checkedDefaultMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Notificações")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { contentPadding ->
        val notificationList = listOf(
            "Whatsapp",
            "E-mail",
            "SMS",
            "Barra de notificações"
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = contentPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = "Cupons",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Ative para receber notificações de cupons disponíveis",
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )

                notificationList.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.weight(1f))

                        Switch(
                            checked = false,
                            onCheckedChange = {},
                            thumbContent = if (checkedDefaultMode) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            }
                        )
                    }
                }
            }

            Column {
                Text(
                    text = "Promoções",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Ative para receber notificações de promoções",
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )

                notificationList.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.weight(1f))

                        Switch(
                            checked = false,
                            onCheckedChange = {},
                            thumbContent = if (checkedDefaultMode) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun NotificationScreenPreview() {
    NotificationScreen()
}