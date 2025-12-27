package com.example.produtosdelimpeza.compose.seller.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import com.example.produtosdelimpeza.R


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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopStatusComponent(){
    val isOnline = true
    var showSheet by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { showSheet = true },
        label = { Text(if (isOnline) "Aberto" else "Fechado", fontSize = 12.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.size(12.dp)
            )
        },
        border = null,
        elevation = AssistChipDefaults.assistChipElevation(
            elevation = 4.dp,
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.secondary
        )
    )

    if (showSheet) {
        StoreStatusBottomSheet(
            onDismiss = { showSheet = false }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreStatusBottomSheet(
    onDismiss: () -> Unit
) {
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


@Composable
fun WorkingRuleForm(
    rule: WorkingRule,
    onRuleChange: (WorkingRule) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─────────────────────────────
            // Dias da semana
            // ─────────────────────────────
            DaysOfWeekSelector(
                selectedDays = rule.days,
                onDayToggle = { day ->
                    val updatedDays =
                        if (rule.days.contains(day)) {
                            rule.days - day
                        } else {
                            rule.days + day
                        }

                    onRuleChange(
                        rule.copy(days = updatedDays)
                    )
                }
            )

            // ─────────────────────────────
            // Horários
            // ─────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = rule.openTime,
                    onValueChange = { newValue ->
                        onRuleChange(
                            rule.copy(openTime = newValue)
                        )
                    },
                    label = {
                        Text(
                            text = "Abertura",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                OutlinedTextField(
                    value = rule.closeTime,
                    onValueChange = { newValue ->
                        onRuleChange(
                            rule.copy(closeTime = newValue)
                        )
                    },
                    label = {
                        Text(
                            text = "Fechamento",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
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