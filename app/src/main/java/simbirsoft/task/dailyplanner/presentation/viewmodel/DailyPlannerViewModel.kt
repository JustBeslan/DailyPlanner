package simbirsoft.task.dailyplanner.presentation.viewmodel

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.extensions.timeIsBetween
import simbirsoft.task.dailyplanner.common.model.ImmutableList
import simbirsoft.task.dailyplanner.common.ui.BaseViewModel
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.presentation.mapper.DailyPlanMapper
import simbirsoft.task.dailyplanner.presentation.model.PlansOnHourModel
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

    private var requestPlansJob: Job? = null
    private var plansForSelectedDay: List<LocalPlanModel> = emptyList()

    init {
        requestPlans()
    }

    override fun proceed(event: DailyPlannerEvent) {
        when (event) {
            DailyPlannerEvent.AddPlanButtonClicked -> handleAddPlanPressed()
            is DailyPlannerEvent.SelectDate -> handleSelectDate(event.selectedDateInMillis)
            is DailyPlannerEvent.PlanClicked -> handleClickPlan(event.id)
        }
    }

    private fun handleAddPlanPressed() {
        cancelJob()
        navigator.openPlanDetailScreen(
            selectedDate = requiredState().selectedDate
        )
    }

    private fun handleClickPlan(id: Long) {
        cancelJob()
        plansForSelectedDay.firstOrNull { it.id == id }?.let {
            navigator.openPlanDetailScreen(plan = it)
        }
    }

    private fun handleSelectDate(selectedDate: Long) {
        cancelJob()
        updateState { copy(selectedDate = selectedDate) }
        requestPlans()
    }

    private fun requestPlans() {
        requestPlansJob = launch(
            exceptionHandler = CoroutineExceptionHandler { _, _ ->
                navigator.showErrorSnackBar(R.string.defaultErrorText)
            },
            coroutine = {
                plansForSelectedDay = repository.getPlans(requiredState().selectedDate).first()
                postProcessingPlans()
            }
        )
    }

    private fun postProcessingPlans() {
        val newPlansOnDay = mutableListOf<PlansOnHourModel>()
        repeat(HOURS_OF_DAY) { hour ->
            val timeStart = LocalTime.of(hour, 0)
            val timeEnd = LocalTime.of(if (hour == HOURS_OF_DAY - 1) 0 else hour + 1, 0)
            val plansOnHour = plansForSelectedDay
                .filter {
                    it.startDateTime.timeIsBetween(
                        start = timeStart,
                        end = timeEnd,
                    )
                }
                .map { dailyPlanMapper.map(it) }
            newPlansOnDay.add(
                PlansOnHourModel(
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    plans = plansOnHour
                )
            )
        }
        updateState { copy(plans = ImmutableList(newPlansOnDay)) }
    }

    private fun cancelJob() {
        requestPlansJob?.let { job ->
            if (job.isCompleted.not() && job.isActive) {
                job.cancel()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): DailyPlannerViewModel
    }
}