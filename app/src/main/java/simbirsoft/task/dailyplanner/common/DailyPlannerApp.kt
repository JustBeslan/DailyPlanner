package simbirsoft.task.dailyplanner.common

import android.app.Application
import simbirsoft.task.dailyplanner.di.AppComponent
import simbirsoft.task.dailyplanner.di.DaggerAppComponent

class DailyPlannerApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    fun appComponent() = appComponent
}