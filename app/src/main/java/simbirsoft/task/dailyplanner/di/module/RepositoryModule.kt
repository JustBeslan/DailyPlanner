package simbirsoft.task.dailyplanner.di.module

import dagger.Binds
import dagger.Module
import simbirsoft.task.dailyplanner.data.repo.DailyPlannerRepositoryImpl
import simbirsoft.task.dailyplanner.di.ActivityScope
import simbirsoft.task.dailyplanner.domain.repo.DailyPlannerRepository

@Module
abstract class RepositoryModule {

    @Binds
    @ActivityScope
    abstract fun bindRepository(
        dailyPlannerRepositoryImpl: DailyPlannerRepositoryImpl
    ): DailyPlannerRepository
}