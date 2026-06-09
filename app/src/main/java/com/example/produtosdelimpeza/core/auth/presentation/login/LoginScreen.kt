package com.example.produtosdelimpeza.core.auth.presentation.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.auth.presentation.AuthUiState
import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.component.LimpOnAuthButton
import com.example.produtosdelimpeza.core.component.LimpOnTextField
import com.example.produtosdelimpeza.core.presentation.NavigationLastUserModeViewModel
import com.example.produtosdelimpeza.core.theme.ProdutosDeLimpezaTheme
import com.example.produtosdelimpeza.core.ui.util.asString
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigationLastUserModeViewModel: NavigationLastUserModeViewModel = hiltViewModel(),
) {
    val uiState by loginViewModel.loginUiState.collectAsState()
    val formState by loginViewModel.loginFormState.collectAsState()

    LoginContent(
        uiState = uiState,
        loginFormState = formState,
        onEvent = loginViewModel::onEvent,
        onSignupClick = onSignupClick,
        onLoginClick = onLoginClick,
        saveLastUserMode = {
            navigationLastUserModeViewModel.saveLastUserMode(ProfileMode.LoggedOut)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    uiState: AuthUiState = AuthUiState(),
    loginFormState: LoginFormState,
    onEvent: (LoginFormEvent) -> Unit = {},
    onSignupClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    saveLastUserMode: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        saveLastUserMode()
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
                                text = stringResource(R.string.register_here),
                                style = MaterialTheme.typography.titleMedium,
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
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.limp_on_light_logo),
                    contentDescription = stringResource(R.string.login_image),
                )
                Text(
                    text = stringResource(R.string.do_login),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = Bold,
                )
                ContentLoginScreen(
                    state = uiState,
                    loginFormState = loginFormState,
                    onLoginClick = onLoginClick,
                    onEvent = onEvent
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
    state: AuthUiState = AuthUiState(),
    loginFormState: LoginFormState,
    onLoginClick: () -> Unit,
    onEvent: (LoginFormEvent) -> Unit,
) {

    val context = LocalContext.current
    val callbackManager = remember {
        CallbackManager.Factory.create()
    }
    val fbLauncher = rememberLauncherForActivityResult(
        LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    ) { result ->
        LoginManager.getInstance().onActivityResult(
            result.resultCode,
            result.data,
            object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    //loginFormState.loginWithFacebook(result.accessToken.token)
                }
                override fun onCancel() {
                    println("onCancel")
                }
                override fun onError(error: FacebookException) {
                    println("onError $error")
                }
            }
        )
    }

    LaunchedEffect(state) {
        if(state.success) onLoginClick()
    }

    state.error?.let { errorMessage ->

        Toast.makeText(
            context,
            errorMessage,
            Toast.LENGTH_LONG
        ).show()

        onEvent(
            LoginFormEvent.CleanErrorMessage
        )
    }


    LimpOnTextField(
        value = loginFormState.email.field,
        onValueChange = { onEvent(LoginFormEvent.UpdateEmail(it)) },
        modifier = Modifier.padding(top = 10.dp,start = 20.dp, end = 20.dp),
        label = R.string.email,
        placeholder = R.string.hint_email,
        errorMessage = loginFormState.email.error?.asString(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(id = R.string.type_email),
                tint = MaterialTheme.colorScheme.primary
            )
        },
    )


    LimpOnTextField(
        value = loginFormState.password.field,
        onValueChange = { onEvent(LoginFormEvent.UpdatePassword(it)) },
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        label = R.string.password,
        placeholder = R.string.hint_password,
        obfuscate = loginFormState.isPasswordHidden,
        errorMessage = loginFormState.password.error?.asString(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = stringResource(id = R.string.type_password),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { onEvent(LoginFormEvent.ChangePasswordVisibility) }) {
                val image = if (loginFormState.isPasswordHidden) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                val description = if (loginFormState.isPasswordHidden) {
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
            loginFormState.email.field.isNotEmpty() &&
            loginFormState.password.field.isNotEmpty()
        ){
            onEvent(LoginFormEvent.LoginWithEmailAndPassword)
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
                        onEvent(LoginFormEvent.LoginWithGoogle)
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
                fbLauncher.launch(listOf("public_profile", "email"))
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
        LoginContent(
            uiState = AuthUiState(),
            loginFormState = LoginFormState(),
            onEvent = {},
            onSignupClick = {},
            onLoginClick = {},
            saveLastUserMode = {}
        )
    }
}
