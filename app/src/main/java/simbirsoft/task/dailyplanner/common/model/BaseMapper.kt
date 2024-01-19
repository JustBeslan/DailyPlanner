package simbirsoft.task.dailyplanner.common.model

interface BaseMapper<in FROM, out TO> {
    fun map(from: FROM): TO
}