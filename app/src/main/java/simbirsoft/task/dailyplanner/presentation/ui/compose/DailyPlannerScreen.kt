package simbirsoft.task.dailyplanner.presentation.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.extensions.toFormatTime
import simbirsoft.task.dailyplanner.common.model.ImmutableList
import simbirsoft.task.dailyplanner.common.ui.ThemePreviewParameter
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme
import simbirsoft.task.dailyplanner.presentation.model.PlanModel
import simbirsoft.task.dailyplanner.presentation.model.PlansForHourModel
import simbirsoft.task.dailyplanner.presentation.model.event.DailyPlannerEvent
import simbirsoft.task.dailyplanner.presentation.model.state.DailyPlannerState
import simbirsoft.task.dailyplanner.presentation.ui.compose.component.DailyPlannerButton
import java.time.LocalTime

@Composable
fun DailyPlannerScreen(
    state: DailyPlannerState,
    onEvent: (DailyPlannerEvent) -> Unit,
) {
    var showedAddPlanButton by remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            AddPlanButton(
                showedAddPlanButton = showedAddPlanButton,
                onAddPlan = { onEvent.invoke(DailyPlannerEvent.AddPlanButtonClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            DatePicker(
                selectedDate = state.selectedDate,
                onSelectDate = { onEvent.invoke(DailyPlannerEvent.SelectDate(it)) },
            )
            Box(
                modifier = Modifier
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth(),
            )
            ListPlans(
                plans = state.plans,
                onPlanClick = { onEvent.invoke(DailyPlannerEvent.PlanClicked(it)) },
                onScrollPlans = { showedAddPlanButton = it },
            )
        }
    }
}

@Composable
private fun AddPlanButton(
    showedAddPlanButton: Boolean,
    onAddPlan: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .padding(5.dp),
        visible = showedAddPlanButton,
        enter = scaleIn(
            transformOrigin = TransformOrigin(0.5f, 1.0f)
        ) + fadeIn(),
        exit = scaleOut(
            transformOrigin = TransformOrigin(0.5f, 1.0f)
        ) + fadeOut(),
    ) {
        DailyPlannerButton(
            imageVector = Icons.Filled.Add,
            buttonTextResId = R.string.add,
            onClick = onAddPlan
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    selectedDate: Long,
    onSelectDate: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate,
    )

    DatePicker(
        state = datePickerState,
        title = null,
        headline = null,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            selectedYearContentColor = MaterialTheme.colorScheme.background,
            selectedYearContainerColor = MaterialTheme.colorScheme.primary,
            weekdayContentColor = MaterialTheme.colorScheme.primary,
            dayContentColor = MaterialTheme.colorScheme.primary,
            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
            selectedDayContentColor = MaterialTheme.colorScheme.background
        )
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            onSelectDate.invoke(it)
        }
    }
}

@Composable
private fun ListPlans(
    plans: ImmutableList<PlansForHourModel>,
    onScrollPlans: (Boolean) -> Unit,
    onPlanClick: (Long) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    var previousFirstVisibleItemIndex = 0
    val showedAddPlanButton by remember {
        derivedStateOf {
            val showedButton = lazyListState.firstVisibleItemIndex <= previousFirstVisibleItemIndex
            previousFirstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            showedButton
        }
    }

    LazyColumn(state = lazyListState) {
        itemsIndexed(
            items = plans.list,
            key = { _, item -> item.timeStart }
        ) { index, item ->
            HourBlock(
                timeStart = item.timeStart.toFormatTime(),
                timeEnd = item.timeEnd.toFormatTime(),
                plansForHour = ImmutableList(item.plans),
                onPlanClick = onPlanClick
            )
            if (index != plans.list.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }

    LaunchedEffect(showedAddPlanButton) {
        onScrollPlans.invoke(showedAddPlanButton)
    }

    LaunchedEffect(plans.list.size) {
        val currentTime = LocalTime.now()
        val indexCurrentHour = plans.list.indexOfFirst { currentTime <= it.timeEnd }
        if (indexCurrentHour != -1) {
            lazyListState.animateScrollToItem(indexCurrentHour)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyPlannerScreenPreview(
    @PreviewParameter(ThemePreviewParameter::class) isDark: Boolean,
) = DailyPlannerTheme(isDark) {
    DailyPlannerScreen(
        state = DailyPlannerState.provideInitialState().copy(
            plans = ImmutableList(
                list = listOf(
                    PlansForHourModel(
                        timeStart = LocalTime.of(10, 0),
                        timeEnd = LocalTime.of(11, 0),
                        plans = listOf(
                            PlanModel(
                                id = 1,
                                name = "name 1",
                                timeStart = "10:15",
                                timeEnd = "10:30"
                            ),
                        )
                    ),
                    PlansForHourModel(
                        timeStart = LocalTime.of(11, 0),
                        timeEnd = LocalTime.of(12, 0),
                        plans = listOf(
                            PlanModel(
                                id = 2,
                                name = "name 1",
                                timeStart = "11:15",
                                timeEnd = "11:30"
                            ),
                            PlanModel(
                                id = 3,
                                name = "name 2",
                                timeStart = "11:35",
                                timeEnd = "11:40"
                            ),
                        )
                    ),
                )
            )
        ),
        onEvent = {}
    )
}