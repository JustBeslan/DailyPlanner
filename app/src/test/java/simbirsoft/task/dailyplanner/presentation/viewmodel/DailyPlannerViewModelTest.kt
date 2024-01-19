package simbirsoft.task.dailyplanner.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import simbirsoft.task.dailyplanner.MockingData
import simbirsoft.task.dailyplanner.common.BaseTest
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.presentation.mapper.DailyPlanMapper
import simbirsoft.task.dailyplanner.presentation.model.event.DailyPlannerEvent
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator

class DailyPlannerViewModelTest : BaseTest() {

    private val navigator: Navigator = mockk(relaxUnitFun = true)
    private val mapper: DailyPlanMapper = mockk()
    private val repository: DailyPlannerRepository = mockk {
        coEvery { getPlans(any()) } returns flowOf(MockingData.getPlans())
    }

    private fun initViewModel(): DailyPlannerViewModel {
        return DailyPlannerViewModel(
            dailyPlanMapper = mapper,
            navigator = navigator,
            repository = repository
        )
    }

    @Test
    fun `when add plan button pressed - open create plan screen`() = runTest {
        // Given
        val viewModel = initViewModel()

        // When
        viewModel.proceed(DailyPlannerEvent.AddPlanButtonClicked)

        // Then
        verify { navigator.openPlanDetailScreen(any(), any()) }
    }

    @Test
    fun `when plan pressed - open plan details screen`() = runTest {
        // Given
        val viewModel = initViewModel()
        val plans = MockingData.getPlans()
        val selectedPlanId = requireNotNull(plans.first().id)

        // When
        viewModel.proceed(DailyPlannerEvent.PlanClicked(selectedPlanId))

        // Then
        verify { navigator.openPlanDetailScreen(any(), any()) }
    }
}