package com.example.produtosdelimpeza.core.auth.presentation.signup

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnAuthButton
import com.example.produtosdelimpeza.core.component.LimpOnTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onBackNavigation: () -> Unit = {},
) {
    var passwordHidden by remember { mutableStateOf(true) }
    var confirmPasswordHidden by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    var showEmailSentSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(showEmailSentSnackbar) {
        if (showEmailSentSnackbar) {
            val result = snackbarHostState.showSnackbar(
                message = "Email de confirmação enviado",
                actionLabel = "Abrir email"
            )
            if (result == SnackbarResult.ActionPerformed) {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_EMAIL)
                }
                context.startActivity(intent)
            }
            //showEmailSentSnackbar = false
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
                                contentDescription = stringResource(id = R.string.icon_navigation_back),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Transparent
                    )
                )
            },
            snackbarHost = { SnackbarHost(modifier = Modifier.padding(bottom = 20.dp), hostState = snackbarHostState)}
        ) {contentPadding ->
            Column(
                modifier = Modifier
                    .padding(top = contentPadding.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
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

                LimpOnTextField(
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
                            contentDescription = stringResource(id = R.string.icon_navigation_back),
                        )
                    },
                )

                LimpOnTextField(
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
                            contentDescription = stringResource(id = R.string.icon_navigation_back),
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

                LimpOnTextField(
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
                            contentDescription = stringResource(id = R.string.icon_navigation_back),
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


                LimpOnAuthButton(
                    text = R.string.to_signup,
                    enabled = signUpViewModel.formState.formIsValid,
                    modifier = Modifier
                        .padding(top = 100.dp, bottom = 20.dp),
                ) {
                    //if (isConnected) {
                        signUpViewModel.registerUser()
                         showEmailSentSnackbar = true
                        return@LimpOnAuthButton

                }
            }
        }
    }
}
