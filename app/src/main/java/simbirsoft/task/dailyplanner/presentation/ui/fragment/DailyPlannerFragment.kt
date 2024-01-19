package simbirsoft.task.dailyplanner.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import simbirsoft.task.dailyplanner.common.extensions.composeView
import simbirsoft.task.dailyplanner.common.ui.BaseFragment
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme
import simbirsoft.task.dailyplanner.presentation.model.event.DailyPlannerEvent
import simbirsoft.task.dailyplanner.presentation.model.state.DailyPlannerState
import simbirsoft.task.dailyplanner.presentation.ui.compose.DailyPlannerScreen
import simbirsoft.task.dailyplanner.presentation.viewmodel.DailyPlannerViewModel
import javax.inject.Inject

class DailyPlannerFragment : BaseFragment<DailyPlannerState,
        DailyPlannerEvent, DailyPlannerViewModel>() {

    companion object {
        fun newInstance(): Fragment {
            return DailyPlannerFragment()
        }
    }

    @Inject
    lateinit var factory: DailyPlannerViewModel.Factory

    override val viewModel: DailyPlannerViewModel by viewModels {
        viewModelFactory {
            initializer {
                factory.create()
            }
        }
    }

    override fun injecting() {
        activityComponent()?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeView {
        DailyPlannerTheme {
            val state by viewModel.state().collectAsState()
            DailyPlannerScreen(
                state = state,
                onEvent = viewModel::proceed
            )
        }
    }
}