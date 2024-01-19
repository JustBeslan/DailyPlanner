package simbirsoft.task.dailyplanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import simbirsoft.task.dailyplanner.domain.model.DailyPlans
import simbirsoft.task.dailyplanner.domain.model.DailyPlansTime
import simbirsoft.task.dailyplanner.domain.model.TimeConverter

@Database(
    entities = [
        DailyPlans::class,
        DailyPlansTime::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TimeConverter::class)
abstract class DailyPlansDatabase : RoomDatabase() {

    abstract fun dailyPlansDao(): DailyPlansDao

    companion object {
        private const val DATABASE_NAME = "daily_plans_db"

        @Volatile
        private var instance: DailyPlansDatabase? = null

        fun getInstance(context: Context): DailyPlansDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): DailyPlansDatabase =
            Room.databaseBuilder(context, DailyPlansDatabase::class.java, DATABASE_NAME).build()
    }
}