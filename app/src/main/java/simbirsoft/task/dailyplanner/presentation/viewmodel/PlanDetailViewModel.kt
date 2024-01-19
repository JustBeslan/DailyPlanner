package simbirsoft.task.dailyplanner.presentation.viewmodel

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.extensions.SYSTEM_ZONE
import simbirsoft.task.dailyplanner.common.extensions.toLocalDateTime
import simbirsoft.task.dailyplanner.common.extensions.toLong
import simbirsoft.task.dailyplanner.common.ui.BaseViewModel
import simbirsoft.task.dailyplanner.domain.exceptions.DeletePlanException
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository
import simbirsoft.task.dailyplanner.presentation.model.event.PlanDetailEvent
import simbirsoft.task.dailyplanner.presentation.model.state.PlanDetailState
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import java.time.LocalDateTime

class PlanDetailViewModel @AssistedInject constructor(
    private val navigator: Navigator,
    private val repository: DailyPlannerRepository,
    @Assisted planDetails: LocalPlanModel?,
    @Assisted selectedDate: Long?,
) : BaseViewModel<PlanDetailState, PlanDetailEvent>(
    initState = PlanDetailState(
        planDetails = planDetails,
        selectedDateTime = selectedDate?.toLocalDateTime() ?: LocalDateTime.now(SYSTEM_ZONE)
    )
) {
    private val defaultExceptionHandler = CoroutineExceptionHandler { _, _ ->
        navigator.showErrorSnackBar(R.string.defaultErrorText)
    }

    override fun proceed(event: PlanDetailEvent) {
        when (event) {
            PlanDetailEvent.OnBackPressed -> navigator.back()
            PlanDetailEvent.OnDeletePressed -> handleDeletePlan()
            is PlanDetailEvent.OnSavePressed -> {
                handleSaveButtonPressed(
                    localPlanModel = LocalPlanModel(
                        name = event.name,
                        description = event.description,
                        startDateTime = event.startDateTime,
                        endDateTime = event.endDateTime
                    )
                )
            }
        }
    }

    private fun handleDeletePlan() {
        val id = requiredState().planDetails?.id
        launch(exceptionHandler = defaultExceptionHandler) {
            if (repository.deletePlan(planId = requireNotNull(id))) {
                navigator.showSuccessSnackBar(R.string.successDeletePlanText)
                navigator.back()
            } else {
                throw DeletePlanException
            }
        }
    }

    private fun handleSaveButtonPressed(localPlanModel: LocalPlanModel) {
        val errorTextResId = when {
            localPlanModel.name.isBlank() -> R.string.errorEmptyText
            localPlanModel.startDateTime.toLong() >= localPlanModel.endDateTime.toLong() -> {
                R.string.errorInvalidTime
            }
            else -> null
        }

        if (errorTextResId == null) {
            launch(exceptionHandler = defaultExceptionHandler) {
                repository.insertPlan(localPlanModel)
                navigator.showSuccessSnackBar(R.string.successSavePlanText)
                navigator.back()
            }
        } else {
            navigator.showErrorSnackBar(errorTextResId)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            planDetails: LocalPlanModel?,
            selectedDate: Long?,
        ): PlanDetailViewModel
    }
}