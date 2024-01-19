package simbirsoft.task.dailyplanner.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class LocalPlanModel(
    val id: Long? = null,
    @ColumnInfo(name = "time_start") val startDateTime: LocalDateTime,
    @ColumnInfo(name = "time_end") val endDateTime: LocalDateTime,
    val name: String,
    val description: String,
) : Parcelable