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
        Log.d("EventVM", "init called")
        getBookedEvents()
    }

    private fun getBookedEvents() {
        viewModelScope.launch {
            getBookedEventsUseCase.execute(Unit)
                .collect { either ->
                    either.onSuccess {
                        updateState { state -> state.copy(bookedEvents = it) }
                    }.onFailure {

                    }
                }
        }
    }
}