package simbirsoft.task.dailyplanner.di

import dagger.BindsInstance
import dagger.Subcomponent
import simbirsoft.task.dailyplanner.presentation.ui.activity.DailyPlannerActivity
import simbirsoft.task.dailyplanner.presentation.ui.fragment.DailyPlannerFragment
import simbirsoft.task.dailyplanner.presentation.ui.fragment.PlanDetailFragment
import simbirsoft.task.dailyplanner.presentation.viewmodel.DailyPlannerViewModel
import simbirsoft.task.dailyplanner.presentation.viewmodel.PlanDetailViewModel

@Subcomponent(modules = [DailyPlansModule::class])
@ActivityScope
interface DailyPlansComponent {

    fun inject(activity: DailyPlannerActivity)
    fun inject(fragment: DailyPlannerFragment)
    fun inject(fragment: PlanDetailFragment)

    fun provideDetailPlanViewModelFactory(): PlanDetailViewModel.Factory
    fun provideDailyPlannerViewModelFactory(): DailyPlannerViewModel.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: DailyPlannerActivity): DailyPlansComponent
    }
}