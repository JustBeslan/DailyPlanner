package simbirsoft.task.dailyplanner.presentation.ui.compose

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.extensions.setTime
import simbirsoft.task.dailyplanner.common.extensions.toFormatDate
import simbirsoft.task.dailyplanner.common.extensions.toFormatTime
import simbirsoft.task.dailyplanner.common.ui.ThemePreviewParameter
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme
import simbirsoft.task.dailyplanner.common.ui.theme.Green
import simbirsoft.task.dailyplanner.common.ui.theme.Red
import simbirsoft.task.dailyplanner.common.ui.theme.White
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.presentation.model.event.PlanDetailEvent
import simbirsoft.task.dailyplanner.presentation.model.state.PlanDetailState
import simbirsoft.task.dailyplanner.presentation.ui.compose.component.DailyPlannerButton
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailScreen(
    state: PlanDetailState,
    onEvent: (PlanDetailEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable { onEvent.invoke(PlanDetailEvent.OnBackPressed) },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { paddings ->
        PlanDetailContent(
            name = state.planDetails?.name.orEmpty(),
            description = state.planDetails?.description.orEmpty(),
            startDateTime = state.planDetails?.startDateTime ?: state.selectedDateTime,
            endDateTime = state.planDetails?.endDateTime ?: state.selectedDateTime,
            readOnly = state.planDetails != null,
            onEvent = onEvent,
            paddingValues = paddings
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PlanDetailContent(
    name: String,
    description: String,
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    readOnly: Boolean,
    onEvent: (PlanDetailEvent) -> Unit,
    paddingValues: PaddingValues,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    val currentName = remember { mutableStateOf(name) }
    val currentDescription = remember { mutableStateOf(description) }

    val currentStartDateTime = remember { mutableStateOf(startDateTime) }
    val currentEndDateTime = remember { mutableStateOf(endDateTime) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
    ) {
        PlanButtons(
            readOnly = readOnly,
            onSavePlan = {
                keyboard?.hide()
                onEvent.invoke(
                    PlanDetailEvent.OnSavePressed(
                        name = currentName.value,
                        description = currentDescription.value,
                        startDateTime = currentStartDateTime.value,
                        endDateTime = currentEndDateTime.value,
                    )
                )
            },
            onDeletePlan = {
                onEvent.invoke(PlanDetailEvent.OnDeletePressed)
            }
        )
        PlanDetailsOutlineTextField(
            onValueChanged = { currentName.value = it },
            labelResId = R.string.name,
            readOnly = readOnly,
            value = currentName.value,
            trailingIcon = Icons.Filled.Clear,
            trailingListener = { currentName.value = "" },
        )
        PlanTime(
            startDateTime = currentStartDateTime.value,
            endDateTime = currentEndDateTime.value,
            readOnly = readOnly,
            onTimeChanged = { hour, minute, isStartTime ->
                if (isStartTime) {
                    currentStartDateTime.value =
                        currentStartDateTime.value.setTime(hour, minute)
                } else {
                    currentEndDateTime.value =
                        currentEndDateTime.value.setTime(hour, minute)
                }
            },
        )
        PlanDetailsOutlineTextField(
            onValueChanged = { currentDescription.value = it },
            labelResId = R.string.description,
            readOnly = readOnly,
            value = currentDescription.value,
            countVisibleLines = 5
        )
    }
}

@Composable
private fun PlanDetailsOutlineTextField(
    onValueChanged: (String) -> Unit,
    @StringRes labelResId: Int,
    value: String? = null,
    readOnly: Boolean,
    trailingIcon: ImageVector? = null,
    trailingListener: () -> Unit = {},
    countVisibleLines: Int = 1,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        value = value.orEmpty(),
        onValueChange = onValueChanged,
        singleLine = countVisibleLines == 1,
        enabled = readOnly.not(),
        minLines = countVisibleLines,
        maxLines = countVisibleLines,
        label = { Text(text = stringResource(labelResId)) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            if (value.isNullOrEmpty().not() && trailingIcon != null && readOnly.not()) {
                Icon(
                    modifier = Modifier
                        .clickable { trailingListener.invoke() },
                    imageVector = trailingIcon,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
private fun PlanTime(
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    readOnly: Boolean,
    onTimeChanged: (Int, Int, Boolean) -> Unit,
) {
    var isStartTimeEditing by remember { mutableStateOf<Boolean?>(null) }
    var showedTimePicker by remember { mutableStateOf(false) }
    var initTimePickerTime by remember { mutableStateOf(LocalTime.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DateTimePlan(
                modifier = Modifier
                    .padding(10.dp),
                date = startDateTime.toFormatDate(),
                time = startDateTime.toFormatTime(),
                readOnly = readOnly,
                onClickTime = {
                    if (showedTimePicker.not()) {
                        initTimePickerTime = startDateTime.toLocalTime()
                        isStartTimeEditing = true
                        showedTimePicker = true
                    }
                },
                containerTimeColor = if (isStartTimeEditing == true) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                },
                contentTimeColor = if (isStartTimeEditing == true) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
            )
            DateTimePlan(
                modifier = Modifier
                    .padding(10.dp),
                date = endDateTime.toFormatDate(),
                time = endDateTime.toFormatTime(),
                readOnly = readOnly,
                onClickTime = {
                    if (showedTimePicker.not()) {
                        initTimePickerTime = endDateTime.toLocalTime()
                        isStartTimeEditing = false
                        showedTimePicker = true
                    }
                },
                containerTimeColor = if (isStartTimeEditing == false) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                },
                contentTimeColor = if (isStartTimeEditing == false) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .padding(10.dp),
            visible = showedTimePicker
        ) {
            PlanTimeEditor(
                initTimePickerTime = initTimePickerTime,
                onSaveTimeClick = { hour, minute ->
                    showedTimePicker = false
                    onTimeChanged.invoke(hour, minute, requireNotNull(isStartTimeEditing))
                    isStartTimeEditing = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlanTimeEditor(
    initTimePickerTime: LocalTime,
    onSaveTimeClick: (Int, Int) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initTimePickerTime.hour,
        initialMinute = initTimePickerTime.minute,
        is24Hour = true
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        TimePicker(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                clockDialColor = MaterialTheme.colorScheme.primary,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.primary,
                clockDialUnselectedContentColor = MaterialTheme.colorScheme.background,
                selectorColor = MaterialTheme.colorScheme.background,
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.background,
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.primary,
            )
        )
        DailyPlannerButton(
            modifier = Modifier
                .align(Alignment.End),
            onClick = {
                onSaveTimeClick.invoke(timePickerState.hour, timePickerState.minute)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            buttonTextResId = R.string.save,
            imageVector = Icons.Filled.Check,
        )
    }
}

@Composable
private fun PlanButtons(
    readOnly: Boolean,
    onSavePlan: () -> Unit,
    onDeletePlan: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd),
        ) {
            DailyPlannerButton(
                onClick = { if (readOnly) onDeletePlan.invoke() else onSavePlan.invoke() },
                containerColor = if (readOnly) Red else Green,
                contentColor = White,
                buttonTextResId = if (readOnly) R.string.delete else R.string.save,
                imageVector = if (readOnly) Icons.Outlined.Delete else Icons.Filled.Check,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DateTimePlan(
    date: String,
    time: String,
    readOnly: Boolean,
    onClickTime: () -> Unit,
    modifier: Modifier = Modifier,
    contentTimeColor: Color,
    containerTimeColor: Color,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = date
        )
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(10.dp))
                .background(containerTimeColor)
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ),
            enabled = readOnly.not(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerTimeColor,
                contentColor = contentTimeColor
            ),
            onClick = {
                keyboard?.hide()
                onClickTime.invoke()
            }
        ) {
            Text(text = time)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanDetailScreenPreview(
    @PreviewParameter(ThemePreviewParameter::class) isDark: Boolean,
) = DailyPlannerTheme(isDark) {
    PlanDetailScreen(
        state = PlanDetailState(
            selectedDateTime = LocalDateTime.now(),
            planDetails = LocalPlanModel(
                startDateTime = LocalDateTime.now().minusHours(5),
                endDateTime = LocalDateTime.now().plusHours(3),
                name = "task",
                description = "description task"
            )
        ),
        onEvent = {},
    )
}