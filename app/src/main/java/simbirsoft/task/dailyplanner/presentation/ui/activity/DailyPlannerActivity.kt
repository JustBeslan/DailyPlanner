package simbirsoft.task.dailyplanner.presentation.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import simbirsoft.task.dailyplanner.R
import simbirsoft.task.dailyplanner.common.DailyPlannerApp
import simbirsoft.task.dailyplanner.di.DailyPlansComponent
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import javax.inject.Inject

class DailyPlannerActivity : FragmentActivity() {

    private var activityComponent: DailyPlansComponent? = null

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_planner_main_screen)
        activityComponent = (applicationContext as DailyPlannerApp).appComponent()
            .provideDailyPlannerComponentFactory()
            .create(this).apply {
                inject(this@DailyPlannerActivity)
            }
        navigator.openDailyPlannerScreen()
    }

    fun activityComponent() = activityComponent
}