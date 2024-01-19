package simbirsoft.task.dailyplanner.presentation.model

data class PlanModel(
    val id: Long? = null,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
)