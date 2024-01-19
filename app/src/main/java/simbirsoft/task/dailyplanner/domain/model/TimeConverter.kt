package simbirsoft.task.dailyplanner.domain.model

import androidx.room.TypeConverter
import simbirsoft.task.dailyplanner.common.extensions.UTC_ZONE
import simbirsoft.task.dailyplanner.common.extensions.toLocalDateTime
import simbirsoft.task.dailyplanner.common.extensions.toLong
import java.time.LocalDateTime

class TimeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime) = localDateTime.toLong(UTC_ZONE)

    @TypeConverter
    fun toLocalDateTime(time: Long) = time.toLocalDateTime(UTC_ZONE)
}