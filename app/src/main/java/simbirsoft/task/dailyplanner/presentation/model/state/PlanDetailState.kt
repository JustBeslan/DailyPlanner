package simbirsoft.task.dailyplanner.presentation.model.state

import simbirsoft.task.dailyplanner.common.model.BaseState
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import java.time.LocalDateTime

data class PlanDetailState(
    val planDetails: LocalPlanModel?,
    val selectedDateTime: LocalDateTime,
) : BaseState
