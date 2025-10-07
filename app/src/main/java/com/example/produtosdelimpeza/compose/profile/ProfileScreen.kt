package com.example.produtosdelimpeza.compose.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BrightnessMedium
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.compose.Screen
import com.example.produtosdelimpeza.compose.main.MainBottomNavigation


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(navController: NavHostController? = null) {
    val verticalScrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
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
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Card(
                    onClick = {},
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
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
    val fieldsList = listOf("Nome", "Email", "Celular", "Cidade")
    var fieldInformationalList by remember {
        mutableStateOf(
            mutableListOf(
                "Hilquias Brasil",
                "hilquias.brasil@gmail.com",
                "(99) 99225-9452",
                "Tuntum-Ma",
            )
        )
    }
    var iconsList by remember {
        mutableStateOf(
            mutableListOf(
                Icons.Outlined.PersonOutline,
                Icons.Outlined.Email,
                Icons.Outlined.Phone,
                Icons.Outlined.LocationCity,
            )
        )
    }
    var isFocused by remember { mutableStateOf(Pair<Int, Boolean>(0, false)) }
    var actualName by remember { mutableStateOf("Hilquias Brasil") }
    var newName by remember { mutableStateOf(actualName) }
    var isNamechanged by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Dados pessoais",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp),
            fontWeight = ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = {
                actualName = newName
                isNamechanged = false
            },
            enabled = isNamechanged,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = stringResource(R.string.edit_personal_data),
            )
        }
    }


    fieldsList.forEachIndexed { index, field ->
        Row(
            modifier = Modifier.padding(start = 35.dp)
        ) {

            Icon(
                imageVector = iconsList[index],
                contentDescription = stringResource(R.string.icon_personal_data),
            )

            Spacer(Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = field,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(10.dp))

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
                        fontWeight = Bold
                    ) else TextStyle(
                        fontSize = 14.sp,
                        color = Gray,
                        fontStyle = FontStyle.Italic,
                    )
                )
            }
        }

        Spacer(Modifier.height(2.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configurations(navController: NavHostController?) {
    var checkedDefaultMode by remember { mutableStateOf(false) }
    var isSheetOpen by remember { mutableStateOf(false) }


    val configurationsItemList = listOf(
        stringResource(R.string.enable_cupons),
        stringResource(R.string.dark_mode),
        stringResource(R.string.manage_notifications),
    )

    var iconsList by remember {
        mutableStateOf(
            mutableListOf(
                Icons.Outlined.LocalOffer,
                Icons.Outlined.BrightnessMedium,
                Icons.Outlined.Notifications
            )
        )
    }

    Text(
        text = "Configurações",
        fontSize = 20.sp,
        modifier = Modifier.padding(top = 20.dp, start = 15.dp, bottom = 10.dp),
        fontWeight = ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
    )

    configurationsItemList.forEachIndexed { index, item ->
        Row(
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .fillMaxWidth()
                .clickable {
                    when (index) {
                        //0 -> TODO: Habilitar tela de cupons
                        1 -> isSheetOpen = !isSheetOpen
                        2 -> navController!!.navigate(Screen.NOTIFICATION.route)
                    }
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = iconsList[index],
                contentDescription = stringResource(R.string.icon_configurations),
            )

            Spacer(Modifier.width(20.dp))

            Text(
                text = item,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.weight(1f))

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
            }

        }
        Spacer(Modifier.height(2.dp))
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it)
                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                        Switch(
                            checked = checkedDefaultMode,
                            onCheckedChange = { checkedDefaultMode = it },
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
    }}



@Composable
fun GeneralInformation(navController: NavHostController?) {

    val generalInformationList = listOf(
        stringResource(R.string.feedback),
        stringResource(R.string.about),
    )


    var iconsList by remember {
        mutableStateOf(
            mutableListOf(
                Icons.Outlined.Feedback,
                Icons.Outlined.Info,
            )
        )
    }

    Text(
        text = "Informações Gerais",
        fontSize = 20.sp,
        modifier = Modifier.padding(top = 20.dp, start = 15.dp, bottom = 10.dp),
        fontWeight = ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
    )

    generalInformationList.forEachIndexed {index, item ->
        Row(
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .clickable {
                    when (index) {
                        //stringResource(R.string.feedback) -> navController!!.navigate(Screen.FEEDBACK.route)
                        1 -> navController!!.navigate(Screen.ABOUT.route)
                    }
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconsList[generalInformationList.indexOf(item)],
                contentDescription = stringResource(R.string.icon_configurations),
            )

            Spacer(Modifier.width(20.dp))

            Text(
                text = item,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.weight(1f))


            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = stringResource(R.string.icon_navigate_next_to_feedback),
            )
        }

        Spacer(Modifier.height(2.dp))

    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 35.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = stringResource(R.string.icon_navigate_back),
        )
        Text(
            text = "Sair",
            modifier = Modifier.padding(start = 20.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    Spacer(modifier = Modifier.padding(bottom = 20.dp))
}



@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}