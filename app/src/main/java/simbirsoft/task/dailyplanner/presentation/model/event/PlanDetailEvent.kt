package simbirsoft.task.dailyplanner.presentation.model.event

import simbirsoft.task.dailyplanner.common.model.BaseEvent
import java.time.LocalDateTime

sealed interface PlanDetailEvent : BaseEvent {
    data object OnBackPressed : PlanDetailEvent
    data object OnDeletePressed : PlanDetailEvent
    data class OnSavePressed(
        val name: String,
        val description: String,
        val startDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
    ) : PlanDetailEvent
}