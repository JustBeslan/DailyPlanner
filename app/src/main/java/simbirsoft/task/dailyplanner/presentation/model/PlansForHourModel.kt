package simbirsoft.task.dailyplanner.presentation.model

import java.time.LocalTime

data class PlansForHourModel(
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val plans: List<PlanModel> = emptyList()
)