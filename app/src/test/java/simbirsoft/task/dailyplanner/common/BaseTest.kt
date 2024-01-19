package simbirsoft.task.dailyplanner.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {

    val unconfinedTestDispatcher = UnconfinedTestDispatcher().apply {
        Dispatchers.setMain(this)
    }
}