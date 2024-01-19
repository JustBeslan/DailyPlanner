package simbirsoft.task.dailyplanner.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "daily_plans_time",
    foreignKeys = [
        ForeignKey(
            entity = DailyPlans::class,
            parentColumns = ["id"],
            childColumns = ["daily_plan_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class DailyPlansTime(
    @PrimaryKey
    @ColumnInfo(
        name = "daily_plan_id",
        index = true
    )
    val dailyPlanId: Long,

    @ColumnInfo(name = "time_start")
    val timeStart: LocalDateTime,

    @ColumnInfo(name = "time_end")
    val timeEnd: LocalDateTime,
)
