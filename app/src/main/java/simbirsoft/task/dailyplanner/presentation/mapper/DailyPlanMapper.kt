package simbirsoft.task.dailyplanner.presentation.mapper

import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.common.extensions.toFormatTime
import simbirsoft.task.dailyplanner.common.model.BaseMapper
import simbirsoft.task.dailyplanner.presentation.model.PlanModel
import javax.inject.Inject

class DailyPlanMapper @Inject constructor() : BaseMapper<LocalPlanModel, PlanModel> {

    override fun map(from: LocalPlanModel): PlanModel {
        return PlanModel(
            id = from.id,
            name = from.name,
            timeStart = from.startDateTime.toFormatTime(),
            timeEnd = from.endDateTime.toFormatTime(),
        )
    }
}