package simbirsoft.task.dailyplanner.presentation.model.state

import simbirsoft.task.dailyplanner.common.extensions.UTC_ZONE
import simbirsoft.task.dailyplanner.common.extensions.toLong
import simbirsoft.task.dailyplanner.common.model.BaseState
import simbirsoft.task.dailyplanner.common.model.ImmutableList
import simbirsoft.task.dailyplanner.presentation.model.PlansOnHourModel
import java.time.LocalDateTime
import java.time.LocalTime

data class DailyPlannerState(
    val selectedDate: Long,
    val plans: ImmutableList<PlansOnHourModel>,
) : BaseState {
    companion object {
        fun provideInitialState(): DailyPlannerState = DailyPlannerState(
            selectedDate = LocalDateTime.now().with(LocalTime.MIN).toLong(UTC_ZONE),
            plans = ImmutableList(emptyList())
        )
    }
}