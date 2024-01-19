package simbirsoft.task.dailyplanner.common.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import simbirsoft.task.dailyplanner.common.model.BaseEvent
import simbirsoft.task.dailyplanner.common.model.BaseState

abstract class BaseViewModel<S : BaseState, E : BaseEvent>(
    initState: S,
) : ViewModel() {

    abstract fun proceed(event: E)

    private val state = MutableStateFlow(initState)
    private val defaultErrorException = CoroutineExceptionHandler { _, _ -> }

    fun state(): StateFlow<S> = state.asStateFlow()

    fun requiredState() = requireNotNull(state.value)

    fun updateState(newState: S.() -> S) {
        state.value = newState.invoke(state.value)
    }

    fun launch(
        exceptionHandler: CoroutineExceptionHandler = defaultErrorException,
        coroutine: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(exceptionHandler) { coroutine.invoke(this) }
}