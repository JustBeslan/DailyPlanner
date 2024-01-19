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
import simbirsoft.task.dailyplanner.common.extensions.withArgs
import simbirsoft.task.dailyplanner.common.ui.BaseFragment
import simbirsoft.task.dailyplanner.common.ui.theme.DailyPlannerTheme
import simbirsoft.task.dailyplanner.domain.exceptions.ExtractArgumentValueException
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.presentation.model.event.PlanDetailEvent
import simbirsoft.task.dailyplanner.presentation.model.state.PlanDetailState
import simbirsoft.task.dailyplanner.presentation.ui.compose.PlanDetailScreen
import simbirsoft.task.dailyplanner.presentation.viewmodel.PlanDetailViewModel
import javax.inject.Inject

class PlanDetailFragment : BaseFragment<PlanDetailState, PlanDetailEvent, PlanDetailViewModel>() {

    companion object {
        private const val DETAIL_PLAN_KEY = "detail_plan_key"
        private const val SELECTED_DATE_KEY = "selected_date_key"

        fun newInstance(
            plan: LocalPlanModel?,
            selectedDate: Long?,
        ): Fragment {
            return PlanDetailFragment().withArgs {
                plan?.let { putParcelable(DETAIL_PLAN_KEY, it) }
                selectedDate?.let { putLong(SELECTED_DATE_KEY, it) }
            }
        }
    }

    @Inject
    lateinit var factory: PlanDetailViewModel.Factory

    override fun injecting() {
        activityComponent()?.inject(this)
    }

    override val viewModel: PlanDetailViewModel by viewModels {
        viewModelFactory {
            initializer {
                val planDetails = try {
                    getRequiredArgument<LocalPlanModel?>(
                        key = DETAIL_PLAN_KEY,
                        handler = { this.getParcelable(it, LocalPlanModel::class.java) },
                        oldHandler = { this.getParcelable(it) },
                    )
                } catch (e: ExtractArgumentValueException) {
                    null
                }
                val selectedDate = try {
                    getRequiredArgument<Long?>(
                        key = SELECTED_DATE_KEY,
                        handler = { this.getLong(it) }
                    )
                } catch (e: ExtractArgumentValueException) {
                    null
                }
                factory.create(planDetails, selectedDate)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeView {
        DailyPlannerTheme {
            val state by viewModel.state().collectAsState()
            PlanDetailScreen(
                state = state,
                onEvent = viewModel::proceed
            )
        }
    }
}