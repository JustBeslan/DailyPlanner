package simbirsoft.task.dailyplanner.presentation.navigator.impl

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import simbirsoft.task.dailyplanner.presentation.ui.fragment.DailyPlannerFragment
import simbirsoft.task.dailyplanner.presentation.ui.fragment.PlanDetailFragment
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {

    @Inject
    lateinit var fragmentManager: FragmentManager

    override fun back() {
        fragmentManager.popBackStack()
    }

    private fun openFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true
    ) {
        val tag = fragment::class.simpleName
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_next_screen,
                R.anim.exit_current_screen,
                R.anim.enter_prev_screen,
                R.anim.exit_next_screen
            )
            setReorderingAllowed(true)
            replace(R.id.DailyPlanerFragmentContainerView, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }

    private fun getView(): FragmentContainerView =
        fragmentManager.fragments.last().requireActivity()
            .findViewById(R.id.DailyPlanerFragmentContainerView)


    override fun openDailyPlannerScreen() {
        openFragment(
            fragment = DailyPlannerFragment.newInstance()
        )
    }

    override fun openPlanDetailScreen(
        plan: LocalPlanModel?,
        selectedDate: Long?,
    ) {
        openFragment(
            fragment = PlanDetailFragment.newInstance(
                plan = plan,
                selectedDate = selectedDate,
            )
        )
    }

    private fun showSnackBar(
        @StringRes messageResId: Int,
        @ColorRes colorResId: Int,
    ) {
        val view = getView()
        val color = ContextCompat.getColor(view.context, colorResId)
        Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT).apply {
            this.view.setBackgroundColor(color)
        }.show()
    }

    override fun showSuccessSnackBar(messageResId: Int) = showSnackBar(
        messageResId = messageResId,
        colorResId = R.color.green
    )

    override fun showErrorSnackBar(messageResId: Int) = showSnackBar(
        messageResId = messageResId,
        colorResId = R.color.red
    )
}