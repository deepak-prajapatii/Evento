package com.evento.ui.eventbooking

import com.evento.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventBookingViewModel @Inject constructor() :
    BaseViewModel<EventBookingUIState, EventBookingUIEvent>(initialState = EventBookingUIState()) {
}