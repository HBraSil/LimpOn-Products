package com.example.produtosdelimpeza.store.onboarding.presentation.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.domain.model.DayOfWeek

@Composable
fun DayOfWeek.displayName(): String {
    return when (this) {
        DayOfWeek.MONDAY -> stringResource(R.string.monday)
        DayOfWeek.TUESDAY -> stringResource(R.string.tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(R.string.wednesday)
        DayOfWeek.THURSDAY -> stringResource(R.string.thursday)
        DayOfWeek.FRIDAY -> stringResource(R.string.friday)
        DayOfWeek.SATURDAY -> stringResource(R.string.saturday)
        DayOfWeek.SUNDAY -> stringResource(R.string.sunday)
    }
}