package simbirsoft.task.dailyplanner.di.module

import androidx.fragment.app.FragmentManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import simbirsoft.task.dailyplanner.di.ActivityScope
import simbirsoft.task.dailyplanner.presentation.navigator.Navigator
import simbirsoft.task.dailyplanner.presentation.navigator.impl.NavigatorImpl
import simbirsoft.task.dailyplanner.presentation.ui.activity.DailyPlannerActivity

@Module(
    includes = [
        NavigatorBindsModule::class,
        NavigatorProvidesModule::class,
    ]
)
data object NavigatorModule

@Module
abstract class NavigatorBindsModule {

    @Binds
    @ActivityScope
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}

@Module
object NavigatorProvidesModule {

    @Provides
    @ActivityScope
    fun provideFragmentManager(activity: DailyPlannerActivity): FragmentManager {
        return activity.supportFragmentManager
    }
}