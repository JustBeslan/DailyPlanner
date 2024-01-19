package simbirsoft.task.dailyplanner.presentation.viewmodel

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import simbirsoft.task.dailyplanner.common.extensions.timeIsBetween
import simbirsoft.task.dailyplanner.common.model.ImmutableList
import simbirsoft.task.dailyplanner.common.ui.BaseViewModel
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.presentation.mapper.DailyPlanMapper
import simbirsoft.task.dailyplanner.presentation.model.PlansForHourModel
import simbirsoft.task.dailyplanner.presentation.model.event.DailyPlannerEvent
import simbirsoft.task.dailyplanner.presentation.model.state.DailyPlannerState
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import java.time.LocalTime

class DailyPlannerViewModel @AssistedInject constructor(
    private val dailyPlanMapper: DailyPlanMapper,
    private val navigator: Navigator,
    private val repository: DailyPlannerRepository,
) : BaseViewModel<DailyPlannerState, DailyPlannerEvent>(
    initState = DailyPlannerState.provideInitialState()
) {
    private companion object {
        private const val HOURS_OF_DAY = 24
    }

    init {
        requestPlans()
    }

    private var plansForSelectedDay: List<LocalPlanModel> = emptyList()

    private val plansForDayTemplate: MutableList<PlansForHourModel> = List(HOURS_OF_DAY) { hour ->
        val timeStart = LocalTime.of(hour, 0)
        val timeEnd = LocalTime.of(if (hour == HOURS_OF_DAY - 1) 0 else hour + 1, 0)
        PlansForHourModel(
            timeStart = timeStart,
            timeEnd = timeEnd
        )
    }.toMutableList()

    override fun proceed(event: DailyPlannerEvent) {
        when (event) {
            DailyPlannerEvent.AddPlanButtonClicked -> {
                navigator.openPlanDetailScreen(selectedDate = requiredState().selectedDate)
            }

            is DailyPlannerEvent.SelectDate -> handleSelectDate(event.selectedDateInMillis)
            is DailyPlannerEvent.PlanClicked -> handleClickPlan(event.id)
        }
    }

    private fun handleClickPlan(id: Long) {
        plansForSelectedDay.firstOrNull { it.id == id }?.let {
            navigator.openPlanDetailScreen(plan = it)
        }
    }

    private fun handleSelectDate(selectedDate: Long) {
        updateState { copy(selectedDate = selectedDate) }
        requestPlans()
    }

    private fun requestPlans() {
        launch {
            repository.getPlans(requiredState().selectedDate).collect {
                plansForSelectedDay = it
                postProcessingPlans()
            }
        }
    }

    private fun postProcessingPlans() {
        val newPlansForDay = plansForDayTemplate
        repeat(newPlansForDay.size) { index ->
            val hourModel = plansForDayTemplate[index]
            val newPlans = plansForSelectedDay
                .filter {
                    it.startDateTime.timeIsBetween(
                        start = hourModel.timeStart,
                        end = hourModel.timeEnd,
                    )
                }
                .map { dailyPlanMapper.map(it) }
            newPlansForDay[index] = newPlansForDay[index].copy(plans = newPlans)
        }
        updateState { copy(plans = ImmutableList(newPlansForDay)) }
    }

    @AssistedFactory
    interface Factory {
        fun create(): DailyPlannerViewModel
    }
}