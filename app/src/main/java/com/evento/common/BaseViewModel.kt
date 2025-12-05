package com.evento.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

abstract class BaseViewModel<STATE : UIState, EVENT : UIEvent>(initialState: STATE) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _event = MutableSharedFlow<EVENT>(extraBufferCapacity = 1)
    val event: SharedFlow<EVENT> = _event.asSharedFlow()

    val currentState: STATE
        get() = state.value

    protected fun sendEvent(uiEvent: EVENT) {
        _event.tryEmit(uiEvent)
    }

    protected fun updateState(update: (old: STATE) -> STATE): STATE = _state.updateAndGet(update)
}