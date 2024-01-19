package simbirsoft.task.dailyplanner

import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object MockingData {

    fun getPlans(): List<LocalPlanModel> {
        return listOf(
            LocalPlanModel(
                id = 0,
                startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)),
                endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 20)),
                name = "name0",
                description = "desc0"
            ),
            LocalPlanModel(
                id = 1,
                startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 15)),
                endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 40)),
                name = "name1",
                description = "desc1"
            ),
            LocalPlanModel(
                id = 2,
                startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 17)),
                endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 30)),
                name = "name2",
                description = "desc2"
            ),
        )
    }
}