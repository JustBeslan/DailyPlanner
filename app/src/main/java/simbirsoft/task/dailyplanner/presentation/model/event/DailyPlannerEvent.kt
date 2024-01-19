package simbirsoft.task.dailyplanner.presentation.model.event

import simbirsoft.task.dailyplanner.common.model.BaseEvent

sealed interface DailyPlannerEvent : BaseEvent {
    data class SelectDate(val selectedDateInMillis: Long) : DailyPlannerEvent
    data object AddPlanButtonClicked : DailyPlannerEvent
    data class PlanClicked(val id: Long) : DailyPlannerEvent
}