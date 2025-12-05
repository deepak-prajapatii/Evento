package com.evento.ui.slotselection

import com.evento.common.UIState
import com.evento.domain.common.ErrorType
import com.evento.domain.entities.TimeSlot

data class SlotSelectionUIState(
    val isLoading: Boolean = false,
    val timeSlots: List<TimeSlot> = emptyList(),
    val selectedTimeSlot: TimeSlot? = null,
    val uiErrorType: ErrorType? = null,
    val uiErrorMessage: String? = null
) : UIState