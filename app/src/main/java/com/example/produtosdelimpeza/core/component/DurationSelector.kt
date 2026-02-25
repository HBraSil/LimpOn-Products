package com.example.produtosdelimpeza.core.component


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map


@Composable
fun DurationSelector(
    titleSection: String,
    selectedDuration: Int,
    onValidityChange: (Int) -> Unit,
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = titleSection,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )


        DaysPicker(
            selectedDay = selectedDuration,
            onDaySelected = onValidityChange
        )
    }
}

@Composable
fun DaysPicker(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {

    val paddedDays = remember {
        listOf(null) + (1..30).toList() + listOf(null)
    }
    val itemHeight = 50.dp
    val visibleItems = 3

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedDay)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return available
            }
        }
    }

    Box(
        modifier = Modifier
            .height(itemHeight * visibleItems)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            modifier = Modifier.nestedScroll(nestedScrollConnection).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(paddedDays.size) { index ->
                val day = paddedDays[index]

                if (day == null) {
                    Spacer(modifier = Modifier.height(itemHeight))
                } else {
                    val isSelected = day == selectedDay

                    val scale by animateFloatAsState(if (isSelected) 1.2f else 1f)
                    val alpha by animateFloatAsState(if (isSelected) 1f else 0.4f)

                    Text(
                        text = "$day dias",
                        modifier = Modifier
                            .height(itemHeight)
                            .wrapContentHeight(Alignment.CenterVertically)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            }
                    )
                }
            }
        }


        Box(
            modifier = Modifier
                .height(itemHeight)
                .height(itemHeight * 3)
                .fillMaxWidth()
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ){}
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { visibleItems ->
                val layoutInfo = listState.layoutInfo
                val center = layoutInfo.viewportEndOffset / 2

                visibleItems.minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - center)
                }?.index
            }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->
                val day = paddedDays[index]
                if (day != null) {
                    onDaySelected(day)
                }
            }
    }
}