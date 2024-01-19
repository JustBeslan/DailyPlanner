package simbirsoft.task.dailyplanner.di.module

import dagger.Binds
import dagger.Module
import simbirsoft.task.dailyplanner.common.model.BaseMapper
import simbirsoft.task.dailyplanner.di.ActivityScope
import simbirsoft.task.dailyplanner.domain.model.LocalPlanModel
import simbirsoft.task.dailyplanner.presentation.mapper.DailyPlanMapper
import simbirsoft.task.dailyplanner.presentation.model.PlanModel

@Module(
    includes = [
        RepositoryModule::class,
    ]
)
abstract class MapperModule {

    @Binds
    @ActivityScope
    abstract fun providesDailyPlanMapper(
        dailyPlanMapper: DailyPlanMapper
    ): BaseMapper<LocalPlanModel, PlanModel>
}