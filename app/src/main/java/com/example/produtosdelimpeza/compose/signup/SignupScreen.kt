package com.example.produtosdelimpeza.compose.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.Screen
import com.example.produtosdelimpeza.compose.component.LimpOnButton
import com.example.produtosdelimpeza.compose.component.LimpOnTxtField
import com.example.produtosdelimpeza.model.EmailVerificationUiState
import com.example.produtosdelimpeza.viewmodels.DeepLinkViewModel
import com.example.produtosdelimpeza.viewmodels.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit = {},
    onToSignupClick: () -> Unit = {},
    onEmailLinkValid: () -> Unit = {},
    deepLinkViewModel: DeepLinkViewModel = hiltViewModel()
) {
    var passwordHidden by remember { mutableStateOf(true) }
    var confirmPasswordHidden by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val emailVerificationUiState = signUpViewModel.emailVerificationUiState.collectAsState().value
    val verticalScrollState = rememberScrollState()


    when (emailVerificationUiState) {
        EmailVerificationUiState.Idle -> {}
        EmailVerificationUiState.Loading -> {
            CircularProgressIndicator()
        }
        EmailVerificationUiState.Verified -> {
            Text("Email verificado com sucesso!")
            // Navegar para Home, se quiser
        }
        is EmailVerificationUiState.Error -> {
            Text("Erro: ${emailVerificationUiState.message}")
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackNavigation) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = stringResource(id = R.string.icon_navigate_back),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Transparent
                    )
                )
            }
        ) {contentPadding ->


            LaunchedEffect(signUpViewModel.emailVerificationUiState.collectAsState().value) {
                when (val state = signUpViewModel.emailVerificationUiState.value) {
                    EmailVerificationUiState.Verified -> {
                        Toast.makeText(
                            context,
                            "Email confirmado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        onToSignupClick
                    }

                    is EmailVerificationUiState.Error -> {
                        Toast.makeText(
                            context,
                            "Erro ao confirmar email: ${state.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }



            Column(
                modifier = Modifier
                    .padding(top = contentPadding.calculateTopPadding())
                    .verticalScroll(verticalScrollState)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Cadastre-se",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, top = 30.dp),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = Bold,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp, start = 20.dp, end = 20.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adiciona espaçamento entre os elementos
                ) {
                    TextField(
                        value = signUpViewModel.formState.name.field,
                        onValueChange = { signUpViewModel.updateName(it) },
                        label = { Text("Nome") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        isError = signUpViewModel.formState.name.error != null,
                        supportingText = { Text(signUpViewModel.formState.name.error ?: "") },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Transparent,
                            focusedContainerColor = Transparent
                        )
                    )
                    TextField(
                        value = signUpViewModel.formState.lastName.field,
                        onValueChange = { signUpViewModel.updateLastName(it) },
                        label = { Text("Sobrenome") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        isError = signUpViewModel.formState.lastName.error != null,
                        supportingText = { Text(signUpViewModel.formState.lastName.error ?: "") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Transparent,
                            focusedContainerColor = Transparent,
                            focusedTextColor = Black,
                            unfocusedTextColor = Black
                        )
                    )
                }

                LimpOnTxtField(
                    value = signUpViewModel.formState.email.field,
                    onValueChange = { signUpViewModel.updateEmail(it)},
                    label = R.string.email,
                    placeholder = R.string.hint_email,
                    errorMessage = signUpViewModel.formState.email.error,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = stringResource(id = R.string.icon_navigate_back),
                        )
                    },
                )

                LimpOnTxtField(
                    value = signUpViewModel.formState.password.field,
                    onValueChange = {
                        signUpViewModel.updatePassword(it)
                        if (signUpViewModel.formState.confirmPassword.field.isNotEmpty()) {
                            signUpViewModel.updatePasswordConfirm(it,signUpViewModel.formState.confirmPassword.field)
                        }
                    },
                    label = R.string.password,
                    placeholder = R.string.hint_password,
                    errorMessage = signUpViewModel.formState.password.error,
                    obfuscate = passwordHidden,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = stringResource(id = R.string.icon_navigate_back),
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val image = if (passwordHidden) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            }
                            val description = if (passwordHidden) {
                                stringResource(id = R.string.show_password)
                            } else {
                                stringResource(id = R.string.hide_password)
                            }

                            Icon(
                                imageVector = image,
                                contentDescription = description
                            )
                        }
                    }
                )

                LimpOnTxtField(
                    value = signUpViewModel.formState.confirmPassword.field,
                    onValueChange = { signUpViewModel.updatePasswordConfirm(signUpViewModel.formState.password.field, it) },
                    label = R.string.confirm_password,
                    placeholder = R.string.hint_confirm_password,
                    errorMessage = signUpViewModel.formState.confirmPassword.error,
                    obfuscate = confirmPasswordHidden,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = stringResource(id = R.string.icon_navigate_back),
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                            val image = if (confirmPasswordHidden) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            }
                            val description = if (confirmPasswordHidden) {
                                stringResource(id = R.string.show_password)
                            } else {
                                stringResource(id = R.string.hide_password)
                            }

                            Icon(
                                imageVector = image,
                                contentDescription = description
                            )
                        }
                    }
                )


                LimpOnButton(
                    text = R.string.to_signup,
                    enabled = signUpViewModel.formState.formIsValid,
                    modifier = Modifier
                        .padding(top = 100.dp, bottom = 20.dp),
                ) {
                    signUpViewModel.registerUser()
                }
            }

        }
    }
}



@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen()
}