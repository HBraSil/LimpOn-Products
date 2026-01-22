package com.example.produtosdelimpeza.compose.seller.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterInviteKeyScreen(
    onBackNavigation: () -> Unit,
    onConfirm: () -> Unit
) {
    var code by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = List(6) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var inviteKey by remember { mutableStateOf("") }
    val isKeyValid = inviteKey.length >= 6 // Exemplo de validação simples

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBFF))
            .padding(24.dp)
    ) {
        // Botão Voltar
        IconButton(onClick = onBackNavigation, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Voltar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Cabeçalho
        Text(
            text = "Digite seu convite",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1C1B1F)
        )

        Text(
            text = "Insira o código de 6 dígitos que você recebeu por e-mail ou WhatsApp.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF49454F),
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            code.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        // aceita apenas dígitos e no máximo 1 char
                        val filtered = newValue.filter { it.isDigit() }.take(1)
                        if (filtered.isNotEmpty()) {
                            // Aqui estamos transformando a lista em um mutableList e, com also, atribuindo a ela um novo valor, que vem da variável filtered
                            code = code.toMutableList().also {it[index] = filtered}

                            if (index < 5)
                                focusRequesters[index + 1].requestFocus()
                            else
                                focusManager.clearFocus()
                        }
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        // color = Color.Black
                    ),
                    modifier = Modifier.size(52.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent{ keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                                when {
                                    value.isNotEmpty() -> {
                                        code = code.toMutableList().also { it[index] = "" }
                                        focusRequesters[index].requestFocus()
                                        true
                                    }
                                    // Campo atual vazio: apaga o anterior e foca nele
                                    index > 0 -> {
                                        code = code.toMutableList().also { it[index - 1] = "" }
                                        focusRequesters[index - 1].requestFocus()
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                false
                            }
                        },
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        LaunchedEffect(Unit) {focusRequesters[0].requestFocus()}
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onConfirm() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6750A4),
                disabledContainerColor = Color(0xFFEADDFF)
            )
        ) {
            Text(
                text = "Confirmar Convite",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(40.dp))
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun EnterInviteKeyPreview() {
    MaterialTheme {
        EnterInviteKeyScreen(onBackNavigation = {}, onConfirm = {})
    }
}