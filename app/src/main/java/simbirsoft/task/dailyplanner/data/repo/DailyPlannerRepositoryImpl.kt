package simbirsoft.task.dailyplanner.data.repo

import kotlinx.coroutines.flow.Flow
import simbirsoft.task.dailyplanner.data.local.DailyPlansDao
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.domain.model.DailyPlans
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.common.extensions.UTC_ZONE
import simbirsoft.task.dailyplanner.common.extensions.toLocalDateTime
import simbirsoft.task.dailyplanner.common.extensions.toLong
import javax.inject.Inject

class DailyPlannerRepositoryImpl @Inject constructor(
    private val dailyPlansDao: DailyPlansDao,
) : DailyPlannerRepository {
    override fun getPlans(startDate: Long): Flow<List<LocalPlanModel>> {
        val endDate = startDate.toLocalDateTime(UTC_ZONE).plusHours(24).toLong(UTC_ZONE)
        return dailyPlansDao.getPlans(
            startDate = startDate,
            endDate = endDate
        )
    }

    override suspend fun insertPlan(localPlanModel: LocalPlanModel) {
        val plan = DailyPlans(
            id = localPlanModel.id,
            name = localPlanModel.name,
            description = localPlanModel.description
        )
        dailyPlansDao.insertPlan(
            dailyPlans = plan,
            timeStart = localPlanModel.startDateTime,
            timeEnd = localPlanModel.endDateTime
        )
    }

    override suspend fun deletePlan(planId: Long): Boolean {
        return dailyPlansDao.deletePlan(planId) == 1
    }
}