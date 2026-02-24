package com.example.produtosdelimpeza.core.auth.presentation.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.component.LimpOnAuthButton
import com.example.produtosdelimpeza.core.component.LimpOnTextField
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import com.example.produtosdelimpeza.core.theme.ProdutosDeLimpezaTheme
import com.example.produtosdelimpeza.core.ui.util.asString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSignupClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel(),
) {
    val verticalScrollState = rememberScrollState()
    val uiState by loginViewModel.loginUiState.collectAsState()


    LaunchedEffect(Unit) {
        navigationLastUserModeViewModel.saveLastUserMode(ProfileMode.LoggedOut)
    }


    Box {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        TextButton(
                            onClick = onSignupClick,
                            modifier = Modifier.padding(end = 20.dp)
                        ) {
                            Text(
                                text = "Registre-se aqui",
                                fontSize = 14.sp,
                                fontWeight = ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = contentPadding.calculateTopPadding())
                    .verticalScroll(verticalScrollState)
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Spacer(Modifier.height(20.dp))
                Image(
                    painter = painterResource(R.drawable.limp_on_light_logo),
                    contentDescription = stringResource(R.string.login_image),
                )
                Text(
                    text = "Faça seu login",
                    fontSize = 20.sp,
                    fontWeight = Bold,
                )
                ContentLoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginClick = onLoginClick,
                )
            }
        }

        if (uiState.isLoading) {
            LoadingLoginOverlay()
        }
    }
}

@Composable
fun ContentLoginScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
) {

    val context = LocalContext.current
    val state by loginViewModel.loginUiState.collectAsState()
    val passwordHidden by loginViewModel.passwordHidden.collectAsState()


    LaunchedEffect(state) {
        if(state.success) {
            onLoginClick()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { errorMessage ->

            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()

            loginViewModel.cleanErrorMessage()
        }
    }


    LimpOnTextField(
        value = loginViewModel.loginFormState.email.field,
        onValueChange = { loginViewModel.updateEmail(it) },
        modifier = Modifier.padding(top = 10.dp,start = 20.dp, end = 20.dp),
        label = R.string.email,
        placeholder = R.string.hint_email,
        errorMessage = loginViewModel.loginFormState.email.error?.asString(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(id = R.string.type_email),
                tint = MaterialTheme.colorScheme.primary
            )
        },
    )

    LimpOnTextField(
        value = loginViewModel.loginFormState.password.field,
        onValueChange = { loginViewModel.updatePassword(it) },
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        label = R.string.password,
        placeholder = R.string.hint_password,
        obfuscate = passwordHidden,
        errorMessage = loginViewModel.loginFormState.password.error?.asString(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = stringResource(id = R.string.type_password),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { loginViewModel.changePasswordVisibility() }) {
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Esqueceu a senha?",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall
        )
    }

    LimpOnAuthButton(
        text = R.string.start,
        modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
    ){
        if (
            loginViewModel.loginFormState.email.field.isNotEmpty() &&
            loginViewModel.loginFormState.password.field.isNotEmpty()
        ){
            loginViewModel.loginWithEmailAndPassword()
        }

        if (state.error != null) {
            Toast.makeText(
                context,
                "state.error",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            onClick = {},
            modifier = Modifier.padding(start = 30.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Image(
                painter = painterResource(id = R.drawable.light_google_logo),
                contentDescription = stringResource(R.string.google_logo),
                modifier = Modifier
                    .size(30.dp)
                    .wrapContentSize(Alignment.Center)
                    .clickable {
                        loginViewModel.signInWithGoogle()
                    }
            )
        }

        HorizontalDivider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .weight(1f), 0.dp, Black)

        Text(
            text = stringResource(R.string.options_to_access),
            color = Gray,
        )

        HorizontalDivider(modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .weight(1f), 0.dp, Black)

        FloatingActionButton(
            onClick = {
                loginViewModel.loginWithFacebook(
                    activity = context as Activity,
                    onSuccess = {
                    },
                    onError = {
                        Log.e("FACEBOOK", "Erro no login", it)
                    }
                )
            },
            modifier = Modifier.padding(end = 30.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Image(
                painter = painterResource(R.drawable.light_facebook_logo),
                contentDescription = stringResource(R.string.facebook_logo),
                modifier = Modifier
                    .size(30.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ProdutosDeLimpezaTheme {
        LoginScreen(onSignupClick = {})
    }
}