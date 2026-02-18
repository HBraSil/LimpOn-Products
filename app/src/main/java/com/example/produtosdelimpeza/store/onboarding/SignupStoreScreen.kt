package com.example.produtosdelimpeza.store.onboarding

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.component.LimpOnDescriptionTextField
import com.example.produtosdelimpeza.core.component.LimpOnTextField
import com.example.produtosdelimpeza.core.component.SimpleTimePicker
import com.example.produtosdelimpeza.core.domain.model.BusinessHours
import com.example.produtosdelimpeza.core.domain.model.DayOfWeek
import com.example.produtosdelimpeza.store.onboarding.presentation.extension.displayName
import java.time.format.DateTimeFormatter
import java.util.UUID

data class TimeInterval(
    val id: String = UUID.randomUUID().toString(),
    val start: String,
    val end: String,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupStoreScreen(
    onBackNavigation: () -> Unit,
    onNavigateToStore: () -> Unit = {},
    onBackToProfile: () -> Unit = {},
    signUpStoreViewModel: SignUpStoreViewModel = hiltViewModel(),
) {
    var showDialog by remember { mutableStateOf(false) }

    val formState = signUpStoreViewModel.formState
    val isConfirmationButtonValid by signUpStoreViewModel.isButtonValid.collectAsState()
    val isScheduleValid by signUpStoreViewModel.isScheduleValid.collectAsState()


    val categories = listOf(
        "Restaurante",
        "Mercado",
        "Padaria",
        "Loja de roupas",
        "Loja de eletrônicos",
        "Farmácia",
        "Produtos de Limpeza",
        "Outro"
    )

    var isScheduleExpanded by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Criar sua loja", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                StoreLogoPicker()
            }
            item {
                Section(title = "Identidade da loja") {
                    LimpOnTextField(
                        value = formState.storeName.field,
                        onValueChange = {
                            signUpStoreViewModel.updateName(it)
                        },
                        errorMessage = formState.storeName.error,
                        label = R.string.business_name,
                        placeholder = R.string.business_name_example,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Store,
                                stringResource(R.string.icon_store)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }
            }
            item {
                Section(title = "Sobre a loja") {
                    LimpOnDescriptionTextField(formState.description.field) {
                        signUpStoreViewModel.updateDescription(it)
                    }
                }
            }
            item {
                Section(title = "Tipo de estabelecimento") {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {
                                selectedCategory = it
                                signUpStoreViewModel.updateCategory(it)
                            },
                            readOnly = true,
                            label = { Text("Tipo de estabelecimento") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        selectedCategory = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                Section(title = "Email de contato") {
                    LimpOnTextField(
                        value = formState.email.field,
                        onValueChange = {
                            signUpStoreViewModel.updateEmail(it)
                        },
                        label = R.string.email,
                        placeholder = R.string.hint_email,
                        errorMessage = formState.email.error,
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().offset(y = (-20).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Usar o mesmo email desta conta",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Checkbox(
                            checked = true,
                            onCheckedChange = {},
                        )
                    }
                }
            }
            item {
                Section(modifier = Modifier.offset(y = (-40).dp), title = "Configurações gerais") {
                    Spacer(Modifier.height(10.dp))
                    StoreSetupQuickActions(
                        onLocationClick = {},
                        onScheduleClick = {
                            isScheduleExpanded = true
                        }
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        showDialog = true
                        signUpStoreViewModel.createStore()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    enabled = isConfirmationButtonValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        "Finalizar cadastro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (isScheduleExpanded) {
        ScheduleBottomSheet(
            onDismiss = { isScheduleExpanded = false },
            isScheduleValid = isScheduleValid,
            allDaySchedule = { time ->
                signUpStoreViewModel.updateAllDaysSchedule(time)
            },
            individualDaySchedule = { day, time ->
                signUpStoreViewModel.updateDaySchedule(day, time)
            },
            saveSchedule = {
                signUpStoreViewModel.save()
            }
        )
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 12.dp,
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Para onde vamos?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Você pode acessar o painel da sua loja ou continuar no perfil.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    NavigationActionCard(
                        title = "Painel da Loja",
                        subtitle = "Dashboard, vendas e estatísticas",
                        icon = Icons.Default.Storefront,
                        isPrimary = true,
                        onClick = {
                            showDialog = false
                            onNavigateToStore()
                        }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    NavigationActionCard(
                        title = "Meu Perfil",
                        subtitle = "Editar informações pessoais",
                        icon = Icons.Default.Person,
                        isPrimary = false,
                        onClick = {
                            showDialog = false
                            onBackToProfile()
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    onDismiss: () -> Unit,
    isScheduleValid: Boolean = false,
    allDaySchedule: (BusinessHours) -> Unit,
    individualDaySchedule: (DayOfWeek, BusinessHours) -> Unit,
    saveSchedule: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight().statusBarsPadding(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Horário de Funcionamento",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Configure quando sua loja estará aberta para os clientes.",
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(24.dp))
            var applyToAllDays by remember { mutableStateOf(false) }
            SelectAllCard(
                checked = applyToAllDays,
                onCheckedChange = { applyToAllDays = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(targetState = applyToAllDays, label = "") { isAll ->
                if (isAll) {
                    AllDaysScheduleCard { time ->
                        allDaySchedule(time)
                    }
                } else {
                    IndividualDaysSchedule { time, day ->
                        individualDaySchedule(time, day)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ElevatedButton(
                onClick = saveSchedule,
                enabled = isScheduleValid,
            ) {
                Text(text = "Salvar")
            }
        }
    }
}


@Composable
private fun SelectAllCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Selecionar todos os dias",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Aplicar mesmo horário para todos",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = MaterialTheme.colorScheme.background,

                    uncheckedThumbColor = MaterialTheme.colorScheme.background,
                    checkedThumbColor = MaterialTheme.colorScheme.background,

                    uncheckedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.6f),
                    checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AllDaysScheduleCard(
    schedulingTime: (BusinessHours) -> Unit,
) {
    var intervals by remember {
        mutableStateOf(
            listOf(TimeInterval(start = "08:00", end = "18:00"))
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "Todos os dias",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            intervals.forEach { _ ->
                IntervalRow { time ->
                    schedulingTime(time)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "+  Adicionar Intervalo",
                color = Color(0xFF4F8CFF),
                modifier = Modifier.clickable {
                    intervals = intervals + TimeInterval(
                        start = "08:00",
                        end = "18:00"
                    )
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun IndividualDaysSchedule(
    schedulingTime: (DayOfWeek, BusinessHours) -> Unit,
) {

    var intervals by remember {
        mutableStateOf(
            listOf(TimeInterval(start = "08:00", end = "18:00"))
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        DayOfWeek.entries.forEach { day ->
            var isEnabled by remember { mutableStateOf(false) }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = if (isEnabled) Color(0xFF4F8CFF) else Color.Gray
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = day.displayName(),
                            color = if (isEnabled) MaterialTheme.colorScheme.onBackground else Color.Gray,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it },
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = MaterialTheme.colorScheme.background,

                                uncheckedThumbColor = MaterialTheme.colorScheme.background,
                                checkedThumbColor = MaterialTheme.colorScheme.background,

                                uncheckedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    0.6f
                                ),
                                checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        )
                    }

                    AnimatedVisibility(visible = isEnabled) {

                        Column {

                            Spacer(modifier = Modifier.height(16.dp))

                            intervals.forEach { _ ->
                                IntervalRow { time ->
                                    schedulingTime(day, time)
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                            }


                            Text(
                                text = "+  Adicionar Intervalo",
                                color = Color(0xFF4F8CFF),
                                modifier = Modifier
                                    .clickable {
                                        intervals = intervals + TimeInterval(
                                            start = "08:00",
                                            end = "18:00"
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun IntervalRow(
    operationTimeChange: (BusinessHours) -> Unit,
) {
    var selectedOpenTime by remember { mutableStateOf("Abre") }
    var selectedCloseTime by remember { mutableStateOf("Fecha") }

    var showDialog by remember { mutableStateOf(false) }
    var idButton by remember { mutableIntStateOf(0) }

    val bussiness = BusinessHours()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    LaunchedEffect(
        selectedOpenTime,
        selectedCloseTime
    ) {
        val openFilteredTime = selectedOpenTime.filter { it != ':' }.isDigitsOnly()
        val closeFilteredTime = selectedCloseTime.filter { it != ':' }.isDigitsOnly()

        if (openFilteredTime && closeFilteredTime) {
            Log.d("teste", "chegou aqui")
            operationTimeChange(
                bussiness.copy(
                    openTime = selectedOpenTime,
                    closeTime = selectedCloseTime
                )
            )
        }
    }


    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = {
                idButton = 1
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (idButton == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = selectedOpenTime
            )
        }


        ElevatedButton(
            onClick = {
                idButton = 2
                if (showDialog) {
                    showDialog = false
                }
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (idButton == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = selectedCloseTime
            )
        }
    }


    if (showDialog) {
        SimpleTimePicker(
            show = true,
            onConfirm = {
                if (idButton == 1) selectedOpenTime = it.format(formatter)
                else {
                    selectedCloseTime = it.format(formatter)

                }
                showDialog = false
            },
            onClose = {
                showDialog = false
            }
        )
    }
}


@Composable
fun StoreSetupQuickActions(
    onLocationClick: () -> Unit,
    onScheduleClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SetupCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.LocationOn,
            title = "Localização",
            subtitle = "Definir endereço",
            onClick = onLocationClick
        )

        SetupCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.AccessTime,
            title = "Horário",
            subtitle = "Configurar dias/horas",
            onClick = onScheduleClick
        )
    }
}

@Composable
private fun SetupCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background.copy(0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NavigationActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit,
) {
    val containerColor =
        if (isPrimary) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.secondary

    val contentColor =
        if (isPrimary) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.background

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        tonalElevation = if (isPrimary) 6.dp else 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = contentColor.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.icon_arrow_forward),
                tint = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}


@Composable
fun StoreLogoPicker() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Adicionar logo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "Logotipo da loja",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun RequestInvitePreview() {
}