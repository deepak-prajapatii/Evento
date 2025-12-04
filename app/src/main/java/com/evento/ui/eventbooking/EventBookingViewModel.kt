package com.evento.ui.eventbooking

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.evento.base.BaseViewModel
import com.evento.domain.usecase.GetBookedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventBookingViewModel @Inject constructor(
    private val getBookedEventsUseCase: GetBookedEventsUseCase
) : BaseViewModel<EventBookingUIState, EventBookingUIEvent>(initialState = EventBookingUIState()) {

    init {
        getBookedEvents()
    }

    private fun getBookedEvents() {
        viewModelScope.launch {
            updateState { state -> state.copy(isLoading = true) }
            getBookedEventsUseCase.execute(Unit)
                .collect { either ->
                    either.onSuccess {
                        updateState { state -> state.copy(bookedEvents = it, isLoading = false) }
                    }.onFailure {
                        updateState { state -> state.copy(isLoading = false) }
                    }
                }
        }
    }
}