package simbirsoft.task.dailyplanner.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import simbirsoft.task.dailyplanner.MockingData
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.BaseTest
import simbirsoft.task.dailyplanner.common.extensions.toLong
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.presentation.model.event.PlanDetailEvent
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import java.time.LocalDateTime

class PlanDetailViewModelTest : BaseTest() {

    private val navigator: Navigator = mockk(relaxUnitFun = true)
    private val repository: DailyPlannerRepository = mockk(relaxUnitFun = true) {
        coEvery { deletePlan(any()) } returns true
    }

    private fun initViewModel(): PlanDetailViewModel {
        return PlanDetailViewModel(
            navigator = navigator,
            repository = repository,
            planDetails = MockingData.getPlans().first(),
            selectedDate = LocalDateTime.now().minusDays(1).toLong()
        )
    }

    @Test
    fun `when back pressed - navigate back`() {
        // When
        val viewModel = initViewModel()
        viewModel.proceed(PlanDetailEvent.OnBackPressed)

        // Then
        verify { navigator.back() }
    }

    @Test
    fun `when delete plan - success`() {
        // When
        val viewModel = initViewModel()
        viewModel.proceed(PlanDetailEvent.OnDeletePressed)

        // Then
        verify { navigator.showSuccessSnackBar(R.string.successDeletePlanText) }
        verify { navigator.back() }
    }

    @Test
    fun `when save plan - success`() {
        // Given
        val plan = MockingData.getPlans().first()

        // When
        val viewModel = initViewModel()
        viewModel.proceed(
            PlanDetailEvent.OnSavePressed(
                name = plan.name,
                description = plan.description,
                startDateTime = plan.startDateTime,
                endDateTime = plan.endDateTime
            )
        )

        // Then
        verify { navigator.showSuccessSnackBar(R.string.successSavePlanText) }
        verify { navigator.back() }
    }
}