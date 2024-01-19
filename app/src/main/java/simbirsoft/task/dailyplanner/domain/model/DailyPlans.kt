package simbirsoft.task.dailyplanner.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_plans")
data class DailyPlans(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(
        name = "id",
        index = true
    )
    val id: Long?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,
)