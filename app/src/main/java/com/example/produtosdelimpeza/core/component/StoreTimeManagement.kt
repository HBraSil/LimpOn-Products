package com.example.produtosdelimpeza.core.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale



// Enum para controlar o modo de operação
enum class ShopStatusMode {
    MANUAL,
    AUTOMATIC
}
enum class ManualStatus {
    OPEN,
    CLOSED
}
data class WorkingRule(
    val days: Set<DayOfWeek>,
    val openTime: String,
    val closeTime: String
)


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTimeManagement(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedMode by remember { mutableStateOf(ShopStatusMode.MANUAL) }
    var manualStatus by remember { mutableStateOf(ManualStatus.OPEN) }


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ModeSelector(
                selectedMode = selectedMode,
                onModeChange = { selectedMode = it }
            )

            Spacer(Modifier.height(24.dp))

            AnimatedContent(
                targetState = selectedMode,
                label = "sheet-content"
            ) { mode ->
                var workingRules by remember { mutableStateOf(listOf(WorkingRule(
                    days = setOf(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY
                    ),
                    openTime = "08:00",
                    closeTime = "18:00"
                ))) }

                when (mode) {
                    ShopStatusMode.MANUAL ->
                        ManualStatusSection(
                            manualStatus = manualStatus,
                            onStatusChange = { manualStatus = it }
                        )

                    ShopStatusMode.AUTOMATIC ->
                        AutomaticScheduleSection(
                            rules = workingRules,
                            onAddRule = {
                                workingRules = workingRules + WorkingRule(
                                    days = setOf(
                                        DayOfWeek.MONDAY,
                                        DayOfWeek.TUESDAY,
                                        DayOfWeek.WEDNESDAY,
                                        DayOfWeek.THURSDAY,
                                        DayOfWeek.FRIDAY
                                    ),
                                    openTime = "08:00",
                                    closeTime = "18:00"
                                )
                            },
                            onRuleChange = { index, updatedRule ->
                                workingRules = workingRules.toMutableList().apply {
                                    this[index] = updatedRule
                                }
                            }
                        )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(stringResource(R.string.save_changes))
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun AutomaticScheduleSection(
    rules: List<WorkingRule>,
    onAddRule: () -> Unit,
    onRuleChange: (Int, WorkingRule) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Horários de funcionamento",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onAddRule) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Adicionar")
            }
        }
        Spacer(Modifier.height(12.dp))
        rules.forEachIndexed { index, rule ->
            WorkingRuleForm(
                rule = rule,
                onRuleChange = { updated ->
                    onRuleChange(index, updated)
                }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkingRuleForm(
    rule: WorkingRule,
    onRuleChange: (WorkingRule) -> Unit
) {

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            DaysOfWeekSelector(
                selectedDays = rule.days,
                onDayToggle = { day ->
                    val updatedDays =
                        if (rule.days.contains(day)) rule.days - day
                        else rule.days + day

                    onRuleChange(rule.copy(days = updatedDays))
                }
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Text(
                    text = "Horário de funcionamento",
                    style = MaterialTheme.typography.labelLarge
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CompactTimeInput(
                        label = "Abertura",
                        time = rule.openTime
                    ) {
                        onRuleChange(rule.copy(openTime = it))
                    }

                    CompactTimeInput(
                        label = "Fechamento",
                        time = rule.closeTime
                    ) {
                        onRuleChange(rule.copy(closeTime = it))
                    }
                }
            }
        }
    }
}


@Composable
fun CompactTimeInput(
    label: String,
    time: String,
    modifier: Modifier = Modifier,
    onTimeChange: (String) -> Unit,
) {
    val (initialHour, initialMinute) = remember(time) {
        val parts = time.split(":")
        val h = parts.getOrNull(0)?.padStart(2, '0') ?: "00"
        val m = parts.getOrNull(1)?.padStart(2, '0') ?: "00"
        h to m
    }

    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }

    val minuteFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)

        Spacer(Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            TimeBlock(
                title = "Hora",
                value = hour,
                imeAction = ImeAction.Next,
                onNext = { minuteFocusRequester.requestFocus() }
            ) {
                hour = it
                onTimeChange("$hour:$minute")
            }

            Text(
                text = ":",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )

            TimeBlock(
                title = "Minuto",
                value = minute,
                imeAction = ImeAction.Done,
                focusRequester = minuteFocusRequester
            ) {
                minute = it
                onTimeChange("$hour:$minute")
            }
        }
    }
}

@Composable
private fun TimeBlock(
    title: String,
    value: String,
    imeAction: ImeAction,
    focusRequester: FocusRequester? = null,
    onNext: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(2)
            )
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(title, style = MaterialTheme.typography.labelSmall)

        Spacer(Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .width(64.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = fieldValue,
                onValueChange = { new ->
                    val old = fieldValue.text
                    val newText = new.text

                    val updated = when {
                        newText.length > old.length -> {
                            val digit = newText.lastOrNull { it.isDigit() }
                            if (digit != null) {
                                "${old[1]}$digit"
                            } else old
                        }

                        // ⌫ BACKSPACE
                        newText.length < old.length -> {
                            when {
                                old[0] != '0' -> "0${old[1]}"
                                old[1] != '0' -> "00"
                                else -> old
                            }
                        }

                        else -> old
                    }

                    fieldValue = TextFieldValue(
                        text = updated,
                        selection = TextRange(2)
                    )
                    onValueChange(updated)
                },
                modifier = Modifier.then(
                    if (focusRequester != null)
                        Modifier.focusRequester(focusRequester)
                    else Modifier
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onNext = { onNext?.invoke() }
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true
            )
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DaysOfWeekSelector(
    selectedDays: Set<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        DayOfWeek.entries.forEach { day ->
            val isSelected = selectedDays.contains(day)

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected)
                    MaterialTheme.colorScheme.secondary
                else
                    Color.Transparent,
                border = if (!isSelected)
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    )
                else null,
                modifier = Modifier
                    .defaultMinSize(minWidth = 44.dp)
                    .clickable { onDayToggle(day) }
            ) {
                Text(
                    text = day.getDisplayName(
                        TextStyle.FULL,
                        Locale("pt", "BR")
                    ),
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.background
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
fun ManualStatusSection(
    manualStatus: ManualStatus,
    onStatusChange: (ManualStatus) -> Unit
) {
    Column {
        Text(
            "Defina o status",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = manualStatus == ManualStatus.OPEN,
                onClick = { onStatusChange(ManualStatus.OPEN) }
            )
            Text(
                text = "Aberta",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = manualStatus == ManualStatus.CLOSED,
                onClick = { onStatusChange(ManualStatus.CLOSED) }
            )
            Text(
                text = "Fechada",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Composable
fun ModeSelector(
    selectedMode: ShopStatusMode,
    onModeChange: (ShopStatusMode) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = selectedMode == ShopStatusMode.MANUAL,
            onClick = { onModeChange(ShopStatusMode.MANUAL) },
            shape = SegmentedButtonDefaults.itemShape(0, 2)
        ) { Text("Manual") }

        SegmentedButton(
            selected = selectedMode == ShopStatusMode.AUTOMATIC,
            onClick = { onModeChange(ShopStatusMode.AUTOMATIC) },
            shape = SegmentedButtonDefaults.itemShape(1, 2)
        ) { Text("Automático") }
    }
}
