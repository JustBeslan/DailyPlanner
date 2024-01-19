package simbirsoft.task.dailyplanner.common.extensions

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")

val UTC_ZONE: ZoneId = ZoneId.of("UTC")
val SYSTEM_ZONE: ZoneId = ZoneId.systemDefault()

fun Long.toLocalDateTime(zoneId: ZoneId = SYSTEM_ZONE): LocalDateTime =
    LocalDateTime.ofInstant(Date(this).toInstant(), zoneId)

fun LocalDateTime.toLong(zoneId: ZoneId = SYSTEM_ZONE): Long =
    atZone(zoneId).toInstant().toEpochMilli()

fun LocalDateTime?.toFormatDate(zoneId: ZoneId = SYSTEM_ZONE): String {
    val currentDateTime = this ?: LocalDateTime.now(zoneId)
    return currentDateTime.format(DATE_FORMATTER)
}

fun LocalDateTime?.toFormatTime(zoneId: ZoneId = SYSTEM_ZONE): String {
    val currentDateTime = this ?: LocalDateTime.now(zoneId)
    return currentDateTime.format(TIME_FORMATTER)
}

fun LocalTime?.toFormatTime(zoneId: ZoneId = SYSTEM_ZONE): String {
    val currentDateTime = this ?: LocalTime.now(zoneId)
    return currentDateTime.format(TIME_FORMATTER)
}

fun LocalDateTime.setTime(hour: Int, minute: Int): LocalDateTime {
    return this.withHour(hour).withMinute(minute)
}

fun LocalDateTime.timeIsBetween(
    start: LocalTime,
    end: LocalTime,
    zoneId: ZoneId = SYSTEM_ZONE
): Boolean {
    val localTime = atZone(zoneId).toLocalTime()
    return (localTime == start || localTime.isAfter(start)) && localTime.isBefore(end)
}