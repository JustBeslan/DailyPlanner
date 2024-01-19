package simbirsoft.task.dailyplanner.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import simbirsoft.task.dailyplanner.data.local.DailyPlansDao
import simbirsoft.task.dailyplanner.data.local.DailyPlansDatabase
import simbirsoft.task.dailyplanner.di.ActivityScope

@Module
object LocalSourceModule {

    @Provides
    @ActivityScope
    fun providesDao(database: DailyPlansDatabase): DailyPlansDao {
        return database.dailyPlansDao()
    }

    @Provides
    @ActivityScope
    fun providesDatabase(application: Application): DailyPlansDatabase {
        return DailyPlansDatabase.getInstance(application)
    }
}