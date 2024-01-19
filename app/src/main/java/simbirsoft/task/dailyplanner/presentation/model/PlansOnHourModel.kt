package simbirsoft.task.dailyplanner.presentation.model

import java.time.LocalTime

data class PlansOnHourModel(
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val plans: List<PlanModel> = emptyList()
)