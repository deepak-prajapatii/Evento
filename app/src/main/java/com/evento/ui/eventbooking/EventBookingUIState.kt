package com.evento.ui.eventbooking

import com.evento.base.UIState
import com.evento.domain.base.ErrorType
import com.evento.domain.entities.Event

data class EventBookingUIState(
    val isLoading: Boolean = false,
    val bookedEvents: List<Event> = emptyList(),
    val uiErrorType: ErrorType? = null,
    val uiErrorMessage: String? = null
) : UIState