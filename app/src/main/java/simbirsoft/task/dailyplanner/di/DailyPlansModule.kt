package simbirsoft.task.dailyplanner.di

import dagger.Module
import simbirsoft.task.dailyplanner.di.module.LocalSourceModule
import simbirsoft.task.dailyplanner.di.module.MapperModule
import simbirsoft.task.dailyplanner.di.module.NavigatorModule
import simbirsoft.task.dailyplanner.di.module.RepositoryModule

@Module(
    includes = [
        RepositoryModule::class,
        NavigatorModule::class,
        LocalSourceModule::class,
        MapperModule::class,
    ]
)
data object DailyPlansModule