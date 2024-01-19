package simbirsoft.task.dailyplanner.presentation.navigator

import androidx.annotation.StringRes
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel

interface Navigator {

    fun back()

    fun openDailyPlannerScreen()

    fun openPlanDetailScreen(
        plan: LocalPlanModel? = null,
        selectedDate: Long? = null,
    )

    fun showSuccessSnackBar(@StringRes messageResId: Int)

    fun showErrorSnackBar(@StringRes messageResId: Int)
}