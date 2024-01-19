package simbirsoft.task.dailyplanner.data.repo

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import simbirsoft.task.dailyplanner.MockingData
import simbirsoft.task.dailyplanner.common.BaseTest
import simbirsoft.task.dailyplanner.common.extensions.toLong
import simbirsoft.task.dailyplanner.data.local.DailyPlansDao
import java.time.LocalDateTime
import java.time.LocalTime

class DailyPlannerRepositoryImplTest : BaseTest() {

    private val dao: DailyPlansDao = mockk(relaxUnitFun = true) {
        every { getPlans(any(), any()) } returns flowOf(MockingData.getPlans())
        coEvery { deletePlan(any()) } returns 1
    }
    private val repository = DailyPlannerRepositoryImpl(
        dailyPlansDao = dao
    )

    @Test
    fun `when getPlans - success`() = runTest {
        // Given
        val startDate = LocalDateTime.now().with(LocalTime.MIN).toLong()

        // When
        val plans = repository.getPlans(startDate).first()
        val expectedPlans = MockingData.getPlans()

        // Then
        assertEquals(plans, expectedPlans)
    }

    @Test
    fun `when insertPlan - success`() = runTest {
        // Given
        val plan = MockingData.getPlans().first()

        // When
        val res = repository.insertPlan(plan)

        // Then
        assertEquals(res, Unit)
    }

    @Test
    fun `when deletePlan - success`() = runTest {
        // Given
        val planId = 0L

        // When
        val isDelete = repository.deletePlan(planId)

        // Then
        assertTrue(isDelete)
    }
}