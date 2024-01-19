package simbirsoft.task.dailyplanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import simbirsoft.task.dailyplanner.domain.model.DailyPlans
import simbirsoft.task.dailyplanner.domain.model.DailyPlansTime
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import java.time.LocalDateTime

@Dao
interface DailyPlansDao {

    @Query(
        """
        SELECT plans.id, time.time_start, time.time_end, plans.name, plans.description
        FROM daily_plans plans JOIN daily_plans_time time ON plans.id = time.daily_plan_id
        WHERE time.time_start BETWEEN :startDate AND :endDate
        ORDER BY time.time_start
    """
    )
    fun getPlans(
        startDate: Long,
        endDate: Long
    ): Flow<List<LocalPlanModel>>

    @Query("DELETE FROM daily_plans WHERE id = :planId")
    suspend fun deletePlan(planId: Long): Int

    @Insert(
        entity = DailyPlans::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertInfoAboutPlan(dailyPlans: DailyPlans): Long

    @Insert(
        entity = DailyPlansTime::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertTimePlan(dailyPlansTime: DailyPlansTime)

    @Transaction
    suspend fun insertPlan(
        dailyPlans: DailyPlans,
        timeStart: LocalDateTime,
        timeEnd: LocalDateTime
    ) {
        val dailyPlanId = insertInfoAboutPlan(dailyPlans)
        val dailyPlansTime = DailyPlansTime(
            dailyPlanId = dailyPlanId,
            timeStart = timeStart,
            timeEnd = timeEnd
        )
        insertTimePlan(dailyPlansTime)
    }
}