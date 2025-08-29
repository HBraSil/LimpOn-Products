package com.example.produtosdelimpeza.compose.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Surface
import androidx.compose.material3.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.Screen
import com.example.produtosdelimpeza.compose.main.MainBottomNavigation
import com.example.produtosdelimpeza.ui.theme.BluishGreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(navController: NavHostController? = null) {
    val verticalScrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                MainBottomNavigation(navController!!)
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScrollState)
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Card(
                    onClick = {},
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(100.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.elevatedCardElevation(3.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.image_profile),
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                PersonalData()
                Configurations(navController)
                GeneralInformation(navController)
            }
        }


    }
}


@Composable
fun PersonalData() {
    val fieldsList = listOf("Nome", "Cidade", "Celular")
    var fieldInformationalList by remember {
        mutableStateOf(
            mutableListOf(
                "",
                "Tuntum-Ma",
                "(99) 99225-9452"
            )
        )
    }
    var isFocused by remember { mutableStateOf(Pair<Int, Boolean>(0, false)) }
    var actualName by remember { mutableStateOf("Hilquias Brasil") }
    var actualPhone by remember { mutableStateOf("Tuntum-Ma") }
    var actualCity by remember { mutableStateOf("(99) 99225-9452") }
    var newName by remember { mutableStateOf(actualName) }
    var isNamechanged by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dados pessoais",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(
            onClick = {
                actualName = newName
                isNamechanged = false
            },
            enabled = isNamechanged,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = stringResource(R.string.edit_personal_data),
            )
        }
    }


    fieldsList.forEachIndexed { index, field ->
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = field,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            BasicTextField(
                value = fieldInformationalList[index],
                onValueChange = { newValue ->
                    fieldInformationalList[index] = newValue
                    isNamechanged = fieldInformationalList[index] != actualName
                },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .onFocusChanged { focusState ->
                        isFocused = Pair(1, focusState.isFocused)
                        /*if (focusState.isFocused) {
                            isFocused = false
                        }*/
                    },
                textStyle = if (isFocused.first == 1 && isFocused.second) TextStyle(
                    fontSize = 16.sp,
                    color = Black,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold
                ) else TextStyle(
                    fontSize = 14.sp,
                    color = Gray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                )
            )
        }

        Divider(
            color = if (isFocused.first == 1 && isFocused.second) BluishGreen else Gray,
            thickness = 1.dp
        )
    }


    /*
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Cidade",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp)
            )
            BasicTextField(
                value = actualCity,
                onValueChange = { newValue ->
                    newName = newValue
                    isNamechanged = newName != actualName
                },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .onFocusChanged { focusState ->
                        isFocused = Pair(2, focusState.isFocused)
                        *//*if (focusState.isFocused) {
                        isFocused. = false
                    }*//*
                },
            textStyle = if (isFocused.first == 2 && isFocused.second) TextStyle(
                fontSize = 16.sp,
                color = Black,
                textAlign = TextAlign.Center,
                fontWeight = Bold
            ) else TextStyle(
                fontSize = 14.sp,
                color = Gray,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )
        )
    }

    Divider(color = if (isFocused.first == 2 && isFocused.second) BluishGreen else Gray, thickness = 1.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Celular", modifier = Modifier.padding(start = 20.dp))
        BasicTextField(
            value = actualPhone,
            onValueChange = { newValue ->
                newName = newValue
                isNamechanged = newName != actualName
            },
            modifier = Modifier
                .padding(end = 20.dp)
                .onFocusChanged { focusState ->
                    isFocused = Pair(3, focusState.isFocused)
                    *//*if (focusState.isFocused) {
                        isFocused = false
                    }*//*
                },
            textStyle = if(isFocused.first == 3 && isFocused.second) TextStyle(
                fontSize = 16.sp,
                color = Black,
                textAlign = TextAlign.Center,
                fontWeight = Bold
            ) else {
                TextStyle(
                    fontSize = 14.sp,
                    color = Gray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                )
            }
        )
    }

    Divider(color = if (isFocused.first == 3 && isFocused.second) BluishGreen else Gray, thickness = 1.dp)*/
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configurations(navController: NavHostController?) {
    var checkedDefaultMode by remember { mutableStateOf(false) }
    var checkedDarkMode by remember { mutableStateOf(false) }
    var isCustomer by remember { mutableStateOf(true) }
    var isSheetOpen by remember { mutableStateOf(false) }


    val configurationsItemList = listOf(
        stringResource(R.string.enable_cupons),
        stringResource(R.string.dark_mode),
        stringResource(R.string.manage_notifications),
        if (isCustomer)stringResource(R.string.become_a_seller) else null
    )

    Text(
        text = "Configurações",
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 50.dp),
        fontWeight = ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
    )

    configurationsItemList.forEachIndexed { index, item ->
        if (item != null) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        when (index) {
                            //0 -> TODO: Habilitar tela de cupons
                            1 -> isSheetOpen = !isSheetOpen
                            2 -> navController!!.navigate(Screen.NOTIFICATION.route)
                            3 -> navController!!.navigate(Screen.SELLER_REGISTER.route)
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item,
                    color = MaterialTheme.colorScheme.onBackground
                )

                when (item) {
                    stringResource(R.string.enable_cupons) -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = stringResource(R.string.navigate_screen_enable_coupons),
                        )
                    }

                    stringResource(R.string.dark_mode) -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = stringResource(R.string.navigate_screen_enable_coupons),
                        )
                    }

                    stringResource(R.string.manage_notifications) -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = stringResource(R.string.navigate_screen_enable_coupons),
                        )
                    }

                    stringResource(R.string.become_a_seller) -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = stringResource(R.string.navigate_screen_become_a_seller),
                        )
                    }
                }

            }
            Divider(color = Gray, thickness = 1.dp)
        }
    }


    val optionsLightModeList = listOf("Usar configuração do sistema", "Ativar modo escuro")
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                isSheetOpen = false
            },
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 20.dp),
            ) {
                optionsLightModeList.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it)
                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                        Switch(checked = checkedDefaultMode, onCheckedChange = { checkedDefaultMode = it })
                    }

                }
            }
        }
    }}


@Composable
fun GeneralInformation(navController: NavHostController?) {
    Text(
        text = "Informações Gerais",
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 50.dp),
        fontWeight = ExtraBold
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .clickable {}
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Avalie o App")

        Spacer(Modifier.weight(1f))


        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = stringResource(R.string.icon_navigate_next_to_feedback),
        )
    }

    Divider(color = Gray, thickness = 1.dp)

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clickable {
                navController!!.navigate(Screen.ABOUT.route)
            }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Sobre")

        Spacer(Modifier.weight(1f))


        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = stringResource(R.string.icon_navigate_next),
        )
    }

    Divider(color = Gray, thickness = 1.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Sair", modifier = Modifier.padding(start = 20.dp))
    }

    Spacer(modifier = Modifier.padding(bottom = 20.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet() {

}


@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}