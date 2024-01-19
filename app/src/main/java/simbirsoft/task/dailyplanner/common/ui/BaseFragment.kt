package simbirsoft.task.dailyplanner.common.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import simbirsoft.task.dailyplanner.common.model.BaseEvent
import simbirsoft.task.dailyplanner.common.model.BaseState
import simbirsoft.task.dailyplanner.domain.exceptions.ExtractArgumentValueException
import simbirsoft.task.dailyplanner.presentation.ui.activity.DailyPlannerActivity

abstract class BaseFragment<S : BaseState, E : BaseEvent, VM : BaseViewModel<S, E>> : Fragment() {

    abstract val viewModel: VM

    abstract fun injecting()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injecting()
    }

    private fun requiredArguments() = requireNotNull(arguments)

    fun <T> getRequiredArgument(
        key: String,
        oldHandler: (Bundle.(String) -> T?)? = null,
        handler: Bundle.(String) -> T?,
    ): T? {
        if (arguments == null || requiredArguments().containsKey(key).not()) {
            throw ExtractArgumentValueException
        } else {
            return if (oldHandler == null
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                handler.invoke(requiredArguments(), key)
            } else {
                oldHandler.invoke(requiredArguments(), key)
            }
        }
    }

    fun activityComponent() = (requireActivity() as DailyPlannerActivity).activityComponent()
}