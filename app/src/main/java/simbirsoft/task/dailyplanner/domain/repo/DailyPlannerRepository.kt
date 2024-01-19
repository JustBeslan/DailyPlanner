package simbirsoft.task.dailyplanner.domain.repo

import kotlinx.coroutines.flow.Flow
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel

interface DailyPlannerRepository {
    fun getPlans(startDate: Long): Flow<List<LocalPlanModel>>
    suspend fun insertPlan(localPlanModel: LocalPlanModel)
    suspend fun deletePlan(planId: Long): Boolean
}